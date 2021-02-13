package com.corgi.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public Map<String, String> sqlMap() {
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("userAdd", "insert into users(id, name, password, level, login, recommend, email) values(?,?,?,?,?,?,?)");
        sqlMap.put("userGet", "select * from users where id = ?");
        sqlMap.put("userGetAll", "select * from users");
        sqlMap.put("userGetCount", "select count(*) from users");
        sqlMap.put("userDeleteAll", "delete from users");
        sqlMap.put("userUpdate", "update users set name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? where id = ?");

        return sqlMap;
    }
}