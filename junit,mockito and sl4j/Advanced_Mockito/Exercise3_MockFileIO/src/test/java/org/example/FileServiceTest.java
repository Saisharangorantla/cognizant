package org.example;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FileServiceTest {
    @Test
    void test() {
        FileReaderService r = mock(FileReaderService.class);
        FileWriterService w = mock(FileWriterService.class);
        when(r.read()).thenReturn("Mock File Content");
        FileService s = new FileService(r, w);
        assertEquals("Processed Mock File Content", s.processFile());
        verify(w).write("Processed Mock File Content");
    }
}