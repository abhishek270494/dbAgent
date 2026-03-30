package com.example.dbAgent.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import com.example.dbAgent.dto.QueryRequest;

@Service
public class QueryService {

    private final DataSourceManager manager;

    public QueryService(DataSourceManager manager) {
        this.manager = manager;
    }

    public List<Map<String, Object>> execute(QueryRequest request) throws Exception {

        DataSource ds = manager.get(request.getConnectionId());

        if (ds == null) {
            throw new RuntimeException("Connection not found");
        }

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(request.getQuery())) {

            List<Map<String, Object>> result = new ArrayList<>();
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                result.add(row);
            }

            return result;
        }
    }
}