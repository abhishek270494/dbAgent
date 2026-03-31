package com.example.dbAgent.service;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.stereotype.Service;

import com.example.dbAgent.model.DbConfigEntity;
import com.example.dbAgent.repository.DbConfigRepository;

@Service
public class ConnectionService {

    private final DbConfigRepository repository;
    private final DataSourceManager manager;

    public ConnectionService(DbConfigRepository repository,
                             DataSourceManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    public void createConnection(DbConfigEntity config) {
        repository.save(config);
        manager.createAndRegister(config);
    }

    public static String buildJdbcUrl(DbConfigEntity config) {
        return switch (config.getType()) {
            case "postgres" ->
                    "jdbc:postgresql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName();
            case "mysql" ->
                    "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName();
            case "oracle" ->
                    "jdbc:oracle:thin:@" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName();
            default -> throw new RuntimeException("Unsupported DB");
        };
    }

    /** Test a connection before registering */
    public boolean testConnection(DbConfigEntity config) {
        String url = buildJdbcUrl(config);

        try (Connection conn = DriverManager.getConnection(
                url,
                config.getUsername(),
                config.getPassword())) {

            return conn.isValid(3); // timeout 3 sec

        } catch (Exception e) {
            throw new RuntimeException("Connection test failed: " + e.getMessage(), e);
        }
    }
}