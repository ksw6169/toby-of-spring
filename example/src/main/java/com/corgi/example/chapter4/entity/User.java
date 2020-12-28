package com.corgi.example.chapter4.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {
    private String id;
    private String name;
    private String password;
}
