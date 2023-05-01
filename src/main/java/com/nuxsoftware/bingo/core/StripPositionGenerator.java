package com.nuxsoftware.bingo.core;

import static com.nuxsoftware.bingo.core.Strip.STRIP_ROWS;
import static com.nuxsoftware.bingo.core.Strip.STRIP_TICKETS;
import static com.nuxsoftware.bingo.core.Ticket.BLANK_SPACES;
import static com.nuxsoftware.bingo.core.Ticket.NUMBER_SPACES;
import static com.nuxsoftware.bingo.core.Ticket.TICKET_COLUMNS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Allow to generate matrix of number positions for Bingo strip.
 * True value means number, False blank space.
 */
public class StripPositionGenerator {

  private static final int MAX_ITERATIONS = 4;
  private final Random random;
  private final boolean[][] positions;

  public StripPositionGenerator(Random random) {
    this.random = random;
    positions = generate();
  }

  public boolean[][] getPositions() {
    return positions;
  }

  private boolean[][] generate() {
    var isValid = false;
    var iterations = 0;

    var positions = generateBase(new boolean[STRIP_ROWS][TICKET_COLUMNS]);

    while (!isValid && iterations++ < MAX_ITERATIONS) {
      isValid = new PositionSwapper().validateAndSwap(positions);
    }

    return isValid ? positions : generate();
  }

  private boolean[][] generateBase(boolean[][] positions) {
    var numberCounter = new HashMap<Integer, Integer>();
    var blankCounter = new HashMap<Integer, Integer>();

    for (int row = 0; row < STRIP_ROWS; row++) {
      var cells = randomRow(numberCounter, blankCounter);
      for (int column = 0; column < TICKET_COLUMNS; column++) {
        (cells.get(column) ? numberCounter : blankCounter).compute(column, (k, v) -> (v == null) ? 1 : v + 1);
        positions[row][column] = cells.get(column);
      }

    }
    return positions;
  }

  private List<Boolean> randomRow(Map<Integer, Integer> numbers, Map<Integer, Integer> blanks) {
    var outOfRangeFields = new TreeMap<Integer, Boolean>();
    outOfRangeFields.putAll(outOfRange(numbers, false));
    outOfRangeFields.putAll(outOfRange(blanks, true));

    var row = randomRowPositions(NUMBER_SPACES - outOfRange(blanks, true).size(),
        BLANK_SPACES - outOfRange(numbers, false).size());
    outOfRangeFields.forEach(row::add); // Add elements in sequence

    return row;
  }

  private Map<Integer, Boolean> outOfRange(Map<Integer, Integer> numbers, boolean isBlank) {
    return numbers.entrySet().stream()
        .filter(entry -> entry.getValue() >= (isBlank ? STRIP_ROWS - Ticket.COLUMN_RANGES.get(entry.getKey()).size() : Ticket.COLUMN_RANGES.get(entry.getKey()).size()))
        .collect(Collectors.toMap(Map.Entry::getKey, v -> isBlank));
  }

  private List<Boolean> randomRowPositions(int numbers, int blanks) {
    var row = new ArrayList<Boolean>(TICKET_COLUMNS);
    IntStream.range(0, numbers+blanks)
        .forEach(i -> row.add(i < numbers));

    Collections.shuffle(row, random);
    return row;

  }

  /**
   * Eliminates three blanks in the column for the ticket by swapping cells in the column
   * and in the row so the number of blanks/numbers is the same for column and row.
   */
  private static class PositionSwapper {

    private boolean validateAndSwap(boolean[][] positions) {
      var isValid = true;

      for (int columnNo = 0; columnNo < TICKET_COLUMNS; columnNo++) {
        for (int ticketNo = 0; ticketNo < STRIP_TICKETS; ticketNo++) {
          if (hasThreeBlanks(positions, ticketNo * 3, columnNo)) {
            isValid = false;
            swap(positions, columnNo, ticketNo * 3, (ticketNo * 3)+1);
          }
        }
      }

      return isValid;
    }

    private void swap(boolean[][] positions, int columnNo, int rowNo, int nextRowNo) {
      nextRowNo = nextRowNo < STRIP_ROWS ? nextRowNo : 0;
      if (positions[rowNo][columnNo] == positions[nextRowNo][columnNo]) {
        swap(positions, columnNo, rowNo, nextRowNo + 1);
        return;
      }

      swapCell(positions, columnNo, rowNo, nextRowNo);
      swapRow(positions, columnNo+1, rowNo, nextRowNo);
    }

    private void swapRow(boolean[][] positions, int nextColumn, int fromRow, int toRow) {
      nextColumn = nextColumn < TICKET_COLUMNS ? nextColumn : 0;
      if (!positions[fromRow][nextColumn] || positions[toRow][nextColumn]) {
        swapRow(positions, nextColumn+1, fromRow, toRow);
        return;
      }

      swapCell(positions, nextColumn, fromRow, toRow);
    }

    private boolean hasThreeBlanks(boolean[][] positions, int ticketNo, int columnNo) {
      return !positions[ticketNo][columnNo]
          && !positions[ticketNo+1][columnNo] && !positions[ticketNo+2][columnNo];
    }

    private void swapCell(boolean[][] positions, int column, int fromRow, int toRow) {
      var fromCell = positions[fromRow][column];
      positions[fromRow][column] = positions[toRow][column];
      positions[toRow][column] = fromCell;
    }
  }
}