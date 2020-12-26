package com.corgi.example.chapter3.dao.example8.template;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
