package icu.wwj.jmh.jdbc;

import java.sql.Connection;

public class UnpooledReadOnlyBenchmarkJDBC extends UnpooledReadOnlyBenchmarkBase {
    
    @Override
    public Connection getConnection() {
        return Jdbcs.getConnection();
    }
}
