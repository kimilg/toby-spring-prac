package springbook.user.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import springbook.user.dao.connection.CountingConnectionMaker;
import springbook.user.domain.User;

import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserDaoTest {
    
    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private UserDao dao;
    
    private User user1;
    private User user2;
    private User user3;
    
    @Before
    public void setup(){
        this.user1 = new User("gyumee", "박성철", "springno1");
        this.user2 = new User("leegw700", "이길원", "springno2");
        this.user3 = new User("bumjin", "박범진", "springno3");

        System.out.println(context);
        System.out.println(this);
        
    }
    
    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
        
        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
        
        User user = dao.get(user1.getId());
        
        assertThat(user1.getName(), is(user.getName()));
        assertThat(user1.getPassword(), is(user.getPassword()));

    }
    
    @Test(expected= EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
        
        dao.get("unknown id");
    }
}
