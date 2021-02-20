package com.corgi.example.service;

import com.corgi.example.exception.SqlNotFoundException;
import com.corgi.example.exception.SqlRetrievalFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

//@RequiredArgsConstructor
//@Service
//public class XmlSqlService implements SqlService {
//    private final SqlReader sqlReader;
//    private final SqlRegistry sqlRegistry;
//
//    @PostConstruct
//    public void loadSql() {
//        sqlReader.read(sqlRegistry);
//    }
//
//    @Override
//    public String getSql(String key) throws SqlRetrievalFailureException {
//        try {
//            return sqlRegistry.findSql(key);
//        } catch (SqlNotFoundException e) {
//            throw new SqlRetrievalFailureException(e);
//        }
//    }
//}
