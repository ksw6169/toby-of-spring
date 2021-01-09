package com.corgi.example.chapter5.example1.service;

import com.corgi.example.chapter5.example1.dao.UserDao;
import com.corgi.example.chapter5.example1.domain.Level;
import com.corgi.example.chapter5.example1.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Service
public class UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    @Autowired
    @Qualifier(value = "chapter5UserDao1")
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier(value = "chapter5TransactionManager")
    private PlatformTransactionManager transactionManager;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels() throws Exception {

        // 트랜잭션 동기화 관리자를 이용해 동기화 작업 초기화
        TransactionSynchronizationManager.initSynchronization();

        // DB 커넥션 생성 & 트랜잭션 시작(트랜잭션 저장소에 생성한 Connection 오브젝트를 저장하여 동기화함)
        // 이후 작업에서는 동기화된 Connection을 사용하게 됨
        Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);

        try {
            // 트랜잭션을 적용할 작업들
            List<User> users = userDao.getAll();

            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }

            // 트랜잭션 커밋(트랜잭션 종료)
            c.commit();
        } catch (Exception e) {
            // 트랜잭션 롤백(트랜잭션 종료)
            c.rollback();
            throw e;
        } finally {
            // DB Connection close
            DataSourceUtils.releaseConnection(c, dataSource);

            // 동기화 작업 종료 및 정리
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    /**
     * Spring에서 제공하는 트랜잭션 추상화 방법 적용
     */
    public void upgradeLevelsByTransactionManager() {

        // JDBC 트랜잭션 추상화 오브젝트 생성
//        PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);

        // 트랜잭션 시작, 트랜잭션은 TransactionStatus 타입의 변수에 저장된다.
        // 트랜잭션에 대한 조작이 필요하다면 PlatformTransactionManager 메소드의 파라미터로 이 객체를 전달해주면 된다.
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }

            transactionManager.commit(status);   // 트랜잭션 커밋
        } catch (RuntimeException e) {
            transactionManager.rollback(status); // 트랜잭션 롤백
            throw e;
        }
    }

    public int add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        return userDao.add(user);
    }

    protected boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();

        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }
}
