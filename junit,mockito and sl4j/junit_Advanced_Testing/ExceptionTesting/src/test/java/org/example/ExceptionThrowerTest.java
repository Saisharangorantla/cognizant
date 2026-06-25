package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionThrowerTest {

    @Test
    public void test1() {

        ExceptionThrower e = new ExceptionThrower();

        ArithmeticException ex =
                assertThrows(ArithmeticException.class,
                        () -> e.throwException());

        assertEquals("Exception occured", ex.getMessage());
    }
}