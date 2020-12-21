package lifeSimulator;

public enum MapDirection {
    NORTH,SOUTH,WEST,EAST,NORTHWEST,NORTHEAST,SOUTHWEST,SOUTHEAST;
    public String toString(){
        return switch(this){
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            case WEST -> "Zachód";
            case EAST -> "Wschód";
            case NORTHWEST -> "Północny zachód";
            case NORTHEAST -> "Północny wschód";
            case SOUTHWEST -> "Południowy zachód";
            case SOUTHEAST -> "Południowy wschód";
        };
    }
    public MapDirection next(){ //nastepny kierunek zgodny z ruchem zegara
        return switch(this){
            case NORTH -> NORTHEAST;
            case SOUTH -> SOUTHWEST;
            case WEST -> NORTHWEST;
            case EAST -> SOUTHEAST;
            case NORTHWEST ->NORTH;
            case NORTHEAST ->EAST;
            case SOUTHWEST -> WEST;
            case SOUTHEAST ->SOUTH;
        };
    }
    public MapDirection previous(){ //nastepny kierunek przeciwny do ruchu zegara
        return switch(this){
            case NORTH -> NORTHWEST;
            case SOUTH -> SOUTHEAST;
            case WEST -> SOUTHWEST;
            case EAST -> NORTHEAST;
            case NORTHWEST -> WEST;
            case NORTHEAST ->NORTH;
            case SOUTHWEST -> SOUTH;
            case SOUTHEAST ->EAST;
        };
    }
    public Vector2D toUnitVector(){ //zamiana kierunku na "wektor jednostkowy"
        return switch(this){
            case NORTH -> new Vector2D(0,1);
            case SOUTH -> new Vector2D(0,-1);
            case WEST -> new Vector2D(-1,0);
            case EAST -> new Vector2D(1,0);
            case NORTHWEST -> new Vector2D(-1,1);
            case NORTHEAST -> new Vector2D(1,1);
            case SOUTHWEST -> new Vector2D(-1,-1);
            case SOUTHEAST -> new Vector2D(1,-1);
        };
    }

}
