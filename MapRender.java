package lifeSimulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

//klasa jest odpowiedzialna za rysowanie mapy
public class MapRender extends JPanel {
    public RectangularMap map;
    public SimulationEngine simulation;
    public MapRender(RectangularMap map, SimulationEngine simulation) {
        this.map = map;
        this.simulation = simulation;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = this.getWidth();
        int height = this.getHeight();
        int widthScale = Math.round(width / (float) map.getWidth()); //obliczanie rozmiarow jednego "piksela" na mapie
        int heightScale = Math.round(height / (float) map.getHeight());
        BufferedImage grassIcon=null;
        BufferedImage animalIcon=null;
        BufferedImage steppeIcon=null;
        BufferedImage jungleIcon=null;
        BufferedImage mostCommonGenotypeAnimal = null;
        try { //import ikon mapy
            steppeIcon = ImageIO.read(new File("src\\lifeSimulator\\icons\\steppe.png"));
            jungleIcon = ImageIO.read(new File("src\\lifeSimulator\\icons\\jungle.png"));
            grassIcon = Grass.getIcon();
            animalIcon = Animal.getIcon();
            mostCommonGenotypeAnimal = ImageIO.read(new File("src\\lifeSimulator\\icons\\animalWithMostCommonGenotype.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ta kolejnosc rysowania elementow umożliwia brak zasłaniania się elementów, tzn zwierzęta nie będą zasłonięte trawą ani stepem i dżunglą

        //rysowanie stepu
        g.drawImage(steppeIcon, 0,0,width,height,null);

        //rysowanie dzungli
                g.drawImage(jungleIcon, map.getJungleLeftTopX() * widthScale,
                map.getJungleLeftTopY() * heightScale,
                map.getJunglewidth() * widthScale,
                map.getJungleheight() * heightScale,null);
        //rysowanie trawy
        for (Grass grass : map.getGrasses().values()) {
            int y = grass.getPosition().getY() * heightScale;
            int x = grass.getPosition().getX() * widthScale;
            g.drawImage(grassIcon, x,y,widthScale,heightScale,null);
        }
        //rysowanie zwierząt
        String mostCommonGenotype = map.getStats().mostCommonGenotype().toString();
        for (LinkedList<Animal> list : map.getAnimals().values()) {
                Animal animal = list.getFirst();
                int y = animal.getPosition().getY() * heightScale;
                int x = animal.getPosition().getX() * widthScale;
                if(simulation.isStopped() && animal.getGenes().toString().equals(mostCommonGenotype)) g.drawImage(mostCommonGenotypeAnimal,x,y,widthScale,heightScale, null);
                else g.drawImage(animalIcon,x,y,widthScale,heightScale, null);
        }




        }
}
