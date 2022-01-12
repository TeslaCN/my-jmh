package icu.wwj.jmh.shardingsphere3;

import icu.wwj.jmh.jdbc.UnpooledPointSelectBenchmarkBase;
import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import javax.sql.DataSource;
import java.sql.Connection;

@State(Scope.Benchmark)
public class PointSelectBenchmarkShardingSphere3 extends UnpooledPointSelectBenchmarkBase {
    
    private static final DataSource DATA_SOURCE = ShardingSpheres.createDataSource("/sharding-jdbc/sysbench-remote-single.yaml");
    
    @Override
    @SneakyThrows
    public Connection getConnection() {
        return DATA_SOURCE.getConnection();
    }
}
