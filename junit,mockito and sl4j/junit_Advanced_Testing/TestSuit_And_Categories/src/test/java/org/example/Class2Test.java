package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Class2Test {
    @Test
    public void test2(){
        Class1 c2=new Class1();
        int result=c2.subNumber(300,100);
        assertEquals(200,result);
    }
}
