package icu.wwj.jmh.dangling;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

@Threads(8)
@Fork(4)
@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 5, time = 3)
@State(Scope.Benchmark)
public class CaseInsensitiveMapBenchmark {
    
    private static final Object OBJECT = new Object();
    
    private static final String[] STRINGS = new String[]{"keY0", "Key1", "KEy2", "kEy3", "KeY4", "KEY5", "key6", "kEY7"};
    
    private static final String[] STRINGS_LOWER;
    
    static {
        STRINGS_LOWER = new String[STRINGS.length];
        for (int i = 0; i < STRINGS.length; i++) {
            STRINGS_LOWER[i] = STRINGS[i].toLowerCase(Locale.ENGLISH);
        }
    }
    
    @Benchmark
    public void benchCaseInsensitiveTreeMap() {
        Map<String, Object> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (String each : STRINGS) {
            map.put(each, OBJECT);
        }
        for (String each : STRINGS_LOWER) {
            map.get(each);
        }
    }
    
    @Benchmark
    public void benchCommonsCaseInsensitiveMap() {
        Map<String, Object> map = new CaseInsensitiveMap<>();
        for (String each : STRINGS) {
            map.put(each, OBJECT);
        }
        for (String each : STRINGS_LOWER) {
            map.get(each);
        }
    }
}
