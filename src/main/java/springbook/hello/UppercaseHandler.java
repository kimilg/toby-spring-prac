/**
 * @(#)UppercaseHandler.java 2022. 07. 03
 * <p>
 * Copyright 2022 Naver Corp. All rights Reserved.
 * Naver PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package springbook.hello;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Ilgoo.Kim
 */
public class UppercaseHandler implements InvocationHandler {
    Hello target;
    
    public UppercaseHandler(Hello target) {
        this.target = target;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String ret = (String)method.invoke(target, args);
        return ret.toUpperCase();
    }
}
