package me.loki2302.jmh;

import me.loki2302.jmh.impl.Jmh;
import org.junit.Test;

import java.util.Arrays;

public class AllBenchmarksTest {
    @Test
    public void runAllBenchmarks() {
        Jmh.benchmark(Arrays.asList("me.loki2302.*"));
    }
}
