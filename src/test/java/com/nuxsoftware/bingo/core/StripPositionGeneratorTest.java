package com.nuxsoftware.bingo.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.nuxsoftware.bingo.core.StripPositionGenerator;
import java.util.ArrayList;
import java.util.Random;
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
  public void countOfBlankSpacesNeedToBeFive() {
    // given
    var positions = generator.getPositions();

    // then
    for (int rownNo = 0; rownNo < positions.length; rownNo++) {
      assertThat(positions[rownNo]).containsOnly(false, false, false, false, true, true, true, true, true);
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

}