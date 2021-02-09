package com.corgi.example.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Arrays;
import java.util.Properties;

@Configuration
public class AopConfig {

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
    public Advisor txAdvisor(TransactionInterceptor txAdvice) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* *..*ServiceImpl.*(..))");
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }

    @Bean
    public TransactionInterceptor txAdvice(PlatformTransactionManager transactionManager) {
        DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
        readOnlyAttribute.setReadOnly(true);

        RuleBasedTransactionAttribute defaultAttribute = new RuleBasedTransactionAttribute();
        defaultAttribute.setRollbackRules(Arrays.asList(new RollbackRuleAttribute(Exception.class)));
        defaultAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        Properties transactionAttributes = new Properties();
        transactionAttributes.setProperty("get*", readOnlyAttribute.toString());
        transactionAttributes.setProperty("*", defaultAttribute.toString());

        TransactionInterceptor transactionAdvice = new TransactionInterceptor();
        transactionAdvice.setTransactionAttributes(transactionAttributes);
        transactionAdvice.setTransactionManager(transactionManager);

        return transactionAdvice;
    }
}
