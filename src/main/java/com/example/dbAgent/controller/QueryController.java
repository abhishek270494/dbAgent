package com.example.dbAgent.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dbAgent.dto.QueryRequest;
import com.example.dbAgent.service.QueryService;

@RestController
@RequestMapping("/query")
public class QueryController {

    private final QueryService service;

    public QueryController(QueryService service) {
        this.service = service;
    }

    @PostMapping
    public Object execute(@RequestBody QueryRequest request) throws Exception {
        return service.execute(request);
    }
}