package org.caizs.project.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
//@MapperScan(sqlSessionFactoryRef = "userSessionFactory")
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@ImportResource("classpath:/org/caizs/project/configs/aopConfig.xml")
public class UserDataSourceConfig {

    private static final String MAPPERLOCATIONS = "classpath*:/com/lianfan/project/**/dao/mapper/*.xml";
    //private static final String TYPEALIASESPACKAGE = "org.caizs.detail.domain,";

    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "custom.datasource.user")
    @Primary
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean("transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(
            @Qualifier("userDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "userSessionFactory")
    @Primary
    public SqlSessionFactory sessionFactory(
            @Qualifier("userDataSource") DataSource dataSource) throws Exception {

        //===1.mybatis-plus globalConfig配置
        GlobalConfiguration globalConfig = new GlobalConfiguration();
        // 字段的驼峰下划线转换
        globalConfig.setDbColumnUnderline(Boolean.TRUE);
        globalConfig.setIdType(IdType.AUTO.getKey());

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);

        //===2.构造sessionFactory(mybatis-plus)
        final MybatisSqlSessionFactoryBean sf = new MybatisSqlSessionFactoryBean();
        sf.setDataSource(dataSource);
        sf.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPERLOCATIONS));
        // sf.setTypeAliasesPackage(TYPEALIASESPACKAGE);
        sf.setGlobalConfig(globalConfig);
        sf.setConfiguration(configuration);
        // 分页插件
        sf.setPlugins(new Interceptor[] { new PaginationInterceptor() });

        return sf.getObject();
    }
}  