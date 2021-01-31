package com.corgi.example.service;

import com.corgi.example.domain.User;
import org.springframework.stereotype.Service;


@Service(value = "testUserService")
public class TestUserServiceImpl extends UserServiceImpl {

    private String exceptionUserId = "userId4";    // 테스트 픽스처의 users(3)의 ID 값을 고정하였음

    @Override
    protected void upgradeLevel(User user) {
        if (exceptionUserId != null && user.getId().equals(exceptionUserId)) {
            throw new TestUserServiceException();
        }

        super.upgradeLevel(user);
    }
}
