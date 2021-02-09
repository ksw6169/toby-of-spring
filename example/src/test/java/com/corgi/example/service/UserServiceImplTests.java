package com.corgi.example.service;

import com.corgi.example.dao.UserDao;
import com.corgi.example.domain.Level;
import com.corgi.example.domain.User;
import com.corgi.example.policy.StandardUserLevelUpgradePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserService testUserService;

    @Autowired
    private UserDao userDao;

    private List<User> users;

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
    void mockAdd() {
//        UserDao mockUserDao = mock(UserDao.class);
//        userService.setUserDao(mockUserDao);

        userDao.deleteAll();

        User user1 = users.get(0);
        user1.setLevel(null);

        User user2 = users.get(4);

        when(userDao.add(any(User.class))).thenReturn(1);

        testUserService.add(user1);
        testUserService.add(user2);
    }

    @Test
    void mockUpgradeLevels() {
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
//        userService.setUserDao(mockUserDao);

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        MailSender mockMailSender = mock(MailSender.class);
        doNothing().when(mockMailSender).send(mailMessageArg.capture());
//        userService.setMailSender(mockMailSender);

        testUserService.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertEquals(Level.SILVER, users.get(1).getLevel());
        verify(mockUserDao).update(users.get(3));
        assertEquals(Level.GOLD, users.get(3).getLevel());

        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        verify(mockMailSender, times(2)).send(any(SimpleMailMessage.class));
        assertEquals(users.get(1).getEmail(), mailMessages.get(0).getTo()[0]);
        assertEquals(users.get(3).getEmail(), mailMessages.get(1).getTo()[0]);
    }

    @Test
    void upgradeAllOrNothing() {
        userDao.deleteAll();

        for (User user : users) {
            userDao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    void advisorAutoProxyCreator() {
        // todo - java.reflect.Proxy != java.sun.Proxy
//        assertEquals(Proxy.class, testUserService.getClass());
    }

    @Test
    void readOnlyTransactionAttribute() {
        assertThrows(TransientDataAccessResourceException.class, () -> testUserService.getAll());
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