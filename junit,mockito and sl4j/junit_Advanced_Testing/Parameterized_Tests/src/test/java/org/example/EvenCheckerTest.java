package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EvenCheckerTest {
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, 8, 10})
    public void test1(){
        EvenChecker evenChecker = new EvenChecker();
        assertTrue(evenChecker.isEven(2));
        System.out.println("test1 passed");
    }


}