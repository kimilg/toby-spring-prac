/**
 * @(#)TxProxyFactoryBean.java 2022. 07. 09
 * <p>
 * Copyright 2022 Naver Corp. All rights Reserved.
 * Naver PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package springbook.user.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

/**
 * @author Ilgoo.Kim
 */
public class TxProxyFactoryBean implements FactoryBean<Object> {
    Object target;
    PlatformTransactionManager transactionManager;
    String pattern;
    Class<?> serviceInterface;
    
    public void setTarget(Object target) {
        this.target = target;
    }
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    public void setServiceInterface(Class serviceInterface) {
        this.serviceInterface = serviceInterface;
    }
    
    @Override
    public Object getObject() throws Exception {
        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setTransactionManager(transactionManager);
        transactionHandler.setTarget(target);
        transactionHandler.setPattern(pattern);
        
        return Proxy.newProxyInstance(getClass().getClassLoader(), 
                new Class[]{serviceInterface}, transactionHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
    
}
