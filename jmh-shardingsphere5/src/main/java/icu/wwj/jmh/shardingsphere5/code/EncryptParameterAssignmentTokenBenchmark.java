package icu.wwj.jmh.shardingsphere5.code;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shardingsphere.encrypt.rewrite.token.pojo.EncryptParameterAssignmentToken;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.CompilerControl.Mode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.profile.AsyncProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class EncryptParameterAssignmentTokenBenchmark {
    
    private EncryptParameterAssignmentToken target;
    
    @Setup(Level.Trial)
    public void setup() {
        target = new EncryptParameterAssignmentToken(0, 0);
        for (int i = 0; i < 16; i++) {
            target.addColumnName(RandomStringUtils.randomAlphanumeric(8));
        }
    }
    
    @Benchmark
    @CompilerControl(Mode.DONT_INLINE)
    public String bench() {
        return target.toString();
    }
    
    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
                .include(EncryptParameterAssignmentTokenBenchmark.class.getName())
                .forks(3)
                .warmupIterations(5)
                .warmupTime(TimeValue.seconds(3))
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(3))
                .timeUnit(TimeUnit.MILLISECONDS)
                .jvmArgs("-XX:+SegmentedCodeCache", "-XX:+AggressiveHeap")
                .threads(Runtime.getRuntime().availableProcessors() / 2)
                .addProfiler(AsyncProfiler.class, "libPath=/home/wuweijie/dev/async-profiler/async-profiler-2.8-linux-x64/build/libasyncProfiler.so;traces=0;flat=50;alluser=true")
                .build()).run();
    }
}
