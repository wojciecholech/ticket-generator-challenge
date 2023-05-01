package com.nuxsoftware.bingo.core;

import static com.nuxsoftware.bingo.core.Ticket.TICKET_COLUMNS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StripPositionGeneratorTest {

  StripPositionGenerator generator;

  @BeforeEach
  void setUp() {
    generator = new StripPositionGenerator(new Random());
  }

  @Test
  public void thereIsNoThreeBlanksInTicket() {
    // given
    var positions = generator.getPositions();

    // then
    for (int columnNo = 0; columnNo < TICKET_COLUMNS; columnNo++) {
      for (int ticketNo = 0; ticketNo < 6; ticketNo++) {
        assertThat(hasThreeBlanks(positions, ticketNo * 3, columnNo)).isFalse();
      }
    }
  }

  @Test
  public void countOfBlankSpacesNeedToBeFive() {
    // given
    var positions = generator.getPositions();

    // then
    for (int rowNo = 0; rowNo < positions.length; rowNo++) {
      assertThat(positionTypeCount(positions[rowNo], true)).isEqualTo(5);
      assertThat(positionTypeCount(positions[rowNo], false)).isEqualTo(4);
    }
  }

  @Test
  public void countOfColumnsNeedToBeNine() {
    // when
    var positions = generator.getPositions();

    // then
    for (int rownNo = 0; rownNo < positions.length; rownNo++) {
      assertThat(positions[rownNo]).hasSize(9);
    }
  }

  @Test
  public void countOfNumbersInFirstColumn() {
    // given
    var columnPositions = new ArrayList<Boolean>();

    // when
    var positions = generator.getPositions();
    for (int rownNo = 0; rownNo < positions.length; rownNo++) {
      columnPositions.add(positions[rownNo][0]);
    }

    // then
    assertThat(columnPositions).haveExactly(9, new Condition<>(v -> v, "number"));
    assertThat(columnPositions).haveExactly(9, new Condition<>(v -> !v, "blank"));
  }

  @Test
  public void countOfNumbersInSecondToEightColumns() {
    // when
    var positions = generator.getPositions();

    for (int columnNo = 1; columnNo < 8; columnNo++) {
      var columnPositions = new ArrayList<Boolean>();

      for (int rownNo = 0; rownNo < positions.length; rownNo++) {
        columnPositions.add(positions[rownNo][columnNo]);
      }

      // then
      assertThat(columnPositions).haveExactly(10, new Condition<>(v -> v, "number"));
      assertThat(columnPositions).haveExactly(8, new Condition<>(v -> !v, "blank"));
    }
  }

  @Test
  public void countOfNumbersInNineColumn() {
    // given
    var columnPositions = new ArrayList<Boolean>();

    // when
    var positions = generator.getPositions();
    for (int rownNo = 0; rownNo < positions.length; rownNo++) {
      columnPositions.add(positions[rownNo][8]);
    }

    // then
    assertThat(columnPositions).haveExactly(11, new Condition<>(v -> v, "number"));
    assertThat(columnPositions).haveExactly(7, new Condition<>(v -> !v, "blank"));
  }

  private int positionTypeCount(boolean[] row, boolean isNumber) {
    return (int) IntStream.range(0, row.length)
        .mapToObj(idx -> row[idx])
        .filter(cell -> cell == isNumber)
        .count();
  }

  private boolean hasThreeBlanks(boolean[][] positions, int ticketNo, int columnNo) {
    return !positions[ticketNo][columnNo]
        && !positions[ticketNo+1][columnNo] && !positions[ticketNo+2][columnNo];
  }
}