package com.ucsmy.collection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2015/12/30.
 */
@Configuration
//@PropertySource(value = "application.yml")
public class GlobalDataConfiguration {

    @Value("${spring.spiderDataSource.driverClassName}")
    private String driver;
    @Value("${spring.spiderDataSource.url}")
    private String url;
    @Value("${spring.spiderDataSource.username}")
    private String username;
    @Value("${spring.spiderDataSource.password}")
    private String password;

    @Autowired
    private Environment env;

    @Bean(name="primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix="spiderDataSource")
    public DataSource primaryDataSource() {
        System.out.println("-------------------- primaryDataSource init ---------------------");
              DataSourceBuilder factory = DataSourceBuilder
              .create()
              .driverClassName(driver)
                      .url(url)
              .username(username)
              .password(password);
        return factory.build();
    }

    @Bean(name="secondaryDataSource")
    @ConfigurationProperties(prefix="spring.secondary")
    public DataSource secondaryDataSource() {
        System.out.println("-------------------- secondaryDataSource init ---------------------");
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(env.getProperty("spring.secondary.driverClassName"));
        config.setJdbcUrl(env.getProperty("spring.secondary.url"));
        config.setUsername(env.getProperty("spring.secondary.username"));
        config.setPassword(env.getProperty("spring.secondary.password"));

        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        return new HikariDataSource(config);
//        return DataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(secondaryDataSource());

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setMapperLocations(resolver
                .getResources("classpath:/mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }


    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(primaryDataSource());
    }


}
