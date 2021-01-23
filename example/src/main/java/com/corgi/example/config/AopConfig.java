package com.corgi.example.config;

import com.corgi.example.aop.advice.TransactionAdvice;
import com.corgi.example.service.UserServiceImpl;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AopConfig {

    @Bean
    public ProxyFactoryBean proxyFactoryBean(UserServiceImpl userService, DefaultPointcutAdvisor advisor) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(userService);
        pfBean.addAdvisor(advisor);
        pfBean.setInterceptorNames("transactionAdvisor");
        return pfBean;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(NameMatchMethodPointcut pointcut, TransactionAdvice advice) {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        return advisor;
    }

    @Bean
    public TransactionAdvice transactionAdvice(PlatformTransactionManager transactionManager) {
        return new TransactionAdvice(transactionManager);
    }

    @Bean
    public NameMatchMethodPointcut transactionPointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }
}
