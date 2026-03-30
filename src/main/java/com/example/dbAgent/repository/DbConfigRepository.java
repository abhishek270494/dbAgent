package com.example.dbAgent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dbAgent.model.DbConfigEntity;

@Repository
public interface DbConfigRepository extends JpaRepository<DbConfigEntity, String> {
}