package bikescheme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class EventTest {

    @Test
    public void test1() {
        assertEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:00,C,i,m, a, b, c, d") );
    }
    @Test
    public void test2() {
        assertNotEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:00,C,i,m, a, b, c, e") );
    }
    
    @Test
    public void test3() {
        assertNotEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:00,C,i,m, a, b, e, d") );
    }
    
    @Test
    public void test4() {
        assertNotEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:00,C,i,m, a, e, c, d") );
    }
    
    @Test
    public void test5() {
        assertNotEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:00,C,i,m, e, b, c, d") );
    }
    
    @Test
    public void test6() {
        assertNotEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:00,C,i,e, a, b, c, d") );
    }
    
    @Test
    public void test7() {
        assertNotEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:00,C,e,m, a, b, c, d") );
    }
    
    @Test
    public void test8() {
        assertNotEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:00,A,i,m, a, b, c, d") );
    }
    
    @Test
    public void test9() {
        assertNotEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("1 00:01,C,i,m, a, b, c, d") );
    }
    
    @Test
    public void test10() {
        assertNotEquals(
                new Event("1 00:00,C,i,m, a, b, c, d"),
                new Event("2 00:00,C,i,m, a, b, c, d") );
    }
    

}
