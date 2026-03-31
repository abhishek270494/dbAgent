package com.example.dbAgent.service;

import static com.example.dbAgent.service.ConnectionService.buildJdbcUrl;

import java.util.HashMap;
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

    public Map<String, Map<String, Object>> getAllPoolStats() {
        Map<String, Map<String, Object>> stats = new HashMap<>();

        dataSourceMap.forEach((key, ds) -> {
            if (ds instanceof HikariDataSource hikari) {

                var mxBean = hikari.getHikariPoolMXBean();

                Map<String, Object> poolStats = new HashMap<>();
                poolStats.put("activeConnections", mxBean.getActiveConnections());
                poolStats.put("idleConnections", mxBean.getIdleConnections());
                poolStats.put("totalConnections", mxBean.getTotalConnections());
                poolStats.put("threadsAwaitingConnection", mxBean.getThreadsAwaitingConnection());

                stats.put(key, poolStats);
            }
        });

        return stats;
    }

    public void remove(String dbId) {
        DataSource ds = dataSourceMap.remove(dbId);

        if (ds == null) {
            throw new RuntimeException("No datasource found for id: " + dbId);
        }

        if (ds instanceof HikariDataSource hikari) {
            hikari.close(); // 🔥 VERY IMPORTANT
            System.out.println("Pool closed for: " + dbId);
        }
    }

}