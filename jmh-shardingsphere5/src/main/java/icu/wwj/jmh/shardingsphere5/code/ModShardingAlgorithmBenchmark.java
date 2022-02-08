package icu.wwj.jmh.shardingsphere5.code;

import org.apache.shardingsphere.sharding.algorithm.sharding.mod.ModShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Fork(1)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 5)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ModShardingAlgorithmBenchmark {
    
    @Param({"1", "100"})
    private static int shardingCount;
    
    private final ModShardingAlgorithm algorithm = new ModShardingAlgorithm();
    
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    
    private Collection<String> availableTargets = IntStream.range(0, shardingCount).mapToObj(each -> "ds_" + each).collect(Collectors.toList());
    
    @Setup(Level.Trial)
    public void setup() {
        algorithm.getProps().setProperty("sharding-count", "" + shardingCount);
        algorithm.init();
        availableTargets = IntStream.range(0, shardingCount).mapToObj(each -> "ds_" + each).collect(Collectors.toList());
    }
    
    @Benchmark
    public String benchModShardingAlgorithm() {
        return algorithm.doSharding(availableTargets, new PreciseShardingValue<>("", "", random.nextLong(Long.MAX_VALUE)));
    }
    
    public static void main(String[] args) throws IOException {
        String[] result = new String[1 + args.length];
        result[0] = ModShardingAlgorithmBenchmark.class.getName();
        System.arraycopy(args, 0, result, 1, args.length);
        Main.main(result);
    }
}
