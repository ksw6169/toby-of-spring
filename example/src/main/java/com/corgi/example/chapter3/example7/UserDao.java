package com.corgi.example.chapter3.example7;

import com.corgi.example.chapter3.example7.context.JdbcContext;
import com.corgi.example.chapter3.example7.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * [Legacy 7]
 * 컨텍스트로 전달하는 콜백 오브젝트 코드도 중복되는 경우가 많다.
 * 따라서, 콜백 코드도 별도 메소드로 한다.
 */
@Repository(value = "chapter3UserDao7")
public class UserDao {

    @Autowired
    @Qualifier(value = "chapter3JdbcContext7")
    private JdbcContext jdbcContext;

    private final Logger log = LoggerFactory.getLogger(UserDao.class);

    public void deleteAll() throws SQLException {
        jdbcContext.executeSql("delete from users");
    }

    public void add(final User user) throws SQLException {
        String sql = "insert into users(id, name, password) values(?,?,?)";
        jdbcContext.executeSql(sql, user.getId(), user.getName(), user.getPassword());
    }
}
