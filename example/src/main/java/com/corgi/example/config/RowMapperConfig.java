package com.corgi.example.config;

import com.corgi.example.domain.Level;
import com.corgi.example.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;


@Configuration
public class RowMapperConfig {

    @Bean
    public RowMapper<User> userMapper() {
        return (rs, i) -> User.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .password(rs.getString("password"))
                .level(Level.valueOf(rs.getInt("level")))
                .login(rs.getInt("login"))
                .recommend(rs.getInt("recommend"))
                .email(rs.getString("email"))
                .build();
    }
}
