package com.corgi.example.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConcurrentHashMapSqlRegistryTests extends AbstractUpdatableSqlRegistryTests {
    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
