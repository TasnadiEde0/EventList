package edu.bbte.idde.teim2310.spring.dataaccess.indb;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Slf4j
@Configuration
@Profile("indb")
public class ConfigurationBean {

    @Value("${spring.jdbc.dataSourceDriver}")
    private String dataSourceDriver;
    @Value("${spring.jdbc.dataSourceJdbcUrl}")
    private String dataSourceJdbcUrl;
    @Value("${spring.jdbc.dataSourceUsername}")
    private String dataSourceUsername;
    @Value("${spring.jdbc.dataSourcePassword}")
    private String dataSourcePassword;
    @Value("${spring.jdbc.dataSourceMaximumPoolSize}")
    private int dataSourceMaximumPoolSize;

    @Bean
    public DataSource getDataSource() {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(dataSourceDriver);
        hikariConfig.setJdbcUrl(dataSourceJdbcUrl);
        hikariConfig.setUsername(dataSourceUsername);
        hikariConfig.setPassword(dataSourcePassword);
        hikariConfig.setMaximumPoolSize(dataSourceMaximumPoolSize);
        return new HikariDataSource(hikariConfig);

    }



}
