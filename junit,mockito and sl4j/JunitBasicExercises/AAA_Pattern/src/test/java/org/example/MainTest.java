package org.example;

import org.example.Main;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MainTest {

    private Main main;

    @Before
    public void setUp() {
        System.out.println("Setting up test environment...");
        main = new Main();
    }

    @After
    public void tearDown() {
        System.out.println("Cleaning up test environment...");
        main = null;
    }

    @Test
    public void testAdd() {

        // Arrange
        int a = 10;
        int b = 20;

        // Act
        int result = main.add(a, b);

        // Assert
        Assert.assertEquals(30, result);
    }

    @Test
    public void testMultiply() {

        // Arrange
        int a = 5;
        int b = 4;

        // Act
        int result = main.multiply(a, b);

        // Assert
        Assert.assertEquals(20, result);
    }
}