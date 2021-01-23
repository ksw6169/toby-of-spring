package com.corgi.example.learningtest;

import com.corgi.example.domain.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FactoryBeanTests {

    @Autowired
    private Message message;

    @Test
    void getMessageFromFactoryBean() {
        assertEquals(Message.class, message.getClass());
        assertEquals("Factory Bean", message.getText());
    }
}
