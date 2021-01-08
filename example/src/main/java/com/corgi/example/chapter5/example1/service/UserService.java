package com.corgi.example.chapter5.example1.service;

import com.corgi.example.chapter5.example1.dao.UserDao;
import com.corgi.example.chapter5.example1.domain.Level;
import com.corgi.example.chapter5.example1.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    @Qualifier(value = "chapter5UserDao1")
    private UserDao userDao;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();

        for (User user : users) {
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                userDao.update(user);
            } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                userDao.update(user);
            }
        }
    }
}
