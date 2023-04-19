package com.nuxsoftware.bingo.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.nuxsoftware.bingo.core.Strip;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StripTest {

  Strip strip;

  @BeforeEach
  void setUp() {
    strip = new Strip(stripMatrix);
  }

  @Test
  public void stripShouldHaveTickets() {
    //when
    var tickets = strip.getTickets();

    // then
    assertThat(tickets).as("Strip should have six tickets").hasSize(6);
  }

  @Test
  public void numbersInTicketColumnsAreOrderedASC() {
    //when
    var tickets = strip.getTickets();

    // then
    for (var ticket: tickets) {
      for (int columnNo = 0; columnNo < 9; columnNo++ ) {
        assertThat(readColumn(0, ticket.getData()))
            .as("Each ticket column should be sorted ASC").isSorted();
      }
    }
  }

  private List<Integer> readColumn(int columnNo, int[][] input) {
    var column = new ArrayList<Integer>();
    for (int rowNo = 0; rowNo < input.length; rowNo++) {
      if (input[rowNo][columnNo] != -1) {
        column.add(input[rowNo][columnNo]);
      }
    }
    return column;
  }

  int[][] stripMatrix = {
      {9, -1, 21, 30, -1, 55, 65, -1, -1},
      {2, 12, 27, 31, -1, -1, 62, -1, -1},
      {7, -1, 20, 33, -1, -1, 68, -1, 88},

      {-1, -1, 26, 38, -1, 59, 63, -1, 86},
      {8, 13, 22, -1, -1, 57, -1, -1, 83},
      {-1, 18, -1, 36, -1, 54, 61, -1, 89},

      {5, -1, 29, -1, 41, -1, 66, 77, -1},
      {-1, 19, -1, 34, 43, 52, 69, -1, -1},
      {-1, 11, -1, 35, -1, 51, 64, 72, -1},

      {-1, -1, 23, 37, 46, 50, 60, -1, -1},
      {-1, 16, 28, -1, -1, 56, 67, 76, -1},
      {-1, -1, 24, 39, 40, -1, -1, 79, 90},

      {-1, 15, -1, -1, 47, 58, -1, 74, 82},
      {-1, 17, -1, -1, 45, 53, -1, 73, 81},
      {3, -1, 25, -1, 42, -1, -1, 75, 85},

      {6, -1, -1, 32, 48, -1, -1, 70, 80},
      {4, 14, -1, -1, 49, -1, -1, 71, 84},
      {1, 10, -1, -1, 44, -1, -1, 78, 87}};
}
