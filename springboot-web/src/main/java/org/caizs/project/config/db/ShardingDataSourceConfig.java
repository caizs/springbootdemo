package org.caizs.project.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
//@MapperScan(sqlSessionFactoryRef = "shardSessionFactory")
public class ShardingDataSourceConfig {

    private static final String MAPPERLOCATIONS = "classpath*:/com/lianfan/project/**/dao/mapper/*.xml";
   // private static final String TYPEALIASESPACKAGE = "org.caizs.detail.domain,";


    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "custom.datasource.sharding.master")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "custom.datasource.sharding.slave")
    public DataSource slaveDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "shardDataSource")
    public DataSource shardDataSource(@Qualifier("masterDataSource") DataSource master,
            @Qualifier("slaveDataSource") DataSource slave)
            throws SQLException {
        //===1.构建shard数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", master);
        dataSourceMap.put("slave0", slave);

        //===2.构建读写分离配置
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration();
        masterSlaveRuleConfig.setName("shard_ds");
        masterSlaveRuleConfig.setMasterDataSourceName("master");
        masterSlaveRuleConfig.getSlaveDataSourceNames().add("slave0");

        //===3.返回shard数据源
        return MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfig,
                new ConcurrentHashMap<>());
    }

    @Bean(name = "shardTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("shardDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "shardSessionFactory")
    public SqlSessionFactory sessionFactory(
            @Qualifier("shardDataSource") DataSource dataSource) throws Exception {

        //===1.mybatis-plus globalConfig配置
        GlobalConfiguration globalConfig = new GlobalConfiguration();
        // 字段的驼峰下划线转换
        globalConfig.setDbColumnUnderline(Boolean.TRUE);

        //===2.构造sessionFactory(mybatis-plus)
        final MybatisSqlSessionFactoryBean sf = new MybatisSqlSessionFactoryBean();
        sf.setDataSource(dataSource);
        sf.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPERLOCATIONS));
       // sf.setTypeAliasesPackage(TYPEALIASESPACKAGE);
        sf.setGlobalConfig(globalConfig);
        // 分页插件
        sf.setPlugins(new Interceptor[] { new PaginationInterceptor() });

        return sf.getObject();
    }
}