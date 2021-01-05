package com.corgi.example.chapter3.example5;

import com.corgi.example.chapter3.example5.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@DisplayName(value = "chapter3-example5")
@Transactional
@SpringBootTest
class UserDaoTest {

    @Autowired
    @Qualifier(value = "chapter3UserDao5")
    private UserDao userDao;

    @Test
    void deleteAll() throws SQLException {
        userDao.deleteAll();
    }

    @Test
    void add() throws SQLException {
        User user = User.builder()
                .id("testIdentity")
                .name("testName")
                .password("testPassword")
                .build();

        userDao.add(user);
    }
}