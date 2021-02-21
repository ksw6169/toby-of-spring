package com.corgi.example.service;

import com.corgi.example.exception.SqlNotFoundException;
import com.corgi.example.exception.SqlUpdateFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
    private final NamedParameterJdbcTemplate template;

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        params.put("sql", sql);

        int affected = template.update("update sqlmap set sql_ = :sql where key_ = :key", params);

        if (affected == 0) {
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다.");
        }
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        params.put("sql", sql);

        template.update("insert into sqlmap(key_, sql_) values(:key, :sql)", params);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);

        try {
            return template.queryForObject("select sql_ from sqlmap where key_ = :key", params, String.class);
        } catch (EmptyResultDataAccessException e) {
            throw new SqlNotFoundException(e);
        }
    }
}
