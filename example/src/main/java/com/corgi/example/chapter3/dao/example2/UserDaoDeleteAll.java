package com.corgi.example.chapter3.dao.example2;


import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository(value = "exampleUserDao2")
public class UserDaoDeleteAll extends UserDao {

    public UserDaoDeleteAll(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("delete from users");
    }
}
