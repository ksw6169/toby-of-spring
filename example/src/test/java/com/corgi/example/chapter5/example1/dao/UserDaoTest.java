package com.corgi.example.chapter5.example1.dao;

import com.corgi.example.chapter5.example1.domain.Level;
import com.corgi.example.chapter5.example1.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserDaoTest {

    @Autowired
    @Qualifier(value = "chapter5UserDao1")
    private UserDao userDao;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        this.user1 = new User("testId1", "testName1", "testPassword1", Level.BASIC, 1, 0);
        this.user2 = new User("testId2", "testName2", "testPassword2", Level.SILVER, 55, 10);
        this.user3 = new User("testId3", "testName3", "testPassword3", Level.GOLD, 100, 40);
    }

    @Test
    void addAndGet() {
        //given
        int expectedValue1 = 1;
        int expectedValue2 = 1;
        int expectedValue3 = 1;

        //when
        int actualValue1 = userDao.add(user1);
        int actualValue2 = userDao.add(user2);
        int actualValue3 = userDao.add(user3);

        User userGet1 = userDao.get(user1.getId());
        User userGet2 = userDao.get(user2.getId());
        User userGet3 = userDao.get(user3.getId());

        //then
        assertEquals(expectedValue1, actualValue1);
        assertEquals(expectedValue2, actualValue2);
        assertEquals(expectedValue3, actualValue3);

        checkSameUser(user1, userGet1);
        checkSameUser(user2, userGet2);
        checkSameUser(user3, userGet3);
    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getLevel(), user2.getLevel());
        assertEquals(user1.getLogin(), user2.getLogin());
        assertEquals(user1.getRecommend(), user2.getRecommend());
    }
}