package com.corgi.example.learningtest;

import com.corgi.example.learningtest.target.Bean;
import com.corgi.example.learningtest.target.Target;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointcutExpressionTests {

    private String expression;
    private List<Boolean> expected;

    @BeforeEach
    void setUp() {
        this.expression = "execution(* *(..))";
        this.expected = Arrays.asList(true, true, true, true, true, true);
    }

    /**
     * 테스트 헬퍼를 통해 개선하기 전 테스트 코드
     */
//    @Test
//    void methodSignaturePointcut() throws NoSuchMethodException {
//
//        // method full signature
//        System.out.println(Target.class.getMethod("minus", int.class, int.class));
//
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression("execution(public int com.corgi.example.learningtest" +
//                ".target.Target.minus(int,int) throws java.lang.RuntimeException)");
//
//        // 리턴 타입은 상관없이 minus라는 메소드 이름을 가지면서 두 개의 int 파라미터를 가진 모든 메소드를 선정하는 포인트컷 표현식
//        pointcut.setExpression("execution(int minus(int, int))");
//
//        // 리턴 타입과 파라미터의 종류, 개수에 상관없이 minus 라는 메소드 이름을 가진 모든 메소드를 선정하는 포인트컷 표현식
//        pointcut.setExpression("execution(* minus(..))");
//
//        // 리턴 타입, 파라미터, 메소드 이름에 상관없이 모든 메소드를 선정하는 포인트컷 표현식
//        pointcut.setExpression("execution(* *(..))");
//
//        /**
//         * Target.minus()
//         * ClassFilter와 MethodMatcher를 각각 가져와 비교한다.
//         */
//        assertEquals(true, pointcut.getClassFilter().matches(Target.class)
//                && pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null));
//
//        /**
//         * Target.plus()
//         */
//        assertEquals(false, pointcut.getClassFilter().matches(Target.class)
//                && pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null));
//
//        /**
//         * Bean.method()
//         */
//        assertEquals(false, pointcut.getClassFilter().matches(Bean.class)
//                && pointcut.getMethodMatcher().matches(Bean.class.getMethod("method"), null));
//    }

    /**
     * 테스트 헬퍼를 통해 개선한 테스트 코드
     */
    @Test
    void methodSignaturePointcut() throws Exception {
        pointcutMatches(expression, expected.get(0), Target.class, "hello");
        pointcutMatches(expression, expected.get(1), Target.class, "hello", String.class);
        pointcutMatches(expression, expected.get(2), Target.class, "plus", int.class, int.class);
        pointcutMatches(expression, expected.get(3), Target.class, "minus", int.class, int.class);
        pointcutMatches(expression, expected.get(4), Target.class, "method");
        pointcutMatches(expression, expected.get(5), Bean.class, "method");
    }

    /**
     * 포인트컷과 메소드를 비교해주는 테스트 헬퍼 메소드
     */
    public void pointcutMatches(String expression, Boolean expected,
                                Class<?> clazz, String methodName, Class<?> ... args) throws Exception {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        assertEquals(expected, pointcut.getClassFilter().matches(clazz)
                && pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null));
    }
}
