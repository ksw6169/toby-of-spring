package com.corgi.example.service.sql;

import com.corgi.example.exception.SqlUpdateFailureException;
import com.corgi.example.service.sql.SqlRegistry;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry {
    void updateSql(String key, String sql) throws SqlUpdateFailureException;

    void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
