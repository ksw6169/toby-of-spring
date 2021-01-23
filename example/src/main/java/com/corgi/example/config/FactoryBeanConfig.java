package com.corgi.example.config;

import com.corgi.example.domain.Message;
import com.corgi.example.factory.MessageFactory;
import com.corgi.example.factory.TxProxyFactory;
import com.corgi.example.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

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

    @Bean
    public TxProxyFactory txProxyFactory(UserService userService, PlatformTransactionManager transactionManager) throws Exception {
        TxProxyFactory factory = new TxProxyFactory();
        factory.setTarget(userService);
        factory.setTransactionManager(transactionManager);
        factory.setPattern("upgradeLevels");
        factory.setServiceInterface(UserService.class);
        return factory;
    }
}
