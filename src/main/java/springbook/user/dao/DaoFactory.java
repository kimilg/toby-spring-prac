package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springbook.user.dao.connection.CountingConnectionMaker;
import springbook.user.dao.connection.NConnectionMaker;
import springbook.user.dao.connection.SimpleConnectionMaker;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {
    @Bean
    public UserDaoJdbc userDao(){
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/springbook?characterEncoding=UTF-8");
        dataSource.setUsername("spring");
        dataSource.setPassword("book");

        return dataSource;
    }

    @Bean
    public SimpleConnectionMaker connectionMaker(){
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public SimpleConnectionMaker realConnectionMaker(){
        return new NConnectionMaker();
    }
}

