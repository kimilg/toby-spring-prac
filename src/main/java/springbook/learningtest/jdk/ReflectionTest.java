/**
 * @(#)ReflectionTest.java 2022. 07. 03
 * <p>
 * Copyright 2022 Naver Corp. All rights Reserved.
 * Naver PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package springbook.learningtest.jdk;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author Ilgoo.Kim
 */
public class ReflectionTest {
    @Test
    public void invokeMethod() throws Exception {
        String name = "Spring";
        
        assertThat(name.length(), is(6));
        
        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer)lengthMethod.invoke(name), is(6));
        
        assertThat(name.charAt(0), is('S'));
        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character)charAtMethod.invoke(name, 0), is('S'));
    }
}
