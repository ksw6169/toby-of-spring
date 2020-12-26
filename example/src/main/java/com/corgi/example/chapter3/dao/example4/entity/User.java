package com.corgi.example.chapter3.dao.example4.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {
    private String id;
    private String name;
    private String password;
}
