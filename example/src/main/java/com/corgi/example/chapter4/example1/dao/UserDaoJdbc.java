package com.corgi.example.chapter4.example1.dao;


import com.corgi.example.chapter4.example1.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository(value = "chapter4UserDao1")
public class UserDaoJdbc implements UserDao {

    @Autowired
    @Qualifier(value = "chapter4JdbcTemplate1")
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            return User.builder()
                    .id(rs.getString("id"))
                    .name(rs.getString("name"))
                    .password(rs.getString("password"))
                    .build();
        }
    };

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("select * from users", userMapper);
    }

    @Override
    public int add(User user) {
        return jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{ id }, userMapper);
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("delete from users");
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }
}
