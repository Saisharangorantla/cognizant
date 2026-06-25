package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Class1Test
{
    @Test
    public void test1(){
        Class1 c1=new Class1();
        int result=c1.addNumber(10,20);
        assertEquals(30,result);
    }

}