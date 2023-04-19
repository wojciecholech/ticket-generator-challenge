package com.nuxsoftware.bingo.core;

import static com.nuxsoftware.bingo.core.Strip.TOTAL_ROWS;
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

/**
 * Allow to generate matrix of number positions for Bingo strip.
 * True value means number, False blank space.
 */
public class StripPositionGenerator {

  private final Random random;
  private final boolean[][] positions = new boolean[18][9];

  public StripPositionGenerator(Random random) {
    this.random = random;
    generate();
  }

  public boolean[][] getPositions() {
    return positions;
  }

  private void generate() {
    var numbers = new HashMap<Integer, Integer>();
    var blanks = new HashMap<Integer, Integer>();

    for (int columnNo = 0; columnNo < TOTAL_ROWS; columnNo++) {
      var row = getRow(columnNo, numbers, blanks);

      for (int rowNo = 0; rowNo < TICKET_COLUMNS; rowNo++) {
        if (row.get(rowNo)) {
          numbers.compute(rowNo, (k, v) -> (v == null) ? 1 : v + 1);
        } else {
          blanks.compute(rowNo, (k, v) -> (v == null) ? 1 : v + 1);
        }

        positions[columnNo][rowNo] = row.get(rowNo);
      }
    }
  }

  private List<Boolean> getRow(int i, Map<Integer, Integer> numbers, Map<Integer, Integer> blanks) {
    var outOfRangeNumbers = outOfRange(numbers, false);
    var outOfRangeBlanks = outOfRange(blanks, true);

    var outOfRangeFields = new TreeMap<Integer, Boolean>();
    outOfRangeFields.putAll(outOfRangeNumbers);
    outOfRangeFields.putAll(outOfRangeBlanks);

    var row = randomRowPositions(i, NUMBER_SPACES - outOfRangeBlanks.size(), BLANK_SPACES - outOfRangeNumbers.size());
    outOfRangeFields.forEach(row::add);

    return row;
  }

  private Map<Integer, Boolean> outOfRange(Map<Integer, Integer> numbers, boolean isBlank) {
    return numbers.entrySet().stream()
        .filter(entry -> entry.getValue() >= (isBlank ? TOTAL_ROWS - Ticket.COLUMN_RANGES.get(entry.getKey()).size() : Ticket.COLUMN_RANGES.get(entry.getKey()).size()))
        .collect(Collectors.toMap(Map.Entry::getKey, v -> isBlank));
  }

  private List<Boolean> randomRowPositions(int rowNo, int numbers, int blanks) {
    var row = new ArrayList<Boolean>(TICKET_COLUMNS);
    
    for (int i = 0; i < numbers; i++) {
      row.add(true);
    }
    for (int i = 0; i < blanks; i++) {
      row.add(false);
    }

    Collections.shuffle(row, random);
    return row;
  }
}
