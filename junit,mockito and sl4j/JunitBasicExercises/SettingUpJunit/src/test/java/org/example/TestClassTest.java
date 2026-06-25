package org.example;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestClassTest {
    @Test
    public void test1(){
        TestClass testClass = new TestClass();
        boolean res = testClass.isEven(4);
        Assertions.assertEquals(true, res);
        System.out.print("Test 1 passed");

    }
    @Test
    public void test2(){
        TestClass testClass = new TestClass();
        boolean res = testClass.isEven(5);
        Assertions.assertEquals(false, res);
        System.out.print("Test 2 passed");
    }

}
