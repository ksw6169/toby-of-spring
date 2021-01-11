package com.corgi.example.chapter5.example1.service;

import com.corgi.example.chapter5.example1.dao.UserDao;
import com.corgi.example.chapter5.example1.domain.Level;
import com.corgi.example.chapter5.example1.domain.User;
import com.corgi.example.chapter5.example1.policy.StandardUserLevelUpgradePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
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

    @Autowired
    @Qualifier(value = "chapter5TransactionManager")
    private PlatformTransactionManager transactionManager;

    @Autowired
    @Qualifier(value = "chapter5DummyMailSender")
    private MailSender mailSender;

    private List<User> users;

    static class TestUserService extends UserService {

        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
            new User("userId1", "userName1", "userPassword1", Level.BASIC, StandardUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER-1, 0, null, "testEmail1@naver.com"),
            new User("userId2", "userName2", "userPassword2", Level.BASIC, StandardUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER, 0, null, "testEmail2@naver.com"),
            new User("userId3", "userName3", "userPassword3", Level.SILVER, 60, StandardUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD-1, null, "testEmail3@naver.com"),
            new User("userId4", "userName4", "userPassword4", Level.SILVER, 60, StandardUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD, null, "testEmail4@naver.com"),
            new User("userId5", "userName5", "userPassword5", Level.GOLD, 49, Integer.MAX_VALUE, null, "testEmail5@naver.com")
        );
    }

    @Test
    void bean() {
        assertNotNull(this.userService);
    }

    @Test
    void upgradeLevels() throws Exception {
        userDao.deleteAll();

        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        this.checkLevelUpgraded(users.get(0), false);
        this.checkLevelUpgraded(users.get(1), true);
        this.checkLevelUpgraded(users.get(2), false);
        this.checkLevelUpgraded(users.get(3), true);
        this.checkLevelUpgraded(users.get(4), false);
    }

    @Test
    void add() {
        userDao.deleteAll();

        User user1 = users.get(0);
        user1.setLevel(null);

        User user2 = users.get(4);

        userService.add(user1);
        userService.add(user2);
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        // 예외를 발생시킬 4번째 사용자의 ID를 넣음
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setTransactionManager(transactionManager);
        testUserService.setMailSender(mailSender);

        userDao.deleteAll();

        for (User user : users) {
            userDao.add(user);
        }

        try {
//            testUserService.upgradeLevelsByTransactionManager();
            testUserService.upgradeLevelsByTransactionManager();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) { }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User upgradedUser = userDao.get(user.getId());

        if (upgraded) {
            assertEquals(user.getLevel().nextLevel(), upgradedUser.getLevel());
        } else {
            assertEquals(user.getLevel(), upgradedUser.getLevel());
        }
    }
}