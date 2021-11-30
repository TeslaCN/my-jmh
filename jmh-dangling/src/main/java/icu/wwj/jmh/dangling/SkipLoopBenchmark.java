package icu.wwj.jmh.dangling;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@State(Scope.Thread)
public class SkipLoopBenchmark {
    
    private static final List<String> O = new ArrayList<>();
    
    static {
        for (int i = 0; i < 10000; i++) {
            O.add(Integer.toString(i, 2));
        }
    }
    
    @Benchmark
    public String benchForeachSkip() {
        StringBuilder builder = new StringBuilder(10000);
        for (String s : O) {
            if (s.hashCode() < 1000) {
                builder.append(s);
            }
        }
        return builder.toString();
    }
    
    @Benchmark
    public String benchStreamSkip() {
        return O.stream().filter(each -> each.hashCode() < 1000).collect(Collectors.joining());
    }
}
