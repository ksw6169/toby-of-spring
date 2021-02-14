package com.corgi.example.service;

import com.corgi.example.exception.SqlNotFoundException;

public interface SqlRegistry {
    void registerSql(String key, String sql);               // SQL을 키와 함께 등록한다.
    String findSql(String key) throws SqlNotFoundException; // 키로 SQL을 검색한다. 검색에 실패하면 예외를 던진다.
}
