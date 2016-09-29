package com.lawyer.xia.config.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by lindeng on 9/26/2016.
 */
@Configuration
@EnableTransactionManagement
public class DruidDataSource  implements EnvironmentAware {
    private RelaxedPropertyResolver propertyResolver;

    @Bean
    public ServletRegistrationBean druidServlet() {
        return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
    }

    @Bean(destroyMethod = "close", initMethod = "init")
    public DataSource writeDataSource() throws SQLException {
        System.out.println("注入druid！！！");


        com.alibaba.druid.pool.DruidDataSource dataSource = new com.alibaba.druid.pool.DruidDataSource();
        dataSource.setUrl(propertyResolver.getProperty("url"));
        dataSource.setUsername(propertyResolver.getProperty("username"));//用户名
        dataSource.setPassword(propertyResolver.getProperty("password"));//密码
        dataSource.setDriverClassName(propertyResolver.getProperty("driver-class-name"));
        dataSource.setInitialSize(Integer.parseInt(propertyResolver.getProperty("initialSize")));
        dataSource.setMaxActive(Integer.parseInt(propertyResolver.getProperty("maxActive")));
        dataSource.setMinIdle(Integer.parseInt(propertyResolver.getProperty("minIdle")));
        dataSource.setMaxWait(Integer.parseInt(propertyResolver.getProperty("maxWait")));
        dataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(propertyResolver.getProperty("timeBetweenEvictionRunsMillis")));
        dataSource.setMinEvictableIdleTimeMillis(Integer.parseInt(propertyResolver.getProperty("minEvictableIdleTimeMillis")));
        dataSource.setValidationQuery(propertyResolver.getProperty("validationQuery"));
        dataSource.setTestOnBorrow(Boolean.getBoolean(propertyResolver.getProperty("testOnBorrow")));
        dataSource.setTestWhileIdle(Boolean.getBoolean(propertyResolver.getProperty("testWhileIdle")));
        dataSource.setTestOnReturn(Boolean.getBoolean(propertyResolver.getProperty("testOnReturn")));
        dataSource.setPoolPreparedStatements(Boolean.getBoolean(propertyResolver.getProperty("poolPreparedStatements")));
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(propertyResolver.getProperty("maxPoolPreparedStatementPerConnectionSize")));
        //配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
        dataSource.setFilters(propertyResolver.getProperty("filters"));
        return dataSource;
    }
}
