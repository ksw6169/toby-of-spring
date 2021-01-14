package com.corgi.example.chapter6.example.service;

import com.corgi.example.chapter6.example.dao.UserDao;
import com.corgi.example.chapter6.example.domain.Level;
import com.corgi.example.chapter6.example.domain.User;
import com.corgi.example.chapter6.example.policy.StandardUserLevelUpgradePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    @Qualifier(value = "chapter6UserServiceImpl")
    private UserServiceImpl userService;

    @Autowired
    @Qualifier(value = "chapter6TransactionManager")
    private PlatformTransactionManager transactionManager;

    private List<User> users;

    static class TestUserService extends UserServiceImpl {

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

    static class MockMailSender implements MailSender {
        private List<String> requests = new ArrayList<>();

        @Override
        public void send(SimpleMailMessage simpleMailMessage) throws MailException {
            requests.add(simpleMailMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMailMessages) throws MailException {
        }

        public List<String> getRequests() {
            return requests;
        }
    }

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getAll() {
            return users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public int update(User user) {
            updated.add(user);
            return 1;
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int deleteAll() {
            throw new UnsupportedOperationException();
        }
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
    void add() {
        UserDao userDao = new MockUserDao(users);
        userDao.deleteAll();

        User user1 = users.get(0);
        user1.setLevel(null);

        User user2 = users.get(4);

        userService.add(user1);
        userService.add(user2);
    }

    @Test
    void upgradeLevels() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mailSender = new MockMailSender();
        userServiceImpl.setMailSender(mailSender);

        userServiceImpl.upgradeLevels();

        List<User> updatedUsers = mockUserDao.getUpdated();
        assertEquals(2, updatedUsers.size());
        checkUserAndLevel(updatedUsers.get(0), "userId2", Level.SILVER);
        checkUserAndLevel(updatedUsers.get(1), "userId4", Level.GOLD);

        List<String> requests = mailSender.getRequests();
        assertEquals(2, requests.size());
        assertEquals(users.get(1).getEmail(), requests.get(0));
        assertEquals(users.get(3).getEmail(), requests.get(1));
    }

//    @Test
//    void upgradeAllOrNothing() throws Exception {
//        TestUserService testUserService = new TestUserService(users.get(3).getId());
//        testUserService.setUserDao(userDao);
//
//        MockMailSender mailSender = new MockMailSender();
//        testUserService.setMailSender(mailSender);
//
//        UserServiceTx userServiceTx = new UserServiceTx();
//        userServiceTx.setTransactionManager(transactionManager);
//        userServiceTx.setUserService(testUserService);
//
//        userDao.deleteAll();
//
//        for (User user : users) userDao.add(user);
//
//        try {
//            testUserService.upgradeLevels();
//            fail("TestUserServiceException expected");
//        } catch (TestUserServiceException e) {
//        }
//
//        checkLevelUpgraded(users.get(1), false);
//    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertEquals(expectedId, updated.getId());
        assertEquals(expectedLevel, updated.getLevel());
    }

//    private void checkLevelUpgraded(User user, boolean upgraded) {
//        User upgradedUser = userDao.get(user.getId());
//
//        if (upgraded) {
//            assertEquals(user.getLevel().nextLevel(), upgradedUser.getLevel());
//        } else {
//            assertEquals(user.getLevel(), upgradedUser.getLevel());
//        }
//    }
}