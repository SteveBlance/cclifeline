package com.codaconsultancy.cclifeline.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.codaconsultancy.cclifeline.domain"})
@EnableJpaRepositories(basePackages = {"com.codaconsultancy.cclifeline.repositories"})
@EnableTransactionManagement
public class RepositoryConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//      remember to change for aws
        dataSource.setUrl("jdbc:mysql://localhost:3306/cclifeline");
        dataSource.setUsername("root");
        dataSource.setPassword("nottherealPassword123!");

        return dataSource;
    }
}