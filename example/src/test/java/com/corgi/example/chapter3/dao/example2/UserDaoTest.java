package com.corgi.example.chapter3.dao.example2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@DisplayName(value = "example-2")
@SpringBootTest
class UserDaoTest {

    @Autowired
    @Qualifier(value = "exampleUserDao2")
    private UserDao userDao;

    @Transactional
    @Test
    void deleteAll() throws SQLException {
        userDao.deleteAll();
    }
}