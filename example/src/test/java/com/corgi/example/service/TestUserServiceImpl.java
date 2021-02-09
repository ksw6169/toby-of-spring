package com.corgi.example.service;

import com.corgi.example.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;


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

    /**
     * 읽기 전용 트랜잭션의 대상인 get으로 시작하는 메소드에서
     * 쓰기 작업 발생 시 예외 발생하는지 확인을 위해 테스트로 만든 getAll() 메소드
     */
    @Override
    public List<User> getAll() {
        for (User user : super.getAll()) {
            super.update(user);
        }

        return null;
    }
}
