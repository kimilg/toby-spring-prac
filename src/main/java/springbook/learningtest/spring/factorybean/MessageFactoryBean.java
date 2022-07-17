/**
 * @(#)MessageFactoryBean.java 2022. 07. 09
 * <p>
 * Copyright 2022 Naver Corp. All rights Reserved.
 * Naver PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package springbook.learningtest.spring.factorybean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Ilgoo.Kim
 */
public class MessageFactoryBean implements FactoryBean<Message> {
    String text;
    
    public void setText(String text) {
        this.text = text;
    }
    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
