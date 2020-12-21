package lifeSimulator;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class StatRender extends JPanel {
    public RectangularMap map;
    public SimulationEngine simulation;
    DecimalFormat displayFormat = new DecimalFormat("#.##");
    public StatRender(RectangularMap map, SimulationEngine simulation){
        this.map=map;
        this.simulation=simulation;
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        int width = this.getWidth();
        int height = this.getHeight(); //38 is toolbar size
        g.setColor(new Color(189, 187, 187));
        g.fillRect(0,0,width,height);
        g.setColor(new Color(0,0,0));
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Dzień "+ map.getDay(), 50, 50);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Liczba zwierząt na mapie: " + map.getStats().numberOfAnimals(),10,80);
        g.drawString("Liczba roślin na mapie: "+map.getStats().numberOfGrasses(),10,110);
        g.drawString("Średnia liczba dzieci dla żyjących zwierząt: " + displayFormat.format(map.getStats().averageChildrenNumber()), 10,140);
        g.drawString("Średni wiek żyjących zwierząt: " + displayFormat.format(map.getStats().averageAge()), 10,170);
        g.drawString("Średni wiek życia dla martwych zwierząt: "+ displayFormat.format(map.getStats().averageDeadAge()),10,200);
        g.drawString("Średnia energia żyjących zwierząt: "+ displayFormat.format(map.getStats().averageEnergy()),10,230);
        g.drawString("Początkowa energia zwierząt: "+Animal.getStartEnergy(), 10,260);
        g.drawString("Rozmiar mapy:\twysokość: "+map.getHeight()+"\t szerokość: "+map.getWidth(),10,290);
        g.drawString("Energia tracona każdego dnia: "+map.getDayEnergyCost(),10,320);
        g.drawString("Zysk energii po jedzeniu: "+Grass.getEatProfit(),10,350);
        g.drawString("Najczęściej występujący genotyp:", 10, 380);
        g.drawString(map.getStats().mostCommonGenotype().toString(), 10, 410);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Zwierzęta z tym genotypem są zaznaczone żółtym obrysem po zatrzymaniu symulacji", 10, 440);
        JButton stop = new JButton("Zatrzymaj/uruchom");
        stop.setSize(50,10);
        stop.setLocation(20,700);
        stop.setVisible(true);

    }

}
