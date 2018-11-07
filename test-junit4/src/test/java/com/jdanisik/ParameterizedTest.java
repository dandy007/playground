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

	@Parameters
  public static Collection<Object[]> data() {
      return Arrays.asList(new Object[][] {
              { 1, 1 }, { 2, 4 }
      });
  }
	
  private final int first;
  private final int second;

  public ParameterizedTest(
    int first, 
    int second
  ) {
    this.first = first;
    this.second = second;
  }

  @Test
  public void shouldReturnCorrectSum() {
      assertEquals(second, first * first);
  }

}
