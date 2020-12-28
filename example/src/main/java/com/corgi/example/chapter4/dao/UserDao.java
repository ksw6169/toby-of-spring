package com.corgi.example.chapter4.dao;


import com.corgi.example.chapter4.entity.User;
import com.corgi.example.chapter4.exception.QuerySyntaxException;
import com.mysql.cj.core.exceptions.MysqlErrorNumbers;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository(value = "chapter4UserDao1")
public class UserDao {

    private final DataSource dataSource;
    private final Logger log = LoggerFactory.getLogger(UserDao.class);

    public List<User> getAll() throws QuerySyntaxException, SQLException {
        List<User> users = new ArrayList<>();

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("selects * from users");     // 예외 발생을 위해 SQL문 고의로 틀리게 작성
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(User.builder()
                        .id(rs.getString("id"))
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .build());
            }
        } catch (SQLException e) {
            log.error(e.getMessage());

            if (e.getErrorCode() == MysqlErrorNumbers.ER_PARSE_ERROR) {     // 예외 전환
                throw new QuerySyntaxException(e);
            } else {
                throw e;
            }
        }

        return users;
    }
}
