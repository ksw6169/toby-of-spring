package com.corgi.example.chapter4.dao;

import com.corgi.example.chapter4.exception.QuerySyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "[4장] 예제-1")
@SpringBootTest
class UserDaoTest {

    @Autowired
    @Qualifier(value = "chapter4UserDao1")
    private UserDao userDao;

    @Test
    void getAll() {
        assertThrows(QuerySyntaxException.class, () -> userDao.getAll());
    }
}