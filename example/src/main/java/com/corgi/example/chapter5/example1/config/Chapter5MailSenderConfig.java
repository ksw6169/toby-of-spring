package com.corgi.example.chapter5.example1.config;

import com.corgi.example.chapter5.example1.service.DummyMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class Chapter5MailSenderConfig {

    @Bean(name = "chapter5MailSender1")
    public MailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.server.com");
        return mailSender;
    }

    @Bean(name = "chapter5DummyMailSender")
    public MailSender dummyMailSender() {
        DummyMailSender mailSender = new DummyMailSender();
        return mailSender;
    }
}
