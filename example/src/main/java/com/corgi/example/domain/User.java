package com.corgi.example.domain;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;
    private Date lastUpgraded;
    private String email;

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();

        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        } else {
            this.level = nextLevel;
            this.lastUpgraded = new Date();
        }
    }
}
