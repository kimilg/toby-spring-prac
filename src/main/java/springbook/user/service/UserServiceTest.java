/**
 * @(#)UserServiceTest.java 2022. 06. 06
 * <p>
 * Copyright 2022 Naver Corp. All rights Reserved.
 * Naver PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package springbook.user.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * @author user
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    UserService userService;
    
    @Autowired
    DataSource dataSource;
    
    @Autowired
    UserDao userDao;
    
    List<User> users;
    
    @Before
    public void setup(){
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승환", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오만규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }
    
    static class TestUserService extends UserService {
        private String id;
        
        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }
    
    static class TestUserServiceException extends RuntimeException {
    }
    
    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setDataSource(this.dataSource);
        
        this.userDao.deleteAll();
        for(User user : users) userDao.add(user);
        
        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }
        catch(TestUserServiceException e) {
        }
        checkLevelUpgraded(users.get(1), false);
    }
    
    @Test
    public void upgradeLevels() throws Exception {
        userDao.deleteAll();
        for(User user : users) {
            userDao.add(user);
        }
        
        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }
    
    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        }
        else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }
    
    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }
    
    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for(Level level : levels) {
            if(level.nextLevel() == null) continue;
            User user = new User();
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel(), is(level.nextLevel()));
        }
    }
    
    @Test(expected=IllegalStateException.class)
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for(Level level : levels) {
            if(level.nextLevel() != null) continue;
            User user = new User();
            user.setLevel(level);
            user.upgradeLevel();
        }
    }
    
    @Test
    public void add(){
        userDao.deleteAll();
        
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);
        
        userService.add(userWithLevel);
        userService.add(userWithoutLevel);
        
        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
        
        assertThat(userWithLevelRead.getLevel(), is(Level.GOLD));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }
    
}





