/**
 * @(#)UserServiceTx.java 2022. 07. 02
 * <p>
 * Copyright 2022 Naver Corp. All rights Reserved.
 * Naver PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package springbook.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.domain.User;

import java.util.List;

/**
 * @author Ilgoo.Kim
 */
public class UserServiceTx implements  UserService {
    UserService userService;
    
    PlatformTransactionManager transactionManager;
    
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    @Override
    public void upgradeLevels() {
        TransactionStatus status = this.transactionManager
                .getTransaction(new DefaultTransactionDefinition());
        try {
            userService.upgradeLevels();
            this.transactionManager.commit(status);
        } catch(RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void add(User user) {
        userService.add(user);
    }

    @Override
    public User get(String id) {
        return userService.get(id);
    }

    @Override
    public List<User> getAll() {
        return userService.getAll();
    }

    @Override
    public void deleteAll() {
        userService.deleteAll();
    }

    @Override
    public void delete(String id) {
        userService.delete(id);
    }

    @Override
    public void update(User user) {
        userService.update(user);
    }
}
