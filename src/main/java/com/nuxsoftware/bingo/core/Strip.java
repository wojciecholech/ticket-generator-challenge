package com.nuxsoftware.bingo.core;

import static com.nuxsoftware.bingo.core.Ticket.TICKET_COLUMNS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Strip {

  public static final int TOTAL_ROWS = 18;

  private final int[][] table;

  Strip(int[][] table) {
    this.table = table;
  }

  public List<Ticket> getTickets() {
    return split(table);
  }

  @Override
  public String toString() {
    return getTickets().stream()
        .map(Ticket::toString)
        .collect(Collectors.joining("\n","---------------------\nSTRIP\n---------------------\n", ""));
  }

  private List<Ticket> split(int[][] data) {
    var tickets = new ArrayList<Ticket>();

    for (int ticketNo = 0; ticketNo < TOTAL_ROWS; ticketNo = ticketNo + 3) {
      var ticket = orderColumnNumbers(Arrays.copyOfRange(data, ticketNo, ticketNo + 3),
          new int[3][TICKET_COLUMNS]);
      tickets.add(new Ticket(ticket));
    }

    return tickets;
  }

  private int[][] orderColumnNumbers(int[][] input, int[][] output) {
    for (int columnNo = 0; columnNo < input[0].length; columnNo++) {
      var column = readColumn(columnNo, input);
      column.sort(null);
      writeColumn(columnNo, column, input, output);
    }
    return output;
  }

  private int[][] writeColumn(int columnNo, List<Integer> numbers, int[][] input, int[][] output) {
    var iterator = numbers.listIterator();
    for (int rowNo = 0; rowNo < input.length; rowNo++) {
      output[rowNo][columnNo] = input[rowNo][columnNo] != -1 ? iterator.next() : -1;
    }
    return output;
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
}
