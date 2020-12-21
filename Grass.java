package lifeSimulator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Grass implements IMapElement  {
    private final Vector2D position;
    private IWorldMap map;
    private static int eatProfit;

    //getters
    public static int getEatProfit() {
        return eatProfit;
    }

    public Vector2D getPosition(){
        return this.position;
    }

    public static BufferedImage getIcon() throws IOException {
        return  ImageIO.read(new File("src\\lifeSimulator\\icons\\grass.png"));
    }

    //setters
    public static void setEatProfit(int eatProfit) {
        Grass.eatProfit = eatProfit;
    }

    //constructor
    public Grass(RectangularMap map, Vector2D position){
        this.map=map;
        this.position=position;
        fixPosition();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grass grass = (Grass) o;
        return Objects.equals(position, grass.position) &&
                Objects.equals(map, grass.map);
    }

    //naprawa pozycji na mapie
    public Vector2D fixPosition(){
        int height=((RectangularMap)map).getHeight();
        int width=((RectangularMap)map).getWidth();
        int x= position.getX();
        int y= position.getY();
        if(x<0) x = width+x;
        if(y<0) y = height+y;
        return new Vector2D(x%width, y%height);
    }
    @Override
    public int hashCode() {
        return Objects.hash(position, map);
    }


    public String toString(){
        return "*";
    }


}
