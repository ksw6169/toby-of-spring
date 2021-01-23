package com.corgi.example.factory;

import com.corgi.example.domain.Message;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

public class MessageFactory implements FactoryBean<Message> {

    @Setter
    private String text;

    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(this.text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
