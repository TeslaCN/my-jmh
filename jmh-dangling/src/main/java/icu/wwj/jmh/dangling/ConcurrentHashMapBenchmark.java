package icu.wwj.jmh.dangling;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Fork(3)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 5)
@Threads(16)
@State(Scope.Benchmark)
public class ConcurrentHashMapBenchmark {
    
    private static final String KEY = "key";
    
    private static final Object VALUE = new Object();
    
    private final Map<String, Object> concurrentMap = new ConcurrentHashMap<>(1, 1);
    
    @Setup(Level.Iteration)
    public void setup() {
        concurrentMap.clear();
    }
    
    @Benchmark
    public Object benchGetBeforeComputeIfAbsent() {
        Object result = concurrentMap.get(KEY);
        if (null == result) {
            result = concurrentMap.computeIfAbsent(KEY, __ -> VALUE);
        }
        return result;
    }
    
    @Benchmark
    public Object benchComputeIfAbsent() {
        return concurrentMap.computeIfAbsent(KEY, __ -> VALUE);
    }
}
