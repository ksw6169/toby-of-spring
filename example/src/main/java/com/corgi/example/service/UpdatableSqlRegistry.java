package com.corgi.example.service;

import com.corgi.example.exception.SqlUpdateFailureException;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry {
    void updateSql(String key, String sql) throws SqlUpdateFailureException;

    void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
