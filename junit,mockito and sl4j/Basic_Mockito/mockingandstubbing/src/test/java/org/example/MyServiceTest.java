package org.example;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MyServiceTest {
    @Test
    public void testExternalApi() {
        // 1) Create a mock for the external API
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        // 2) Stub the method to return a predefined value
        when(mockApi.getData()).thenReturn("Mock Data");

        // 3) Inject mock into service and exercise
        MyService service = new MyService(mockApi);
        String result = service.fetchData();

        assertEquals("Mock Data", result);
        // verify that the mock was called once
        verify(mockApi, times(1)).getData();
    }
}

