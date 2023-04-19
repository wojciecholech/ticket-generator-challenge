package com.nuxsoftware.bingo.jmh;

import com.nuxsoftware.bingo.core.StripFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode({Mode.Throughput})
public class StripGeneratorBenchmark {

  @Benchmark
  @Fork(value = 2)
  @Warmup(iterations = 2)
  public void generate() {
    new StripFactory().create();
  }
}
