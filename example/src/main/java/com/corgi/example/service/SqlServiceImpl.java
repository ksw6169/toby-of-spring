package com.corgi.example.service;

import com.corgi.example.exception.SqlRetrievalFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class SqlServiceImpl implements SqlService {
    private final Map<String, String> sqlMap;

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return sqlMap.get(key);
    }
}
