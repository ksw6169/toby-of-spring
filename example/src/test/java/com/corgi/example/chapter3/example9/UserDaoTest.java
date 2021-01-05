package com.corgi.example.chapter3.example9;

import com.corgi.example.chapter3.example9.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "chapter3-example9")
@SpringBootTest
@Transactional
class UserDaoTest {

    @Autowired
    @Qualifier(value = "chapter3UserDao9")
    private UserDao userDao;

    @Test
    void deleteAll() {
        assertEquals(1, userDao.deleteAll());
    }

    @Test
    void add() {
        User user = User.builder()
                .id("testUserId")
                .name("testUserName")
                .password("testUserPassword")
                .build();

        assertEquals(1, userDao.add(user));
    }

    @Test
    void getCount() {
        assertEquals(1, userDao.getCount());
    }

    @Test
    void get() {
        User user = userDao.get("testid");
        assertEquals("testid", user.getId());
        assertEquals("testname", user.getName());
        assertEquals("testpassword", user.getPassword());
    }

    @Test
    void getFailure() {
        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> userDao.get("anonymous"));
    }

    @Test
    void getAll() {
        userDao.deleteAll();

        List<User> users = userDao.getAll();
        assertEquals(0, users.size());

        User user1 = User.builder()
                .id("userId1")
                .name("userName1")
                .password("userPassword1")
                .build();

        userDao.add(user1);
        users = userDao.getAll();

        assertEquals(1, users.size());
        checkSameUser(user1, users.get(0));

        User user2 = User.builder()
                .id("userId2")
                .name("userName2")
                .password("userPassword2")
                .build();

        userDao.add(user2);
        users = userDao.getAll();

        assertEquals(2, users.size());
        checkSameUser(user2, users.get(1));

        User user3 = User.builder()
                .id("userId3")
                .name("userName3")
                .password("userPassword3")
                .build();

        userDao.add(user3);
        users = userDao.getAll();

        assertEquals(3, users.size());
        checkSameUser(user3, users.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
    }
}