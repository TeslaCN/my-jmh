package icu.wwj.jmh.shardingsphere5;

import icu.wwj.jmh.jdbc.UnpooledReadOnlyBenchmarkBase;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;

public class ShardingSphereReadOnlyBenchmark extends UnpooledReadOnlyBenchmarkBase {
    
    private static final DataSource DATA_SOURCE;
    
    static {
        DATA_SOURCE = ShardingSpheres.createDataSource();
    }
    
    @SneakyThrows
    @Override
    public Connection getConnection() {
        return DATA_SOURCE.getConnection();
    }
}
