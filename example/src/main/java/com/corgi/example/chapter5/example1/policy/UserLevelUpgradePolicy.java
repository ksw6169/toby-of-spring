package com.corgi.example.chapter5.example1.policy;

import com.corgi.example.chapter5.example1.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
