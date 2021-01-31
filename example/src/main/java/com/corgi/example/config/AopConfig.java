package com.corgi.example.config;

import com.corgi.example.aop.advice.TransactionAdvice;
import com.corgi.example.aop.pointcut.NameMatchClassMethodPointcut;
import com.corgi.example.service.UserServiceImpl;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
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

    /**
     * NameMatchMethodPointcut <- NameMatchClassMethodPointcut bean이 주입된다.
     */
    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(NameMatchMethodPointcut pointcut, TransactionAdvice advice) {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        return advisor;
    }

    /**
     * DefaultAdvisorAutoProxyCreator : 등록된 빈 중에서 Advisor 인터페이스를 구현한 것을 모두 찾는다.
     * 빈 클래스가 프록시 선정 대상이라면 프록시를 만들어 원래 빈 오브젝트와 바꿔치기한다.
     * 원래 빈 오브젝트는 프록시 뒤에 연결되서 프록시를 통해서만 접근 가능하게 바뀌는 것이다.
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public TransactionAdvice transactionAdvice(PlatformTransactionManager transactionManager) {
        return new TransactionAdvice(transactionManager);
    }

    @Bean
    public NameMatchClassMethodPointcut transactionPointcut() {
        NameMatchClassMethodPointcut pointcut = new NameMatchClassMethodPointcut();
        pointcut.setMappedClassName("*ServiceImpl");
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }
}
