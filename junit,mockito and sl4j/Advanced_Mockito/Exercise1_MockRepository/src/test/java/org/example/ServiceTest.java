package org.example; 
import org.junit.jupiter.api.Test; 
import static org.mockito.Mockito.*; 
import static org.junit.jupiter.api.Assertions.*; 
public class ServiceTest{ 
    @Test 
    void testServiceWithMockRepository(){ 
        Repository repo=mock(Repository.class); 
        when(repo.getData()).thenReturn("Mock Data"); 
        Service s=new Service(repo); 
        assertEquals("Processed Mock Data",s.processData());
    }
}