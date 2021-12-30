package icu.wwj.jmh.dangling;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.ThreadLocalRandom;

@Fork(3)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 5)
@Threads(16)
@State(Scope.Benchmark)
public class StringConcatBenchmark {
    
    private final String a = "" + ThreadLocalRandom.current().nextDouble();
    
    private final String b = "" + ThreadLocalRandom.current().nextDouble();
    
    private final String c = "" + ThreadLocalRandom.current().nextDouble();
    
    @Benchmark
    public String benchFormat() {
        return String.format("%s%s%s", a, b, c);
    }
    
    @Benchmark
    public String benchPlus() {
        return a + b + c;
    }
}
