package lifeSimulator;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, modified by Michał Skorek
 *
 */
public interface IWorldMap {

    /**
     * Place a animal on the map.
     *
     * @param animal
     *            The animal to place on the map.
     * @return True if the animal was placed.
     */
    boolean place(Animal animal);

    /**
     * Place a grass on the map.
     *
     * @param grass
     *            The grass to place on the map.
     * @return True if the grass was placed. The grass cannot be placed if the map is already occupied.
     */
    boolean place(Grass grass);
    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */

    boolean isOccupied(Vector2D position);


}