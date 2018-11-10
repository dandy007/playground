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
			{ 3, 9 }, 
			{ 5, 25 }
		});
	}

	private final int input;
	private final int expectedResult;

	public ParameterizedTest(
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
