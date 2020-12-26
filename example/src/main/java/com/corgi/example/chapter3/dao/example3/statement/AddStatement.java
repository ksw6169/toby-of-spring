package com.corgi.example.chapter3.dao.example3.statement;

import com.corgi.example.chapter3.dao.example3.entity.User;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@AllArgsConstructor
public class AddStatement implements StatementStrategy {

    private User user;

    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        return ps;
    }
}
