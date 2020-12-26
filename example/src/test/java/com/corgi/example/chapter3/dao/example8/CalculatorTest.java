package com.corgi.example.chapter3.dao.example8;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName(value = "example-8")
public class CalculatorTest {

    private Calculator calculator;
    private String path;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        this.calculator = new Calculator();
        this.path = ResourceUtils.getFile("classpath:chapter3/numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        assertEquals(10, calculator.calcSum(this.path));
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        assertEquals(24, calculator.calcMultiply(this.path));
    }

    @Test
    public void concatenateStrings() throws IOException {
        assertEquals("1234", calculator.concatenate(this.path));
    }
}
