package lifeSimulator;

import java.util.Objects;

public class Vector2D {
    private int x;
    private int y;
    public Vector2D(int x, int y){
        this.x=x;
        this.y=y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public String toString(){
        return "("+x+","+y+")";
    }
    public boolean  precedes(Vector2D other)
    {
        return  (x<=other.x && y<=other.y);
    }
    public boolean follows(Vector2D other){
        return (x>=other.x && y>=other.y);
    }
    public Vector2D upperRight(Vector2D other){
        return new Vector2D(Math.max(this.x,other.x),Math.max(this.y,other.y));
    }
    public Vector2D lowerLeft(Vector2D other){
        return new Vector2D(Math.min(this.x,other.x),Math.min(this.y,other.y));
    }
    public Vector2D add(Vector2D other){
        return new Vector2D(this.x+other.x,this.y+other.y);
    }
    public Vector2D subtract(Vector2D other){
        return new Vector2D(this.x-other.x,this.y-other.y);
    }
    public Vector2D opposite(){
        return new Vector2D(-x,-y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.getX() &&
                y == vector2D.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
