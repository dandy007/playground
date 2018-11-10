package com.jdanisik;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParameterizedTest {

  @BeforeClass
  public static void beforeClass() {
    System.out.println("beforeClass");
  }

  @Before
  public void before() {
    System.out.println("before");
  }

  @After
  public void after() {
    System.out.println("after");
  }

  @AfterClass
  public static void afterClass() {
    System.out.println("afterClass");
  }

//  @Parameters(name="{index}: sort[{0}]={1}")
  @Parameters(name="{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
      { "Usecase 1", 5, 25 },
      { "Usecase 2", 3, 9 },
    });
  }

  private final String name;
  private final int input;
  private final int expectedResult;

  public ParameterizedTest(
      String name,
      int input,
      int expectedResult
      ) {
    this.name = name;
    this.input = input;
    this.expectedResult = expectedResult;
  }

  @Test
  public void testPow() {
    System.out.println(name);
    assertEquals(expectedResult, (int)Math.pow(input, 2));
  }

}
