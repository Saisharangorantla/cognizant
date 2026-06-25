import org.junit.Test;

import static org.junit.Assert.*;

public class AssertionsTest {
    @Test
    public void testAssertions() {
        org.junit.Assert.assertEquals(5, 2+3);
        assertTrue(4 > 2);
        assertFalse(2 > 4);
        assertNotNull("something");
        

        System.out.print("All assertions passed successfully.");
    }
}
