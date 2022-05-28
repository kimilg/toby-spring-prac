package springbook.user.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.Assert;
import springbook.user.dao.connection.CountingConnectionMaker;
import springbook.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args){
        JUnitCore.main("springbook.user.dao.UserDaoTest");
    }
    
    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);
        
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
        
        User user = new User("kimig2", "holyman", "yeap");

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user.getId());
        
        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));

        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println(ccm.getCounter());
    }
    
    @Test(expected= EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);
        
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
        
        dao.get("unknown id");
    }
}
