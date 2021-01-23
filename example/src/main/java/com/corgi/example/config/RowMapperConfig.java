package com.corgi.example.config;

import com.corgi.example.domain.Level;
import com.corgi.example.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class RowMapperConfig {

    @Bean
    public RowMapper<User> userMapper() {
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
