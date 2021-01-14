package com.corgi.example.chapter6.example.policy;

import com.corgi.example.chapter6.example.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
