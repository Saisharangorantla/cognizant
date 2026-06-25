package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.annotation.processing.SupportedAnnotationTypes;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    @Test
    public void test1(){
        ExternalApi mockobj= Mockito.mock(ExternalApi.class);
        Mockito.when(mockobj.getData()).thenReturn("Hello World");
        Service service=new Service(mockobj);
        String result=service.getData();
        Mockito.verify(mockobj).getData();
    }
}