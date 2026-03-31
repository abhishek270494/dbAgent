package com.example.dbAgent.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dbAgent.model.DbConfigEntity;
import com.example.dbAgent.service.ConnectionService;
import com.example.dbAgent.service.DataSourceManager;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService service;
    private final DataSourceManager dataSourceManager;

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody DbConfigEntity config) {
        // 1. Test connection first
        if (!service.testConnection(config)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Cannot connect to database. Check credentials/URL."));
        }
        service.createConnection(config);
        return ResponseEntity.ok(Map.of("message", "Pool '" + config.getDatabaseName() + "' registered."));
    }

    /** List all pools with live stats */
    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        return ResponseEntity.ok(dataSourceManager.getAllPoolStats());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        dataSourceManager.remove(id);
        return ResponseEntity.ok(Map.of(
                "message", "Connection removed and pool shut down"
        ));
    }
}