package com.corgi.example.chapter3.example8;

import com.corgi.example.chapter3.example8.template.LineCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class Calculator {

    public Integer calcSum(String path) throws IOException {
        LineCallback<Integer> callback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value += Integer.parseInt(line);
            }
        };

        return lineReadTemplate(path, callback, 0);
    }

    public Integer calcMultiply(String path) throws IOException {
        LineCallback<Integer> callback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value *= Integer.parseInt(line);
            }
        };

        return lineReadTemplate(path, callback, 1);
    }

    public String concatenate(String path) throws IOException {
        LineCallback<String> callback = new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value += line;
            }
        };

        return lineReadTemplate(path, callback, "");
    }


    public <T> T lineReadTemplate(String path, LineCallback callback, T initVal) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = null;
            T res = initVal;

            while ((line = br.readLine()) != null) {
                res = (T) callback.doSomethingWithLine(line, res);
            }

            return res;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
