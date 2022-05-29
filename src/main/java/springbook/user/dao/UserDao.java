package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.protocol.Resultset;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.dao.connection.SimpleConnectionMaker;
import springbook.user.dao.strategy.AddStatement;
import springbook.user.dao.strategy.DeleteAllStatement;
import springbook.user.dao.strategy.StatementStrategy;
import springbook.user.domain.User;

import javax.sql.DataSource;

public class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User get(String id) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            c = this.dataSource.getConnection();
            ps = c.prepareStatement("select * from users where id = ?");
            ps.setString(1, id);
            rs = ps.executeQuery();
            
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
            
        } catch(SQLException e) {
            throw e;
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch(SQLException e){
                    
                }
            }
            if(ps != null) {
                try {
                    ps.close();
                } catch(SQLException e) {
                    
                }
            }
            if(c != null) {
                try {
                    c.close();
                } catch(SQLException e) {
                    
                }
            }
        }
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

    public void add(User user) throws SQLException {
        jdbcContextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                
                return ps;
            }
        });
    }
    
    public void deleteAll() throws SQLException {
        jdbcContextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("delete from users");
                return ps;
            }
        });
    }
    
    public void jdbcContextWithStatementStrategy(StatementStrategy stm) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        
        try{
            c = dataSource.getConnection();
            ps = stm.makePreparedStatement(c);
            ps.executeUpdate();
        } catch(SQLException e) {
            throw e;
        } finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch(SQLException e) {
                }
            }
            if(c != null) {
                try {
                    c.close();
                } catch(SQLException e) {
                    
                }
            }
        }
        
    }
    
    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch(SQLException e) {
            throw e;
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch(SQLException e) {
                    
                }
            }
            if(ps != null) {
                try {
                    ps.close();
                } catch(SQLException e) {
                    
                }
            }
            if(c != null) {
                try {
                    c.close();
                } catch(SQLException e) {
                    
                }
            }
        }
        
    }

}
