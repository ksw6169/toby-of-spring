package com.corgi.example.chapter3.example9.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private String id;
    private String name;
    private String password;
}
