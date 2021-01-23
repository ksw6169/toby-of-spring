package com.corgi.example.service;

import com.corgi.example.domain.User;

public interface UserService {
    int add(User user);
    void upgradeLevels();
}
