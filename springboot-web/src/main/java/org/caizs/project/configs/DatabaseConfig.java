package org.caizs.project.configs;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.google.common.collect.Lists.newArrayList;

//@Configuration
//@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
//@ImportResource("classpath:/com/lianfan/project/configs/aopConfig.xml")
public class DatabaseConfig implements EnvironmentAware {

    private RelaxedPropertyResolver propertyResolver;

    private DruidDataSource druidDataSource;

    /** * mybatis mapper resource 路径 */
    private static String[] MAPPER_PATH = { "classpath*:/com/lianfan/project/**/dao/mapper/*.xml" };

    // private static String typeAliasPackage = "org.caizs.detail.domain,";
    // //不推荐用别名

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
    }

    @Bean
    public ServletRegistrationBean druidServlet(@Value("${spring.datasource.statview.login.username}") String username,
            @Value("${spring.datasource.statview.login.password}") String password, @Value("${spring.datasource.statview.login.allow}") String allow,
            @Value("${spring.datasource.statview.login.deny}") String deny) {
        ServletRegistrationBean filter = new ServletRegistrationBean();
        filter.setServlet(new StatViewServlet());
        filter.setName("druidStatView");
        // 登录URL http://localhost:8080/druid/
        filter.addUrlMappings("/druid/*");
        // 设置白名单
        filter.addInitParameter("allow", allow);
        // 设置黑名单
        filter.addInitParameter("deny", deny);
        // 设置登录查看信息的账号密码.
        filter.addInitParameter("loginUsername", username);
        filter.addInitParameter("loginPassword", password);
        // 禁用HTML页面上的"Reset All"功能
        filter.addInitParameter("resetEnable", "false");
        return filter;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions",
                "*.js, *.gif, *.jpg, *.png, *.css, *.ico, *.swf, /druid/*, /static/*, /public/*, /resources/*");
        return filterRegistrationBean;
    }

    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(getDruidDataSourceInstance());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置mybatis配置
        sqlSessionFactoryBean.setConfiguration(configuration());
        // 设置数据源
        sqlSessionFactoryBean.setDataSource(getDruidDataSourceInstance());
        // 设置分页插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[] { pageHelperPlugin() });
        // 设置mapperxml 扫描路径
        sqlSessionFactoryBean.setMapperLocations(mapperXml());
        // sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        return sqlSessionFactoryBean.getObject();
    }

    private synchronized DruidDataSource getDruidDataSourceInstance() throws SQLException {
        if (this.druidDataSource == null) {
            this.druidDataSource = dataSource();
        }
        return this.druidDataSource;
    }

    // 注册dataSource
    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(propertyResolver.getProperty("driver-class-name"));
        druidDataSource.setUrl(propertyResolver.getProperty("url"));
        druidDataSource.setUsername(propertyResolver.getProperty("username"));
        druidDataSource.setPassword(propertyResolver.getProperty("password"));
        druidDataSource.setInitialSize(Integer.parseInt(propertyResolver.getProperty("initialSize")));
        druidDataSource.setMinIdle(Integer.parseInt(propertyResolver.getProperty("minIdle")));
        druidDataSource.setMaxActive(Integer.parseInt(propertyResolver.getProperty("maxActive")));
        druidDataSource.setMaxWait(Integer.parseInt(propertyResolver.getProperty("maxWait")));
        druidDataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(propertyResolver.getProperty("timeBetweenEvictionRunsMillis")));
        druidDataSource.setMinEvictableIdleTimeMillis(Long.parseLong(propertyResolver.getProperty("minEvictableIdleTimeMillis")));
        druidDataSource.setValidationQuery(propertyResolver.getProperty("validationQuery"));
        druidDataSource.setTestWhileIdle(Boolean.parseBoolean(propertyResolver.getProperty("testWhileIdle")));
        druidDataSource.setTestOnBorrow(Boolean.parseBoolean(propertyResolver.getProperty("testOnBorrow")));
        druidDataSource.setTestOnReturn(Boolean.parseBoolean(propertyResolver.getProperty("testOnReturn")));
        druidDataSource.setPoolPreparedStatements(Boolean.parseBoolean(propertyResolver.getProperty("poolPreparedStatements")));
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(
                Integer.parseInt(propertyResolver.getProperty("maxPoolPreparedStatementPerConnectionSize")));
        druidDataSource.setFilters(propertyResolver.getProperty("filters"));
        druidDataSource.setConnectionProperties(propertyResolver.getProperty("connectionProperties"));
        return druidDataSource;
    }

    private org.apache.ibatis.session.Configuration configuration() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        // ...
        return configuration;
    }

    /**
     * 分页插件
     */
    private PageHelper pageHelperPlugin() {
        PageHelper pageHelper = new PageHelper();
        Properties props = new Properties();
        // props.setProperty("dialect", "mysql"); //可以自动侦测
        props.setProperty("offsetAsPageNum", "false");
        props.setProperty("rowBoundsWithCount", "false");
        props.setProperty("pageSizeZero", "true");
        props.setProperty("reasonable", "false");
        props.setProperty("returnPageInfo", "none");
        pageHelper.setProperties(props);
        return pageHelper;
    }

    /**
     * mapper xml资源
     */
    private Resource[] mapperXml() throws IOException {
        PathMatchingResourcePatternResolver reolver = new PathMatchingResourcePatternResolver();
        List<Resource> list = newArrayList();
        for (String path : MAPPER_PATH) {
            list.addAll(Arrays.asList(reolver.getResources(path)));
        }
        return list.toArray(new Resource[] {});
    }

}
