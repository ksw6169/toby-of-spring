package com.corgi.example.chapter6.example.service;

import com.corgi.example.chapter6.example.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service("chapter6UserServiceTx")
public class UserServiceTx implements UserService {

    @Autowired
    @Qualifier(value = "chapter6UserServiceImpl")
    private UserService userService;

    @Autowired
    @Qualifier(value = "chapter6TransactionManager")
    private PlatformTransactionManager transactionManager;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public int add(User user) {
        return userService.add(user);
    }

    /**
     * UserService에서 Transaction 기능만 분리
     */
    @Override
    public void upgradeLevels() {
        TransactionStatus status = this.transactionManager
                .getTransaction(new DefaultTransactionDefinition());

        try {
            userService.upgradeLevels();
            this.transactionManager.commit(status);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
