package org.example;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
@Suite
@SelectClasses({Class1Test.class, Class2Test.class})
public class CombineTest {

    public void testAllTests() {
        // This method will run all tests in Class1Test and Class2Test
    }


}
