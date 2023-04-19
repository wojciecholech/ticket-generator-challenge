package com.nuxsoftware.bingo.core;

import static com.nuxsoftware.bingo.core.Ticket.COLUMN_RANGES;

import com.nuxsoftware.bingo.core.Ticket.ColumnRange;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StripNumberGenerator {

  private final Map<Integer, Cage> cages;

  public StripNumberGenerator(Random random) {
    this.cages = COLUMN_RANGES.entrySet()
        .stream()
        .collect(Collectors.toMap(Entry::getKey, e -> new Cage(e.getValue(), random)));
  }

  public int[][] insert(boolean[][] positions, int[][] table) {
    for (int rowNo = 0; rowNo < positions.length; rowNo++) {
      for (int columnNo = 0; columnNo < positions[rowNo].length; columnNo++) {
        table[rowNo][columnNo] = positions[rowNo][columnNo] ? next(columnNo) : -1;
      }
    }
    return table;
  }

  private int next(int columnNo) {
    return cages.get(columnNo).next();
  }

  private static class Cage {

    private final ColumnRange range;
    private final Random random;
    private final List<Integer> balls;
    private final AtomicInteger round = new AtomicInteger(0);

    Cage(ColumnRange range, Random random) {
      this.range = range;
      this.random = random;
      this.balls = shuffle(balls(range));
    }

    int next() {
      if (round.get() > range.size()) {
        throw new IndexOutOfBoundsException("No more balls in the cage :(");
      }
      return balls.get(round.getAndIncrement());
    }

    private List<Integer> balls(ColumnRange range) {
      return IntStream.rangeClosed(range.from(), range.to())
          .boxed()
          .collect(Collectors.toList());
    }

    private List<Integer> shuffle(List<Integer> balls) {
      Collections.shuffle(balls, random);
      return balls;
    }
  }
}