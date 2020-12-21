package lifeSimulator;

public enum MoveDirection {
    FORWARD(0),RIGHTFORWARD(1), RIGHT(2),RIGHTBACKWARD(3),BACKWARD(4),LEFT(5),LEFTBACKWARD(6),LEFTFORWARD(7);

    private final int level;
    public String toString(){
    return switch(this){
        case FORWARD -> "Naprzód";
        case RIGHTFORWARD -> "Prosto w prawo";
        case RIGHT -> "W prawo";
        case RIGHTBACKWARD -> "Do tyłu w prawo";
        case BACKWARD -> "Do tyłu";
        case LEFTBACKWARD -> "Do tyłu w lewo";
        case LEFT -> "W lewo";
        case LEFTFORWARD -> "Prosto w lewo";
    };
    }

    MoveDirection(int level)
    {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }


}