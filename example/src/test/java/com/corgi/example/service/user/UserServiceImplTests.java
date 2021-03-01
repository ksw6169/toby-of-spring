package com.corgi.example.service.user;

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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private PlatformTransactionManager transactionManager;

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
        mockUserDao.deleteAll();

        User user1 = users.get(0);
        user1.setLevel(null);

        User user2 = users.get(4);

        when(mockUserDao.add(any(User.class))).thenReturn(1);

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

//    @Test
//    void transactionSync() {
//
//        // 트랜잭션 정의는 기본 값을 사용한다.
//        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
//
//        // 트랜잭션 매니저에게 트랜잭션을 요청한다. 기존에 시작된 트랜잭션이 없으니 새로운 트랜잭션을 시작시키고 트랜잭션 정보를 돌려준다.
//        // 동시에 만들어진 트랜잭션을 다른 곳에서도 사용할 수 있도록 동기화한다.
//        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);
//
//        try {
//            // 테스트 코드 상의 모든 작업을 하나의 트랜잭션으로 통합한다.
//            userDao.deleteAll();
//            userDao.add(users.get(0));
//            userDao.add(users.get(1));
//        } finally {
//            // 테스트 결과가 어떻든 상관없이 테스트가 끝나면 무조건 롤백한다. 테스트 중에 발생했던 DB의 변경 사항은 모두 이전 상태로 복구한다.
//            transactionManager.rollback(txStatus);
//        }
//    }

    @Test
    @Transactional
    void transactionSync() {
        // 테스트 코드 상의 모든 작업을 하나의 트랜잭션으로 통합한다.
        userDao.deleteAll();
        userDao.add(users.get(0));
        userDao.add(users.get(1));
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