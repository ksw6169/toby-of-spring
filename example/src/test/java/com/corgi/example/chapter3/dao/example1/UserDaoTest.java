package com.corgi.example.chapter3.dao.example1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName(value = "example-1")
@SpringBootTest
class UserDaoTest {

    @Autowired
    @Qualifier(value = "exampleUserDao1")
    private UserDao userDao;

    @Transactional
    @Test
    void deleteAll() throws SQLException {
        userDao.deleteAll();
    }

    @Test
    void getCount() throws SQLException {
        assertEquals(true, userDao.getCount() > 0);
    }
}