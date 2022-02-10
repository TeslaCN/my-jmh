package icu.wwj.jmh.jdbc;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 5, time = 3)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class PgPreparedStatementBenchmark {
    
    private final Connection connection;
    
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    
    private PreparedStatement preparedStatement;
    
    public PgPreparedStatementBenchmark() {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "postgres");
        } catch (SQLException e) {
            connection = null;
            e.printStackTrace();
        }
        this.connection = connection;
    }
    
    @Setup(Level.Invocation)
    public void setup() {
        try {
            preparedStatement = connection.prepareStatement("select ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Benchmark
    public void benchSetTimestamp() throws SQLException {
        preparedStatement.setTimestamp(1, new Timestamp(random.nextLong()));
    }
    
    @TearDown(Level.Invocation)
    public void tearDown() {
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
                .include(PgPreparedStatementBenchmark.class.getName())
                .threads(16)
                .forks(3)
                .build()).run();
    }
}
