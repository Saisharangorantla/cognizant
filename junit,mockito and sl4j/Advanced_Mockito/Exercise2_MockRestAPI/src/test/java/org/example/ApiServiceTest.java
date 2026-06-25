package org.example; 
import org.junit.jupiter.api.Test; 
import static org.mockito.Mockito.*; 
import static org.junit.jupiter.api.Assertions.*; 
public class ApiServiceTest{ 
    @Test void test(){ 
        RestClient c=mock(RestClient.class); 
        when(c.getResponse()).thenReturn("Mock Response"); 
        assertEquals("Fetched Mock Response",new ApiService(c).fetchData());
    }
}