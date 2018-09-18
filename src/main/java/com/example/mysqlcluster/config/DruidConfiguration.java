package com.example.mysqlcluster.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by zm on 2018/9/18.
 */
@Configuration
public class DruidConfiguration {

    @Qualifier("master")
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource dataSourceMaster() {
        return DruidDataSourceBuilder.create().build();
    }

    @Qualifier("slave")
    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    public DataSource dataSourceSlave() {
        return DruidDataSourceBuilder.create().build();
    }
}
