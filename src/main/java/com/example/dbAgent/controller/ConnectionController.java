package com.example.dbAgent.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dbAgent.model.DbConfigEntity;
import com.example.dbAgent.service.ConnectionService;

@RestController
@RequestMapping("/connections")
public class ConnectionController {

    private final ConnectionService service;

    public ConnectionController(ConnectionService service) {
        this.service = service;
    }

    @PostMapping
    public String create(@RequestBody DbConfigEntity config) {
        service.createConnection(config);
        return "Connection created";
    }
}