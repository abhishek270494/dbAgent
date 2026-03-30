package com.example.dbAgent.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dbAgent.model.DbConfigEntity;
import com.example.dbAgent.repository.DbConfigRepository;
import com.example.dbAgent.service.DataSourceManager;

import jakarta.annotation.PostConstruct;

@Component
public class StartupLoader {

    private final DbConfigRepository repository;
    private final DataSourceManager manager;

    public StartupLoader(DbConfigRepository repository,
                         DataSourceManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    @PostConstruct
    public void loadAllConnections() {
        List<DbConfigEntity> configs = repository.findAll();

        configs.forEach(manager::createAndRegister);

        System.out.println("Loaded " + configs.size() + " DB connections");
    }
}