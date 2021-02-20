package com.corgi.example.service;

import com.corgi.example.exception.SqlNotFoundException;
import com.corgi.example.exception.SqlUpdateFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ConcurrentHashMapSqlRegistryTests {

    @Autowired
    private UpdatableSqlRegistry sqlRegistry;

    @BeforeEach
    void setUp() {
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    @Test
    void find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    @Test
    void unknownKey() {
        assertThrows(SqlNotFoundException.class, () -> sqlRegistry.findSql("SQL9999!@#$"));
    }

    @Test
    void updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFindResult("SQL1", "Modified2", "SQL3");
    }

    @Test
    void updateMulti() {
        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY3", "Modified3");

        sqlRegistry.updateSql(sqlmap);
        checkFindResult("Modified1", "SQL2", "Modified3");
    }

    @Test
    void updateWithNotExistingKey() {
        assertThrows(SqlUpdateFailureException.class, () -> sqlRegistry.updateSql("SQL9999!@#$", "Modified2"));
    }

    private void checkFindResult(String ... expectedValues) {
        assertEquals(expectedValues[0], sqlRegistry.findSql("KEY1"));
        assertEquals(expectedValues[1], sqlRegistry.findSql("KEY2"));
        assertEquals(expectedValues[2], sqlRegistry.findSql("KEY3"));
    }
}
