package com.corgi.example.config;

import com.corgi.example.factory.Message;
import com.corgi.example.factory.MessageFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ref : https://www.baeldung.com/spring-factorybean
 */
@Configuration
public class FactoryBeanConfig {

    @Bean
    public Message message() throws Exception {
        MessageFactory factory = new MessageFactory();
        factory.setText("Factory Bean");
        return factory.getObject();
    }
}
