package springbook.learningtest.template;

import ch.qos.logback.classic.pattern.LineOfCallerConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(String filepath) throws IOException {
        LineCallback<Integer> callback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return Integer.valueOf(line) + value;
            }
        };
        return fileReadTemplate(filepath, callback, 0);
    }
    
    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback<Integer> callback = new LineCallback<>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return Integer.valueOf(line) * value;
            }
        };
        return fileReadTemplate(filepath, callback, 1);
    }
    
    public String concatenateStrings(String filepath) throws IOException {
        LineCallback<String> callback = new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        };
        return fileReadTemplate(filepath, callback, "");
    }
    
    public <T> T fileReadTemplate(String filepath, LineCallback<T> callback, T initValue) 
            throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            T sum = initValue;
            String line = null;
            while((line = br.readLine()) != null) {
                sum = callback.doSomethingWithLine(line, sum);
            }
            return sum;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch(IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
