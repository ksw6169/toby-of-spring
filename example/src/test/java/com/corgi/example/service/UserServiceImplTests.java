package com.corgi.example.service;

import com.corgi.example.dao.UserDao;
import com.corgi.example.domain.Level;
import com.corgi.example.domain.User;
import com.corgi.example.factory.TxProxyFactory;
import com.corgi.example.policy.StandardUserLevelUpgradePolicy;
import com.corgi.example.proxy.handler.TransactionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class UserServiceImplTests {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private TransactionHandler txHandler;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TxProxyFactory txProxyFactory;

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
        UserDao mockUserDao = mock(UserDao.class);
        userServiceImpl.setUserDao(mockUserDao);

        mockUserDao.deleteAll();

        User user1 = users.get(0);
        user1.setLevel(null);

        User user2 = users.get(4);

        when(mockUserDao.add(any(User.class))).thenReturn(1);

        assertEquals(1, userServiceImpl.add(user1));
        assertEquals(1, userServiceImpl.add(user2));
    }

    @Test
    void mockUpgradeLevels() {
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        MailSender mockMailSender = mock(MailSender.class);
        doNothing().when(mockMailSender).send(mailMessageArg.capture());
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

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
    @DirtiesContext
    void upgradeAllOrNothing() throws Exception {

        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mock(MailSender.class));

        txProxyFactory.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactory.getObject();

        userDao.deleteAll();

        for (User user : users) {
            userDao.add(user);
        }

        txHandler.setPattern("upgradeLevels");

        try {
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

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