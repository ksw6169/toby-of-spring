package com.corgi.example.service.sql;

import com.corgi.example.exception.SqlRetrievalFailureException;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
