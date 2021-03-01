package com.corgi.example.learningtest;

import com.corgi.example.dao.UserDao;
import com.corgi.example.domain.Level;
import com.corgi.example.domain.User;
import com.corgi.example.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MockitoTests {

    @Mock
    UserDao userDao;

    @InjectMocks
    @Autowired
    UserServiceImpl userServiceImpl;

    @Test
    void mockTest() {
        User user = mock(User.class);
        assertTrue(user != null);
    }

    @Test
    void thenReturnTest() {
        User user = mock(User.class);
        when(user.getLevel()).thenReturn(Level.BASIC);
        assertEquals(user.getLevel(), Level.BASIC);
    }

    @Test
    void thenThrowTest() {
        User user = mock(User.class);
        when(user.getLevel()).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> user.getLevel());
    }

    @Test
    void doThrowTest() {
        User user = mock(User.class);
        doThrow(new IllegalArgumentException()).when(user).setName("test");
        assertThrows(IllegalArgumentException.class, () -> user.setName("test"));
    }

    @Test
    void doNothingTest() {
        User user = mock(User.class);
        doNothing().when(user).setName(anyString());

        user.setName("test");
        verify(user).setName(anyString());
    }

    @Test
    void verifyTest() {
        User user = mock(User.class);
        user.setLevel(Level.BASIC);

        // n번 호출했는지 체크
        verify(user, times(1)).setLevel(any(Level.class));

        // 호출 안했는지 체크
        verify(user, never()).getLevel();
        verify(user, never()).setLevel(eq(Level.GOLD));

        user.setLevel(Level.GOLD);

        // 1번 이상 호출했는지 체크
        verify(user, atLeastOnce()).setLevel(any(Level.class));

        // 2번 이상 호출했는지 체크
        verify(user, atLeast(2)).setLevel(any(Level.class));

        // 1번 이하 호출했는지 체크
        verify(user, atMostOnce()).setEmail(anyString());

        // 2번 이하 호출했는지 체크
        verify(user, atMost(2)).setLevel(any(Level.class));

        // 지정된 시간(ms) 안에 메소드를 호출했는지 체크
        user.setName(anyString());
        verify(user, timeout(100)).setName(anyString());

        // 지정된 시간(ms) 안에 메소드를 1번 이상 호출했는지 체크
        verify(user, timeout(100).atLeast(1)).setLevel(any(Level.class));
    }

    @Test
    void injectMocksTest() {
        MockitoAnnotations.initMocks(this);
        when(userDao.add(any(User.class))).thenReturn(1);

        User user = mock(User.class);
        userServiceImpl.add(user);
    }
}
