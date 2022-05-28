package springbook.user.dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface SimpleConnectionMaker  {
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException;
}
