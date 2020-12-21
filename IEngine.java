package lifeSimulator;

import java.io.IOException;

/**
 * The interface responsible for managing the moves of the animals.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 *
 */
public interface IEngine {
    /**
     * simulate of each day in simulation.
     *
     */
    void run() throws InterruptedException, IOException;
}