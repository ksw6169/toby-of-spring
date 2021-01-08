package com.corgi.example.chapter5.example1.dao;

import com.corgi.example.chapter5.example1.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "chapter5UserDao1")
public class UserDaoJdbc implements UserDao {

    @Autowired
    @Qualifier(value = "chapter5JdbcTemplate1")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier(value = "chapter5UserRowMapper1")
    private RowMapper<User> rowMapper;

    private static final Logger log = LoggerFactory.getLogger(UserDaoJdbc.class);

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{ id }, rowMapper);
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("select * from users", rowMapper);
    }

    @Override
    public int add(User user) {
        return jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) values(?,?,?,?,?,?)",
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    @Override
    public int update(User user) {
        return jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ? ",
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("delete from users");
    }
}
