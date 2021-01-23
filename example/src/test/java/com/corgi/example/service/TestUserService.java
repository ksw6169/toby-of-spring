package com.corgi.example.service;

import com.corgi.example.domain.User;

public class TestUserService extends UserServiceImpl {

    private String exceptionUserId;

    public TestUserService(String exceptionUserId) {
        this.exceptionUserId = exceptionUserId;
    }

    @Override
    protected void upgradeLevel(User user) {
        if (exceptionUserId != null && user.getId().equals(exceptionUserId)) {
            throw new TestUserServiceException();
        }

        super.upgradeLevel(user);
    }
}
