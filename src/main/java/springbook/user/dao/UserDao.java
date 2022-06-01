package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import springbook.user.dao.context.JdbcContext;
import springbook.user.dao.strategy.StatementStrategy;
import springbook.user.domain.User;

import javax.sql.DataSource;

public class UserDao {
    
    private DataSource dataSource;
    
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);        
        this.dataSource = dataSource;
    }
    
    public User get(String id) {
        return this.jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("select * from users where id = ?");
                ps.setString(1, id);
                return ps;
            }
        }, new ResultSetExtractor<User>() {
            public User extractData(ResultSet rs) throws SQLException, DataAccessException {
                User user = null;
                if(rs.next()) {
                    user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                }
                
                if(user == null) {
                    throw new EmptyResultDataAccessException(1);
                }

                return user;
            }
        });
    }
    
    public void delete(String id) throws SQLException {
        Connection c = this.dataSource.getConnection();
        PreparedStatement ps = c
                .prepareStatement("delete from users where id = ?");
        ps.setString(1, id);
        
        ps.executeUpdate();
        ps.close();
        c.close();
    }

    public void add(User user) {
        this.jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }
    
    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }
    
    public int getCount() {
        return this.jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                resultSet.next();
                return resultSet.getInt(1);
            }
        });
    }

}
