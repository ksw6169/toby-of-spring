package com.corgi.example.proxy.handler;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@Setter
public class TransactionHandler implements InvocationHandler {

    @Autowired
    @Qualifier(value = "userServiceImpl")
    private Object target;  // 부가 기능을 제공할 타깃 오브젝트. 어떤 타입의 오브젝트에도 적용할 수 있다.

    @Autowired
    private PlatformTransactionManager transactionManager;  // 트랜잭션 기능을 제공할 때 필요한 트랜잭션 매니저

    private String pattern;     // 트랜잭션을 적용할 메소드 이름 패턴

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().startsWith(pattern)) {
            return invokeInTransaction(method, args);
        } else {
            return method.invoke(target, args);
        }
    }

    private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            Object ret = method.invoke(target, args);
            this.transactionManager.commit(status);
            return ret;
        } catch (InvocationTargetException e) {
            this.transactionManager.rollback(status);
            throw e.getTargetException();
        }
    }
}
