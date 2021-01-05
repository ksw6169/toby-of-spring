package com.corgi.example.chapter4.example1.dao;

import com.corgi.example.chapter4.example1.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "chapter4-example1")
@Transactional
@SpringBootTest
class UserDaoTest {

    @Autowired
    @Qualifier(value = "chapter4UserDao1")
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    @Test
    void add() {
        User user = User.builder()
                .id("testId1")
                .name("testName1")
                .password("testPassword1")
                .build();

        assertEquals(1, userDao.add(user));
    }

    @Test
    void get() {
        User user = userDao.get("testid");
        assertEquals("testid", user.getId());
        assertEquals("testname", user.getName());
        assertEquals("testpassword", user.getPassword());
    }

    @Test
    void getAll() {
        List<User> users = userDao.getAll();
        assertEquals(1, users.size());
    }

    @Test
    void deleteAll() {
        assertEquals(1, userDao.deleteAll());
    }

    @Test
    void getCount() {
        assertEquals(1, userDao.getCount());
    }

    @Test
    public void duplicateKey() {
        userDao.deleteAll();

        User user = User.builder()
                .id("testId2")
                .name("testName2")
                .password("testPassword2")
                .build();

        assertEquals(1, userDao.add(user));
        assertThrows(DuplicateKeyException.class, () -> userDao.add(user));
    }
}