package com.corgi.example.dao;

import com.corgi.example.domain.User;

import java.util.List;

public interface UserDao {

    User get(String id);

    int getCount();

    List<User> getAll();

    int add(User user);

    int update(User user);

    int deleteAll();
}
