package com.nuxsoftware.bingo.core;

import static com.nuxsoftware.bingo.core.Strip.TOTAL_ROWS;
import static com.nuxsoftware.bingo.core.Ticket.TICKET_COLUMNS;

import java.util.Random;

public class StripFactory {

  public Strip create() {
    var random = new Random(System.currentTimeMillis());
    var positions = new StripPositionGenerator(random).getPositions();
    var table = new StripNumberGenerator(random).insert(positions, new int[TOTAL_ROWS][TICKET_COLUMNS]);

    return new Strip(table);
  }
}
