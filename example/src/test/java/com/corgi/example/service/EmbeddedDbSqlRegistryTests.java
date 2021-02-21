package com.corgi.example.service;

import org.junit.jupiter.api.AfterEach;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class EmbeddedDbSqlRegistryTests extends AbstractUpdatableSqlRegistryTests {
    protected EmbeddedDatabase database;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:sql/schema.sql")
                .addScript("classpath:sql/data.sql")
                .build();

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(database);

        return new EmbeddedDbSqlRegistry(template);
    }

    @AfterEach
    void tearDown() {
        database.shutdown();
    }
}
