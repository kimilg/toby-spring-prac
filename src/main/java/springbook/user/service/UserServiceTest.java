/**
 * @(#)UserServiceTest.java 2022. 06. 06
 * <p>
 * Copyright 2022 Naver Corp. All rights Reserved.
 * Naver PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package springbook.user.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
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
    UserService testUserService;
    
    @Autowired
    PlatformTransactionManager transactionManager;
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    ApplicationContext context;        
    
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
    
    static class TestUserServiceImpl extends UserServiceImpl {
        private String id = "madnite1";

        @Override
        protected void upgradeLevel(User user) {
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }
    
    static class TestUserServiceException extends RuntimeException {
    }
    
    
    static class MockUserDao implements  UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();
    
        private MockUserDao(List<User> users) {
            this.users = users;
        }
        
        @Override
        public List<User> getAll() {
            return this.users;
        }
        
        private List<User> getUpdated() {
            return this.updated;
        }
        
        @Override
        public void update(User user) {
            updated.add(user);
        }
        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }
        @Override
        public void delete(String id) {
            throw new UnsupportedOperationException();
        }
        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }
        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }
        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }
    
    @Test
    public void upgradeAllOrNothing() throws Exception{
        this.userDao.deleteAll();
        for(User user : users) userDao.add(user);
        
        try {
            this.testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }
        catch(TestUserServiceException e) {
        }
        checkLevelUpgraded(users.get(1), false);
    }
    
    @Test
    public void advisorAutoProxyCreator() {
        assertThat(java.lang.reflect.Proxy.class.isAssignableFrom(testUserService.getClass()), is(true));
    }
    
    @Test
    public void upgradeLevels() throws Exception {
        UserServiceImpl userServiceImpl= new UserServiceImpl();
        MockUserDao mockUserDao = new MockUserDao(users);
        userServiceImpl.setUserDao(mockUserDao);
        
        userServiceImpl.upgradeLevels();
        
        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkLevelUpgraded(updated.get(0), "joytouch", Level.SILVER);
        checkLevelUpgraded(updated.get(1), "madnite1", Level.GOLD);
    }

    @Test
    public void mockUpgradeLevels() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        UserDao mockUserDao = mock(UserDao.class);
        userServiceImpl.setUserDao(mockUserDao);
        
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.upgradeLevels();
        
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(1)).update(users.get(1));
        assertThat(users.get(1).getLevel(), is(Level.SILVER));
        verify(mockUserDao, times(1)).update(users.get(3));
        assertThat(users.get(3).getLevel(), is(Level.GOLD));

        ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
        verify(mockUserDao, times(2)).update(userArg.capture());
        List<User> updatedUsers = userArg.getAllValues();
        assertThat(updatedUsers.get(0), is(users.get(1)));
        assertThat(updatedUsers.get(1), is(users.get(3)));
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

    private void checkLevelUpgraded(User user, String userId, Level expectedLevel) {
        assertThat(user.getId(), is(userId));
        assertThat(user.getLevel(), is(expectedLevel));
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





