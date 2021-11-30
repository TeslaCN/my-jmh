package icu.wwj.jmh.dangling;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Benchmark)
public class LoopBenchmark {
    
    private final List<Integer> list = new ArrayList<>(); 
    
    @Setup
    public void setup() {
        for (int i = 0; i < 10000000; i++) {
            list.add(ThreadLocalRandom.current().nextInt());
        }
    }
    
    @Benchmark
    public int benchForIndex() {
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }
        return sum;
    }
    
    @Benchmark
    public int benchForEach() {
        int sum = 0;
        for (Integer each : list) {
            sum += each;
        }
        return sum;
    }
    
    @Benchmark
    public int benchStream() {
        return list.stream().mapToInt(each -> each).sum();
    }
    
    @Benchmark
    public int benchParallelStream() {
        return list.parallelStream().mapToInt(each -> each).sum();
    }
}
