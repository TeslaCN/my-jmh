package icu.wwj.jmh.jdbc;

import java.sql.Connection;

public class UnpooledReadWriteBenchmarkJDBC extends UnpooledReadWriteBenchmarkBase {
    
    @Override
    public Connection getConnection() {
        return Jdbcs.getConnection();
    }
}
