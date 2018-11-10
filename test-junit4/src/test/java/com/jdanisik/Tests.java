package com.jdanisik;

import org.junit.Test;

public class Tests {

  @Test(expected=NumberFormatException.class)
  public void testExpected() {
    Long.valueOf("a123"); // throw number format exception
  }

}
