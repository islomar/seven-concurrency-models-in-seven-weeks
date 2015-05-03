/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.util.Date;

class DateFormatBug {
  public static void main(String[] args) throws Exception {
    final DateParser parser = new DateParser();
    final String dateString = "2012-01-01";
    final Date dateParsed = parser.parse(dateString);

    class ParsingThread extends Thread {
      public void run() {
        try {
          while(true) {
            Date d = parser.parse(dateString);
            if (!d.equals(dateParsed)) {
              System.out.println("Expected: "+ dateParsed +", got: "+ d);
            }
          }
        } catch (Exception e) {
          System.out.println("Caught: "+ e);
        }
      }
    }

    Thread t1 = new ParsingThread();
    Thread t2 = new ParsingThread();
    t1.start();
    t2.start();
  }
}