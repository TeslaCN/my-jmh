package icu.wwj.jmh.dangling;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@Fork(3)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 5)
@Threads(1)
@State(Scope.Benchmark)
public class GetCanonicalNameBenchmark {
    
    @Benchmark
    public String benchGetCanonicalName() {
        for (int i = 0; i < 7; i++) {
            GetCanonicalNameBenchmark.class.getCanonicalName();
        }
        return GetCanonicalNameBenchmark.class.getCanonicalName();
    } 
}
