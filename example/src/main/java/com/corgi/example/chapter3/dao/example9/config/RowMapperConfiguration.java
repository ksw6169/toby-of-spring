package com.corgi.example.chapter3.dao.example9.config;

import com.corgi.example.chapter3.dao.example9.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class RowMapperConfiguration {

    @Bean
    public RowMapper<User> userMapper() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                return User.builder()
                        .id(rs.getString("id"))
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .build();
            }
        };
    }
}
