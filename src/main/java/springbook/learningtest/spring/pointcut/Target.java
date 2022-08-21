/**
 * @(#)Target.java 2022. 08. 08
 * <p>
 * Copyright 2022 Naver Corp. All rights Reserved.
 * Naver PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package springbook.learningtest.spring.pointcut;

/**
 * @author Ilgoo.Kim
 */
public class Target implements TargetInterface{
    @Override
    public void hello() {}

    @Override
    public void hello(String a) {}

    @Override
    public int minus(int a, int b) throws RuntimeException {
        return 0;
    }

    @Override
    public int plus(int a, int b) {
        return 0;
    }
    
    public void method(){}
}
