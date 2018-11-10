package com.jdanisik;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParameterizedTest {

//  @Parameters(name="{index}: sort[{0}]={1}")
  @Parameters(name="{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
      { "Usecase 1", 5, 25 },
      { "Usecase 2", 3, 9 },
    });
  }

  private final int input;
  private final int expectedResult;

  public ParameterizedTest(
      String name,
      int input,
      int expectedResult
      ) {
    this.input = input;
    this.expectedResult = expectedResult;
  }

  @Test
  public void testPow() {
    assertEquals(expectedResult, (int)Math.pow(input, 2));
  }

}
