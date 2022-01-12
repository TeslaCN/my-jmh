package icu.wwj.jmh.jdbc;

import java.sql.Connection;

public class UnpooledPointSelectBenchmarkJDBC extends UnpooledPointSelectBenchmarkBase {
    
    @Override
    public Connection getConnection() {
        return Jdbcs.getConnection();
    }
}
