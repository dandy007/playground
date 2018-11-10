package com.jdanisik;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class Tests {

  @Rule
  public TestName name = new TestName();

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

  @Test(expected=NumberFormatException.class)
  public void testExpected() {
    System.out.println(name.getMethodName());

    Long.valueOf("a123"); // throw number format exception
  }

}
