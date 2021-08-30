package icu.wwj.jmh.shardingsphere5;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Group)
public class MultiShardingFullPointSelectBenchmark {
    
    private static final int TABLE_SIZE = 1_000_000;
    
//    private static final DataSource dataSource = ShardingSpheres.createDataSource("/shardingsphere-jdbc/sysbench-remote-single.yaml");
    private static final DataSource dataSource = ShardingSpheres.createDataSource("/shardingsphere-jdbc/sysbench-remote-dd-sharding.yaml");
    
    private final PreparedStatement[] preparedStatements = new PreparedStatement[10];
    
    private Connection connection;
    
    @Setup(Level.Iteration)
    public void setup() throws Exception {
        connection = dataSource.getConnection();
        for (int i = 0; i < preparedStatements.length; i++) {
            preparedStatements[i] = connection.prepareStatement("select c from sbtest" + (i + 1) + " where id = ?");
        }
    }
    
    @Group("FullPointSelect")
    @Benchmark
    @OperationsPerInvocation(10)
    public void benchFullPointSelect() throws Exception {
        for (PreparedStatement each : preparedStatements) {
            each.setInt(1, ThreadLocalRandom.current().nextInt(TABLE_SIZE));
            each.execute();
        }
    }
    
    @TearDown(Level.Iteration)
    public void tearDown() throws Exception {
        for (PreparedStatement each : preparedStatements) {
            each.close();
        }
        connection.close();
    }
}
