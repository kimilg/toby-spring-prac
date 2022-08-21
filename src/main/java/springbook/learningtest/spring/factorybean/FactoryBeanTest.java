/**
 * @(#)FactoryBeanTest.java 2022. 07. 09
 * <p>
 * Copyright 2022 Naver Corp. All rights Reserved.
 * Naver PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package springbook.learningtest.spring.factorybean;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Ilgoo.Kim
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/FactoryBeanTest-context.xml")
public class FactoryBeanTest {
    @Autowired
    ApplicationContext context;
    
    @Test
    public void getMessageFromFactoryBean(){
        Object message = context.getBean("message");
        assertThat(message.getClass(), is(Message.class));
        assertThat(((Message)message).getText(), is("Factory Bean"));
    }
    
    @Test
    public void getFactoryBean() {
        Object factory = context.getBean("&message");
        assertThat(factory.getClass(), is(MessageFactoryBean.class));
    }
}
