package com.corgi.example.chapter6.example.config;

import com.corgi.example.chapter6.example.domain.Level;
import com.corgi.example.chapter6.example.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class Chapter6RowMapperConfig {

    @Bean(name = "chapter6UserRowMapper")
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
                        .email(rs.getString("email"))
                        .build();
            }
        };
    }
}
