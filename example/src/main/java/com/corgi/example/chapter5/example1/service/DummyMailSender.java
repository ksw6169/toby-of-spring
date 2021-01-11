package com.corgi.example.chapter5.example1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.lang.reflect.Field;

@Slf4j
public class DummyMailSender implements MailSender {

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        try {
            log.debug(toMessage(simpleMailMessage));
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void send(SimpleMailMessage... simpleMailMessages) throws MailException {
        try {
            log.debug(toMessage(simpleMailMessages));
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * todo - 기능 테스트 필요
     */
    private String toMessage(SimpleMailMessage... simpleMailMessages) throws IllegalAccessException {
        StringBuilder builder = new StringBuilder();

        if (simpleMailMessages != null && simpleMailMessages.length > 0) {
            Field[] fields = getFields(simpleMailMessages.getClass());

            for (SimpleMailMessage message : simpleMailMessages) {
                for (Field field : fields) {
                    builder.append(field.getName());
                    builder.append(" : ");
                    builder.append(field.get(message));
                    builder.append("\n");
                }
            }
        }

        return builder.toString();
    }

    private Field[] getFields(Class<?> clazz) {
        return clazz.getFields();
    }
}
