package icu.wwj.jmh.jdbc.connection;

import icu.wwj.jmh.jdbc.JDBCConnectionProvider;

import java.sql.Connection;

public interface UnpooledJDBCConnectionProvider extends JDBCConnectionProvider {
    
    @Override
    default Connection getConnection() {
        return Jdbcs.getConnection();
    }
}
