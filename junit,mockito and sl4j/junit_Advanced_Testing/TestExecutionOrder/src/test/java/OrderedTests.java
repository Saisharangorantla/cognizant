import org.example.Tests;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)

public class OrderedTests {
    @Test
    @Order(3)
    public void test1(){
        Tests t=new Tests();
        assert t.add(2,3)==5;
        System.out.println(" test 1 passed successfully.");
    }
    @Order(2)
    @Test
    public void test2(){
        Tests t=new Tests();
        assert t.sub(5,3)==2;
        System.out.println(" test 2 passed successfully.");
    }
    @Test
    @Order(1)
    public void test3(){
            Tests t=new Tests();
            assert t.mul(2,3)==6;
            System.out.println(" test 3 passed successfully.");

    }
    @Test
    @Order(4)
    public void test4(){
        Tests t=new Tests();
        assert t.div(6,3)==2;
        System.out.println(" test 4 passed successfully.");
    }

}
