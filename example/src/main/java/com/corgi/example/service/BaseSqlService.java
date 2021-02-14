package com.corgi.example.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseSqlService {
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;
}
