package lifeSimulator;

import org.junit.Test;

import static org.junit.Assert.*;

public class Vector2DTest {
    Vector2D v1 = new Vector2D(2,4);
    Vector2D v2 = new Vector2D(3,5);
    @Test
    public void getX() {
        assertEquals(v1.getX(), 2);
        assertEquals(v2.getX(), 3);
    }

    @Test
    public void getY() {
        assertEquals(v1.getY(), 4);
        assertEquals(v2.getY(), 5);
    }

    @Test
    public void testToString() {
        assertEquals(v1.toString(), "(2,4)");
        assertEquals(v2.toString(), "(3,5)");

    }

    @Test
    public void precedes() {
        assertTrue(v1.precedes(v2));
        assertFalse(v2.precedes(v1));
    }

    @Test
    public void follows() {
        assertFalse(v1.follows(v2));
        assertTrue(v2.follows(v1));
    }

    @Test
    public void upperRight() {
        assertEquals(v1.upperRight(v2), v2);
        assertEquals(v2.upperRight(v1), v2);
    }

    @Test
    public void lowerLeft() {
        assertEquals(v1.lowerLeft(v2), v1);
        assertEquals(v2.lowerLeft(v1), v1);

    }

    @Test
    public void add() {
        assertEquals(v1.add(v2), new Vector2D(5,9));
        assertEquals(v2.add(v1), new Vector2D(5,9));
    }

    @Test
    public void subtract() {
        assertEquals(v1.subtract(v2), new Vector2D(-1,-1));
        assertEquals(v2.subtract(v1), new Vector2D(1,1));
    }

    @Test
    public void opposite() {
        assertEquals(v1.opposite(), new Vector2D(-2,-4));
        assertEquals(v2.opposite(), new Vector2D(-3,-5));
    }

    @Test
    public void testEquals() {
        assertNotEquals(v1, v2);
        assertNotEquals(v2, v1);
        assertEquals(v1, v1);
        assertEquals(v2, v2);
    }
}