package com.hugh.base.service.bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class Benchmarks {

    @Benchmark
    public void testStringConcat() {
        String s = "a";
        for (int i = 0; i < 100; i++) s = s + i;
    }
}
