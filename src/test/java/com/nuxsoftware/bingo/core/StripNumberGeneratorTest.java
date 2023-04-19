package com.nuxsoftware.bingo.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.nuxsoftware.bingo.core.StripNumberGenerator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StripNumberGeneratorTest {

  StripNumberGenerator numberGenerator;

  @BeforeEach
  void setUp() {
    numberGenerator = new StripNumberGenerator(new Random());
  }

  @Test
  public void allNumbersAreInserted() {
    // when
    var strip = numberGenerator.insert(stripPositions, new int[18][9]);

    // then
    for (int rowNo = 0; rowNo < stripPositions.length; rowNo++) {
      for (int columnNo = 0; columnNo < stripPositions[rowNo].length; columnNo++) {
        if (stripPositions[rowNo][columnNo]) {
          assertThat(strip[rowNo][columnNo]).isBetween(1, 90);
        } else {
          assertThat(strip[rowNo][columnNo]).isEqualTo(-1);
        }
      }
    }
  }

  @Test
  public void noDuplicatesInStrip() {
    // when
    var strip = numberGenerator.insert(stripPositions, new int[18][9]);
    var numbers = new HashSet<Integer>();

    // then
    for (int rowNo = 0; rowNo < stripPositions.length; rowNo++) {
      for (int columnNo = 0; columnNo < stripPositions[rowNo].length; columnNo++) {
        if (strip[rowNo][columnNo] != -1) {
          numbers.add(strip[rowNo][columnNo]);
        }
      }
    }

    assertThat(numbers).hasSize(90);
    assertThat(numbers).allMatch(val -> val > 0 && val < 91);
  }

  @Test
  public void numbersProperlyArrangedInFirstColumn() {
    // given
    var columnNo = 0;

    // when
    var strip = numberGenerator.insert(stripPositions, new int[18][9]);
    var column = extractColumn(strip, columnNo);

    // then
    assertThat(column).hasSize(9);
    assertThat(column).allMatch(val -> val >= 1 && val <= 9);
  }

  @Test
  public void numbersProperlyArrangedInSecondToEightColumns() {
    // when
    var strip = numberGenerator.insert(stripPositions, new int[18][9]);
    var columns = IntStream.range(1, 8).boxed()
        .collect(Collectors.toMap(columnNo -> columnNo, columnNo -> extractColumn(strip, columnNo)));

    // then
    assertThat(columns).hasSize(7);
    assertThat(columns.values()).allMatch(column -> column.size() == 10);
    columns.forEach((column, numbers) -> assertThat(numbers).allMatch(val -> val >= column*10 && val <= column*10+9));
  }

  private Set<Integer> extractColumn(int[][] strip, int columnNo) {
    var column = new HashSet<Integer>();

    for (int rowNo = 0; rowNo < stripPositions.length; rowNo++) {
      if (strip[rowNo][columnNo] != -1) {
        column.add(strip[rowNo][columnNo]);
      }
    }

    return column;
  }

  boolean[][] stripPositions = {
      {true, false, true, true, true, true, false, false, false},
      {false, true, false, true, true, false, true, false, true},
      {true, false, true, true, false, true, false, false, true},

      {false, true, true, false, true, false, true, false, true},
      {true, false, true, false, false, false, true, true, true},
      {true, true, false, false, true, true, true, false, false},

      {false, true, false, false, true, true, true, false, true},
      {false, false, false, false, true, true, true, true, true},
      {false, true, false, false, true, false, true, true, true},

      {true, true, false, true, false, true, true, false, false},
      {true, true, true, true, false, false, false, true, false},
      {true, false, false, false, true, false, true, true, true},

      {false, true, true, true, true, false, false, true, false},
      {false, true, true, true, true, true, false, false, false},
      {true, true, false, false, false, true, false, true, true},

      {false, false, true, true, false, false, true, true, true},
      {false, false, true, true, false, true, false, true, true},
      {true, false, true, true, false, true, false, true, false}};
}
