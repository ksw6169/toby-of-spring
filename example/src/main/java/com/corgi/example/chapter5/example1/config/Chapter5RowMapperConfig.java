package com.corgi.example.chapter5.example1.config;

import com.corgi.example.chapter5.example1.domain.Level;
import com.corgi.example.chapter5.example1.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class Chapter5RowMapperConfig {

    @Bean(name = "chapter5UserRowMapper1")
    public RowMapper<User> userRowMapper() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                return User.builder()
                        .id(rs.getString("id"))
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .level(Level.valueOf(rs.getInt("level")))
                        .login(rs.getInt("login"))
                        .recommend(rs.getInt("recommend"))
                        .build();
            }
        };
    }
}
