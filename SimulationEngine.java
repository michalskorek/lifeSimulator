package lifeSimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SimulationEngine extends JFrame implements IEngine, ActionListener, MouseListener {
    private final RectangularMap map;
    private final JFrame frame;
    private final int simulationNumber;
    private final MapRender render;
    private boolean stopped=false;
    private final JButton runStopButton;
    private FileWriter statsFile;
    private static int initialAnimalsNumber;

    public JFrame getFrame() {
        return frame;
    }


    public static void setInitialAnimalsNumber(int initialAnimalsNumber) {
        SimulationEngine.initialAnimalsNumber = initialAnimalsNumber;
    }


    public boolean isStopped() {
        return stopped;
    }
    public SimulationEngine(RectangularMap map, int number) throws IOException {
        this.map=map;
        this.simulationNumber=number;
        for(int i=0;i<initialAnimalsNumber;i++) this.map.place(new Animal(this.map,generateRandomVector(map.getWidth(),map.getHeight())));
        frame = new JFrame("Life Simulator");
        frame.setSize(1317, 840);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setIconImage(Animal.getIcon());
        render = new MapRender(map, this);
        StatRender stats = new StatRender(map, this);
        runStopButton=new JButton("Zatrzymaj");
        runStopButton.setBounds(20,700,200,30);
        runStopButton.addActionListener(this);
        render.setSize(800,800);
        stats.setSize(500,800);
        render.setLocation(500,0);
        stats.setLocation(0,0);
        render.addMouseListener(this);
        frame.add(runStopButton);
        frame.add(render);
        frame.add(stats);
        frame.setVisible(true);
        statsFile = new FileWriter("stats"+number+".txt");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        statsFile.append("\nStats for simulation started on ").append(dtf.format(now)).append("\n");
        statsFile.close();
    }
    public Vector2D generateRandomVector(int widthLimit, int heightLimit){
        Random generator= new Random();
        return new Vector2D(generator.nextInt(widthLimit),generator.nextInt(heightLimit));
    }
    @Override
    public void run() throws IOException {
            frame.validate();
            frame.repaint();
                if(!stopped) {
                    if(map.animalObservationEnded()){
                        runStopButton.doClick();
                        JFrame toDisplay = map.animalObservationResult();
                        toDisplay.setSize(400,138);
                        toDisplay.setLocation(render.getLocationOnScreen().x+200,render.getLocationOnScreen().y+300);
                        toDisplay.setVisible(true);
                    }
                    map.newDay();
                    statsFile = new FileWriter("stats"+simulationNumber+".txt",true);
                    statsFile.append(map.getStats().toString());
                    statsFile.close();
                }

    }



    @Override
    public void actionPerformed(ActionEvent e) { //obsluga przycisku uruchom/zatrzymaj
        if(e.getSource()==runStopButton){
            stopped=!stopped;
            if(runStopButton.getText().equals("Zatrzymaj")) runStopButton.setText("Uruchom");
            else runStopButton.setText("Zatrzymaj");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { //obsluga klikania na zwierze przy zatrzymanej symulacji
        if(e.getSource()==render){
            if (!isStopped()) return;
            int x = e.getX();
            int y = e.getY();
            int positionXOnMap = x / map.getWidthScale();
            int positionYOnMap = y / map.getHeightScale();
            LinkedList<Animal> list = map.getAnimals().get(new Vector2D(positionXOnMap, positionYOnMap));
            if (list==null) return;
            Animal animal = list.getLast();
            JFrame details = new JFrame("Opis zwierzęcia");
            try {
                details.setIconImage(Animal.getIcon());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            details.setResizable(false);
            details.setSize(500, 500);
            details.setLocation(render.getLocationOnScreen().x + 150, render.getLocationOnScreen().y + 100);
            details.setVisible(true);
            details.add(animal);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
