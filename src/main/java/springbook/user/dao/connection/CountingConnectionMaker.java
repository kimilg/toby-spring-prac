package springbook.user.dao.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements SimpleConnectionMaker{
    private int counter = 0;
    private SimpleConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(SimpleConnectionMaker connectionMaker) {
        this.realConnectionMaker = connectionMaker;
    }

    @Override
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        counter += 1;
        return realConnectionMaker.makeNewConnection();
    }

    public int getCounter(){
        return counter;
    }
}
