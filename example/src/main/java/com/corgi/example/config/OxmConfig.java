package com.corgi.example.config;

import com.corgi.example.service.ConcurrentHashMapSqlRegistry;
import com.corgi.example.service.SqlRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class OxmConfig {
    @Bean
    public Jaxb2Marshaller unmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setContextPath("com.corgi.example.xml");
        return unmarshaller;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
