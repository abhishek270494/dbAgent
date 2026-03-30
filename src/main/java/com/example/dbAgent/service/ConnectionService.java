package com.example.dbAgent.service;

import java.util.List;

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

    public List<DbConfigEntity> getAll() {
        return repository.findAll();
    }
}