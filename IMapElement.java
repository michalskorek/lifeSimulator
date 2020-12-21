package lifeSimulator;

public interface IMapElement {
    /**
     * Checks if element is equal given object.
     *
     * @param object
     *
     * @return True if given elements are equal.
     */
    boolean equals(Object o);

    /**
     * Returns the current position of element on map.
     *
     *
     * @return Position of the element.
     */
    Vector2D getPosition();

}
