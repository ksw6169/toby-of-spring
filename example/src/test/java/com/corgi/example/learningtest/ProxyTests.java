package com.corgi.example.learningtest;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 다이내믹 프록시를 이용한 프록시 클래스 학습 테스트(439p)
 */
public class ProxyTests {

    interface Hello {
        String sayHello(String name);
        String sayHi(String name);
        String sayThankYou(String name);
    }

    /**
     * 타깃 클래스
     */
    public class HelloTarget implements Hello {
        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankYou(String name) {
            return "Thank You " + name;
        }
    }

    /**
     * 프록시 클래스
     */
    @AllArgsConstructor
    public class HelloUppercase implements Hello {
        private Hello hello;    // 위임할 타깃 오브젝트. 다른 프록시를 중간에 추가할 수도 있으므로 인터페이스로 접근한다.

        @Override
        public String sayHello(String name) {
            return hello.sayHello(name).toUpperCase();
        }

        @Override
        public String sayHi(String name) {
            return hello.sayHi(name).toUpperCase();
        }

        @Override
        public String sayThankYou(String name) {
            return hello.sayThankYou(name).toUpperCase();
        }
    }

    public class UppercaseHandler implements InvocationHandler {
        Object target;   // 어떤 종류의 인터페이스를 구현한 타깃에도 적용 가능하도록 Object 타입으로 수정

        public UppercaseHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object ret = method.invoke(target, args);   // 타깃으로 위임. 인터페이스의 모든 메소드 호출에 적용된다.

            if (ret instanceof String && method.getName().startsWith("say")) {    // 리턴 타입과 메소드 이름이 일치하는 경우에만 부가 기능 적용
                return ((String) ret).toUpperCase();
            } else {
                return ret;
            }
        }
    }

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        assertEquals("Hello Toby", hello.sayHello("Toby"));
        assertEquals("Hi Toby", hello.sayHi("Toby"));
        assertEquals("Thank You Toby", hello.sayThankYou("Toby"));
    }

    @Test
    void uppercaseProxy() {
        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertEquals("HELLO TOBY", proxiedHello.sayHello("Toby"));
        assertEquals("HI TOBY", proxiedHello.sayHi("Toby"));
        assertEquals("THANK YOU TOBY", proxiedHello.sayThankYou("Toby"));
    }

    @Test
    void dynamicProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),                // 동적으로 사용되는 다이내믹 프록시 클래스의 로딩에 사용할 클래스 로더(=다이내믹 프록시가 정의되는 클래스 로더 지정)
                new Class[] { Hello.class },                // 구현할 인터페이스(여러 개 지정 가능)
                new UppercaseHandler(new HelloTarget()));   // 부가 기능과 위임 코드를 담은 InvocationHandler

        assertEquals("HELLO TOBY", proxiedHello.sayHello("Toby"));
        assertEquals("HI TOBY", proxiedHello.sayHi("Toby"));
        assertEquals("THANK YOU TOBY", proxiedHello.sayThankYou("Toby"));
    }
}
