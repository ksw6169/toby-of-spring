package com.corgi.example.factory;

import lombok.Getter;

@Getter
public class Message {

    private String text;

    private Message(String text) {
        this.text = text;
    }

    public static Message newMessage(String text) {
        return new Message(text);
    }
}
