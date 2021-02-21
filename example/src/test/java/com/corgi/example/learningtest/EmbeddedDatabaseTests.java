package com.corgi.example.learningtest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmbeddedDatabaseTests {
    private EmbeddedDatabase database;
    private NamedParameterJdbcTemplate template;    // JdbcTemplate을 더 편리하게 사용할 수 있게 확장한 템플릿(SimpleJdbcTemplate은 deprecated 되어 NamedParameterJdbcTemplate 사용)

    @BeforeEach
    void setUp() {
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)         // 내장형 DB 종류
                .addScript("classpath:sql/schema.sql")      // 초기화에 사용할 DB 스크립트 경로 지정(여러 개 지정 가능)
                .addScript("classpath:sql/data.sql")
                .build();

        template = new NamedParameterJdbcTemplate(database);
    }

    /**
     * 매 테스트를 진행한 뒤에 DB를 종료한다.
     * 내장형 메모리 DB는 따로 저장하지 않는 한 애플리케이션과 함께
     * 매번 새롭게 DB를 만들고 제거하는 생명주기를 갖는다.
     */
    @AfterEach
    void tearDown() {
        database.shutdown();
    }

    @Test
    void initData() {
        assertEquals(2, template.queryForObject("select count(*) from sqlmap", (Map<String, ?>) null, Integer.class));

        List<Map<String, Object>> list = template.queryForList("select * from sqlmap order by key_", (Map<String, ?>) null);

        assertEquals("KEY1", list.get(0).get("key_"));
        assertEquals("SQL1", list.get(0).get("sql_"));
        assertEquals("KEY2", list.get(1).get("key_"));
        assertEquals("SQL2", list.get(1).get("sql_"));
    }

    @Test
    void insert() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "KEY3");
        params.put("sql", "SQL3");

        template.update("insert into sqlmap(key_, sql_) values(:key, :sql)", params);

        assertEquals(3, template.queryForObject("select count(*) from sqlmap", (Map<String, ?>) null, Integer.class));
    }
}
