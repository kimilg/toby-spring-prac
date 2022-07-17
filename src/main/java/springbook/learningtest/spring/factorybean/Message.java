/**
 * @(#)Message.java 2022. 07. 09
 * <p>
 * Copyright 2022 Naver Corp. All rights Reserved.
 * Naver PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package springbook.learningtest.spring.factorybean;

/**
 * @author Ilgoo.Kim
 */
public class Message {
    String text;
    private Message(String text){
        this.text = text;
    }
    public String getText(){
        return text;
    }
    public static Message newMessage(String text) {
        return new Message(text);
    }
}
