package com.corgi.example.chapter4.example1.dao;

import com.corgi.example.chapter4.example1.entity.User;

import java.util.List;

public interface UserDao {

    int add(User user);

    User get(String id);

    List<User> getAll();

    int deleteAll();

    int getCount();
}
