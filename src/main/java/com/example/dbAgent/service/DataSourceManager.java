package com.example.dbAgent.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.dbAgent.model.DbConfigEntity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class DataSourceManager {

    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    @Value("${app.datasource.pool-size}")
    private int poolSize;

    public void createAndRegister(DbConfigEntity config) {

        HikariConfig hikari = new HikariConfig();

        hikari.setJdbcUrl(buildJdbcUrl(config));
        hikari.setUsername(config.getUsername());
        hikari.setPassword(config.getPassword());
        hikari.setMaximumPoolSize(poolSize);

        HikariDataSource ds = new HikariDataSource(hikari);

        dataSourceMap.put(config.getDbId(), ds);
    }

    public DataSource get(String id) {
        return dataSourceMap.get(id);
    }

    private String buildJdbcUrl(DbConfigEntity config) {
        switch (config.getType()) {
            case "postgres":
                return "jdbc:postgresql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName();
            case "mysql":
                return "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName();
            case "oracle":
                return "jdbc:oracle:thin:@" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName();
            default:
                throw new RuntimeException("Unsupported DB");
        }
    }
}