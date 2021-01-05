package com.corgi.example.chapter5.example1.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;
}
