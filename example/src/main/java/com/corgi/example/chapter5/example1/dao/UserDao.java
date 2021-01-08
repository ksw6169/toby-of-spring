package com.corgi.example.chapter5.example1.dao;

import com.corgi.example.chapter5.example1.domain.User;

import java.util.List;

public interface UserDao {

    User get(String id);

    int getCount();

    List<User> getAll();

    int add(User user);

    int update(User user);

    int deleteAll();
}
