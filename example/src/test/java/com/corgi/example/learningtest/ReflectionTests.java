package com.corgi.example.learningtest;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTests {

    @Test
    void invokeMethod() throws Exception {
        String name = "Spring";

        // length()
        Method lengthMethod = String.class.getMethod("length");
        assertEquals(6, (Integer) lengthMethod.invoke(name));

        // charAt(int index)
        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertEquals('S', (Character) charAtMethod.invoke(name, 0));
    }
}
