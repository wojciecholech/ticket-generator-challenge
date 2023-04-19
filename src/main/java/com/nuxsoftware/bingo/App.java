package com.nuxsoftware.bingo;

import com.nuxsoftware.bingo.core.StripFactory;

public class App {

  public static void main(String[] args) {
    var strip = new StripFactory().create();
    System.out.println(strip);
  }
}
