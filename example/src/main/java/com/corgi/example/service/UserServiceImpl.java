package com.corgi.example.service;

import com.corgi.example.dao.UserDao;
import com.corgi.example.domain.Level;
import com.corgi.example.domain.User;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
@AllArgsConstructor
@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private UserDao userDao;
    private MailSender mailSender;

    @Override
    public void upgradeLevels() {
        List<User> users = this.userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    @Override
    public int add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        return this.userDao.add(user);
    }

    protected boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();

        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        this.userDao.update(user);
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom("ksw6169@naver.com");
        message.setSubject("Upgrade 안내");
        message.setText("사용자님의 등급이 " + user.getLevel() + "로 업그레이드 되었습니다.");

        this.mailSender.send(message);
    }
}
