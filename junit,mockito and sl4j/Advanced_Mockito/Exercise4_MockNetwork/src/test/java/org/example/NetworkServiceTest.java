package org.example;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class NetworkServiceTest {
    @Test
    void test() {
        NetworkClient c = mock(NetworkClient.class);
        when(c.connect()).thenReturn("Mock Connection");
        assertEquals("Connected to Mock Connection", new NetworkService(c).connectToServer());
    }
}