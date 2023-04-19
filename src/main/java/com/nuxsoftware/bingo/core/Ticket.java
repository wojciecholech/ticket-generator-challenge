package com.nuxsoftware.bingo.core;

import java.util.Arrays;
import java.util.Map;

public class Ticket {

  public static final int NUMBER_SPACES = 5;
  public static final int BLANK_SPACES = 4;
  public static final int TICKET_COLUMNS = NUMBER_SPACES + BLANK_SPACES;

  private final int[][] data;

  Ticket(int[][] table) {
    this.data = table;
  }

  public int[][] getData() {
    return data;
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    for (int i = 0; i < data.length; i++) {
      builder.append(Arrays.toString(data[i])).append("\n");
    }
    return builder.toString();
  }

  public static final Map<Integer, ColumnRange> COLUMN_RANGES = Map.of(
      0, new ColumnRange(1,9),
      1, new ColumnRange(10,19),
      2, new ColumnRange(20,29),
      3, new ColumnRange(30,39),
      4, new ColumnRange(40,49),
      5, new ColumnRange(50,59),
      6, new ColumnRange(60,69),
      7, new ColumnRange(70,79),
      8, new ColumnRange(80,90)
  );

  public record ColumnRange(int from, int to) {
    public int size() {
      return (to - from) + 1;
    }
  }
}
