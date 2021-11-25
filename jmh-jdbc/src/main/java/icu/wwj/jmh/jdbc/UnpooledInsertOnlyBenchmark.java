package icu.wwj.jmh.jdbc;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Group)
@Fork(3)
@Warmup(iterations = 10, time = 3)
@Measurement(iterations = 10, time = 3)
@Deprecated
public class UnpooledInsertOnlyBenchmark {
    
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    
    private Connection connection;
    
    private PreparedStatement preparedStatement;
    
    @Setup(Level.Iteration)
    public void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo_ds?useSSL=false&useServerPrepStmts=true&cachePrepStmts=true", "root", "");
        preparedStatement = connection.prepareStatement("insert into t_order (order_id, user_id, n, status, amount) values (?, ?, ?, ?, ?)");
    }
    
    @Group
    @Benchmark
    public void benchInsert() throws Exception {
        preparedStatement.setLong(1, random.nextLong(Long.MAX_VALUE));
        preparedStatement.setLong(2, random.nextLong(Long.MAX_VALUE));
        preparedStatement.setString(3, randomString(100));
        preparedStatement.setInt(4, random.nextInt());
        preparedStatement.setBigDecimal(5, BigDecimal.valueOf(random.nextLong(10_000_000)));
        try {
            preparedStatement.execute();
        } catch (Exception ignored) {
        }
    }
    
    private String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        while (sb.length() < length) {
            sb.append(ThreadLocalRandom.current().nextLong());
        }
        sb.setLength(length);
        return sb.toString();
    }
    
    @TearDown(Level.Iteration)
    public void tearDown() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}
