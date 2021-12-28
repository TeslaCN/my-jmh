package icu.wwj.jmh.jdbc;

import java.sql.Connection;

public class UnpooledBatchInsertBenchmarkJDBC extends UnpooledBatchInsertBenchmarkBase {
    
    @Override
    public Connection getConnection() {
        return Jdbcs.getConnection();
    }
}
