package com.corgi.example.service.sql;

import com.corgi.example.service.sql.OxmSqlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OxmSqlServiceTests {

    @Autowired
    private OxmSqlService oxmSqlService;

    private Map<String, String> sqlMap = new HashMap<>();

    @BeforeEach
    void setUp() {
        this.sqlMap.put("userAdd", "insert into users(id, name, password, level, login, recommend, email) values(?,?,?,?,?,?,?)");
        this.sqlMap.put("userGet", "select * from users where id = ?");
        this.sqlMap.put("userGetAll", "select * from users");
        this.sqlMap.put("userGetCount", "select count(*) from users");
        this.sqlMap.put("userDeleteAll", "delete from users");
        this.sqlMap.put("userUpdate", "update users set name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? where id = ?");
    }

    @Test
    void getSql() {
        for (String key : sqlMap.keySet()) {
            checkSql(key, sqlMap.get(key));
        }
    }

    private void checkSql(String key, String expectedSql) {
        assertEquals(expectedSql, oxmSqlService.getSql(key));
    }
}