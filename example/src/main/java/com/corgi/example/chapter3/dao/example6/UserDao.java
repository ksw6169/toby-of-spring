package com.corgi.example.chapter3.dao.example6;

import com.corgi.example.chapter3.dao.example6.context.JdbcContext;
import com.corgi.example.chapter3.dao.example6.entity.User;
import com.corgi.example.chapter3.dao.example6.statement.StatementStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * [Legacy 6]
 * 다른 Dao에서 사용할 수 있도록 {@link com.corgi.example.chapter3.dao.example5.UserDao#jdbcContextWithStatementStrategy(com.corgi.example.chapter3.dao.example5.statement.StatementStrategy)} 기능을
 * 별도 컨텍스트 클래스 {@link com.corgi.example.chapter3.dao.example6.context.JdbcContext} 로 분리
 */
@Repository(value = "exampleUserDao6")
public class UserDao {

    @Autowired
    @Qualifier(value = "jdbcContext6")
    private JdbcContext jdbcContext;

    private final Logger log = LoggerFactory.getLogger(UserDao.class);

    public void deleteAll() throws SQLException {
        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement("delete from users");
            }
        });
    }

    public void add(final User user) throws SQLException {
        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
    }
}
