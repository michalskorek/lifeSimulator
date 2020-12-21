package lifeSimulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapDirectionTest {

    @org.junit.jupiter.api.Test
    void testToString() {

        assertEquals(MapDirection.NORTH.toString(), "Północ");
        assertEquals(MapDirection.SOUTH.toString(), "Południe");
        assertEquals(MapDirection.WEST.toString(), "Zachód");
        assertEquals(MapDirection.EAST.toString(), "Wschód");
    }

    @org.junit.jupiter.api.Test
    void next() {
        assertEquals(MapDirection.NORTH.next(), MapDirection.NORTHEAST);
        assertEquals(MapDirection.NORTHEAST.next(), MapDirection.EAST);
        assertEquals(MapDirection.EAST.next(), MapDirection.SOUTHEAST);
        assertEquals(MapDirection.SOUTHEAST.next(), MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH.next(), MapDirection.SOUTHWEST);
        assertEquals(MapDirection.SOUTHWEST.next(), MapDirection.WEST);
        assertEquals(MapDirection.WEST.next(), MapDirection.NORTHWEST);
        assertEquals(MapDirection.NORTHWEST.next(), MapDirection.NORTH);
    }

    @org.junit.jupiter.api.Test
    void previous() {
        assertEquals(MapDirection.NORTH.previous(),MapDirection.NORTHWEST);
        assertEquals(MapDirection.NORTHWEST.previous(),MapDirection.WEST);
        assertEquals(MapDirection.WEST.previous(),MapDirection.SOUTHWEST);
        assertEquals(MapDirection.SOUTHWEST.previous(),MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH.previous(),MapDirection.SOUTHEAST);
        assertEquals(MapDirection.SOUTHEAST.previous(),MapDirection.EAST);
        assertEquals(MapDirection.EAST.previous(),MapDirection.NORTHEAST);
        assertEquals(MapDirection.NORTHEAST.previous(),MapDirection.NORTH);
    }

    @org.junit.jupiter.api.Test
    void toUnitVector() {
        assertEquals(MapDirection.NORTH.toUnitVector(),new Vector2D(0,1));
        assertEquals(MapDirection.NORTHWEST.toUnitVector(),new Vector2D(-1,1));
        assertEquals(MapDirection.WEST.toUnitVector(),new Vector2D(-1,0));
        assertEquals(MapDirection.SOUTHWEST.toUnitVector(),new Vector2D(-1,-1));
        assertEquals(MapDirection.SOUTH.toUnitVector(),new Vector2D(0,-1));
        assertEquals(MapDirection.SOUTHEAST.toUnitVector(),new Vector2D(1,-1));
        assertEquals(MapDirection.EAST.toUnitVector(),new Vector2D(1,0));
        assertEquals(MapDirection.NORTHEAST.toUnitVector(),new Vector2D(1,1));
    }

}