package com.corgi.example.chapter5.example1.service;

import com.corgi.example.chapter5.example1.dao.UserDao;
import com.corgi.example.chapter5.example1.domain.Level;
import com.corgi.example.chapter5.example1.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier(value = "chapter5UserDao1")
    private UserDao userDao;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
            new User("userId1", "userName1", "userPassword1", Level.BASIC, 49, 0),
            new User("userId2", "userName2", "userPassword2", Level.BASIC, 50, 0),
            new User("userId3", "userName3", "userPassword3", Level.SILVER, 60, 29),
            new User("userId4", "userName4", "userPassword4", Level.SILVER, 60, 30),
            new User("userId5", "userName5", "userPassword5", Level.GOLD, 49, 100)
        );
    }

    @Test
    void bean() {
        assertNotNull(this.userService);
    }

    @Test
    void upgradeLevels() {
        userDao.deleteAll();

        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        this.checkLevel(users.get(0), Level.BASIC);
        this.checkLevel(users.get(1), Level.SILVER);
        this.checkLevel(users.get(2), Level.SILVER);
        this.checkLevel(users.get(3), Level.GOLD);
        this.checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User updatedUser = userDao.get(user.getId());
        assertEquals(expectedLevel, updatedUser.getLevel());
    }
}