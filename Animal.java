package lifeSimulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Animal extends JPanel implements IMapElement, Comparable<Animal>, ActionListener {
    private MapDirection direction;
    private Vector2D position;
    private IWorldMap map;
    private int age =0; //poczatkowy wiek zwierzecia
    private Genes genes;
    private static int startEnergy; //statyczna zmienna startowej energii dla wszystkich zwierząt
    private int energy=startEnergy; //domyslnie kazde zwierze ma makymalna energie, nowourodzone maja przypisywana inna wartosc w konstruktorze
    private JTextField observeDaysField; //pole tekstowe do wskazania liczby dni obserwacji
    private int observingBeginDay; // dzien od ktorego zwierze jest obserwowane
    private int observingDays; // dlugosc obserwacji
    private LinkedList<Animal> children = new LinkedList<>();
    private int descedantsBeforeObservation;
    private int childrenBeforeObservation;


    //getters
    public int getDescedantsBeforeObservation() {
        return descedantsBeforeObservation;
    }

    public int getChildrenBeforeObservation() {
        return childrenBeforeObservation;
    }

    public int getObservingBeginDay() {
        return observingBeginDay;
    }

    public int getObservingDays() {
        return observingDays;
    }

    public int getAge() {
        return age;
    }

    public static int getStartEnergy() {
        return startEnergy;
    }

    public Genes getGenes(){return genes;}

    public Vector2D getPosition(){
        return position;
    }

    public LinkedList<Animal> getChildren(){
        return children;
    }

    public int getEnergy(){return energy;}

    public static BufferedImage getIcon() throws IOException {
        return  ImageIO.read(new File("src\\lifeSimulator\\icons\\animal.png"));
    }

    //setters
    public void setAge(int age) {
        this.age = age;
    }
    public static void setStartEnergy(int startEnergy) {
        Animal.startEnergy = startEnergy;
    }
    public void setEnergy(int energy){
        this.energy=energy;
    }





    //constructors

    public Animal(IWorldMap map, Vector2D initialposition){ //konstruktor zwierzat poczatkowych na mapie
        this.direction=MapDirection.values()[new Random().nextInt(8)];
        this.map=map;
        this.position=initialposition;
        this.genes=new Genes();
        JPanel animalObservePanel = new JPanel();
        JLabel label = new JLabel("Przez ile dni obserwować zwierze:");
        observeDaysField = new JTextField(10);
        JButton button = new JButton("Obserwuj");
        label.setLabelFor(observeDaysField);
        animalObservePanel.add(label);
        animalObservePanel.add(observeDaysField);
        animalObservePanel.add(button);
        button.addActionListener(this);
        add(animalObservePanel);
    }
    public Animal(IWorldMap map, Animal parent1, Animal parent2){ //konstruktor dziecka i jednoczesne przypisanie mu pozycji obok rodzicow
        this.map=map;
        Random generator=new Random();
        LinkedList<MapDirection> nonOccupiedPositions= new LinkedList<>(Arrays.asList(MapDirection.values())); //lista wolnych miejsc dla dziecka

        for(MapDirection move: MapDirection.values()){ //sprawdzamy czy istnieje wolne miejsce na polach obok rodzicow
            if(map.isOccupied(fixPosition(parent1.getPosition().add(move.toUnitVector())))) nonOccupiedPositions.remove(move);
        }
        if(nonOccupiedPositions.size()==0) {             //jesli wszystkie pola dookola sa zajete, ustawiamy na pierwszym wylosowanym
            Vector2D randomUnitVector = MapDirection.values()[generator.nextInt(8)].toUnitVector();
            this.position= fixPosition(parent1.getPosition().add(randomUnitVector));
        }

        else{ //jesli nie, losujemy wolne miejsce dla dziecka spośród wolnych miejsc
            Vector2D randomUnitVector = nonOccupiedPositions.get(generator.nextInt(nonOccupiedPositions.size())).toUnitVector(); //losowany wektor jednostkowy
            this.position= fixPosition(parent1.getPosition().add(randomUnitVector));
        }
        this.genes=new Genes(parent1.getGenes(),parent2.getGenes());
        this.energy=(int) (0.25*(parent1.getEnergy()+parent2.getEnergy()));
        this.direction=MapDirection.values()[new Random().nextInt(8)]; //losowy kierunek początkowy
        parent1.setEnergy((int) (0.75*parent1.getEnergy())); //redukcja energii rodziców
        parent2.setEnergy((int) (0.75*parent1.getEnergy()));
        parent1.children.add(this);
        parent2.children.add(this);
        JPanel animalObservePanel = new JPanel(); //kazde zwierze ma swoj panel obserwacyjny
        JLabel label = new JLabel("Przez ile dni obserwować zwierze:");
        observeDaysField = new JTextField(10);
        JButton button = new JButton("Obserwuj");
        label.setLabelFor(observeDaysField);
        animalObservePanel.add(label);
        animalObservePanel.add(observeDaysField);
        animalObservePanel.add(button);
        button.addActionListener(this);
        add(animalObservePanel);
    }



    public String toString(){
       return switch(direction){
           case NORTH -> "N";
           case SOUTH -> "S";
           case WEST -> "W";
           case EAST -> "E";
           case NORTHWEST -> "NW";
           case NORTHEAST -> "NE";
           case SOUTHWEST -> "SW";
           case SOUTHEAST -> "SE";
       };
    }
    public void move(){ //ruch w losowym kierunku na podstawie genów
        Random moveGenerator = new Random();
        MoveDirection direction= genes.getGenes()[moveGenerator.nextInt(genes.getGenes().length)];
        move(direction);
    }
    public void move(MoveDirection direction){ //ruch zwierzęcia
        switch(direction){
            case FORWARD -> { //jesli nie zmienia kierunku to nic nie wykonujemy
            }
            case RIGHTFORWARD -> this.direction=this.direction.next();
            case RIGHT -> this.direction=this.direction.next().next();
            case RIGHTBACKWARD -> this.direction=this.direction.next().next().next();
            case BACKWARD -> this.direction=this.direction.next().next().next().next();
            case LEFTBACKWARD -> this.direction=this.direction.previous().previous().previous();
            case LEFT -> this.direction=this.direction.previous().previous();
            case LEFTFORWARD -> this.direction=this.direction.previous();
        }
        position=position.add(this.direction.toUnitVector());
        fixPosition();
    }
    public void fixPosition(){ //metoda naprawiająca pozycje zwierzęcia w przypadku wyjścia poza granice mapy
        int height=((RectangularMap)map).getHeight();
        int width=((RectangularMap)map).getWidth();
        int x= position.getX();
        int y= position.getY();
        if(x<0) x = width+x;
        if(y<0) y = height+y;
        position=new Vector2D(x%width, y%height);
    }
    public Vector2D fixPosition(Vector2D position){ //jak wyżej, tylko dla wektora
        int height=((RectangularMap)map).getHeight();
        int width=((RectangularMap)map).getWidth();
        int x= position.getX();
        int y= position.getY();
        if(x<0) x = width+x;
        if(y<0) y = height+y;
        return new Vector2D(x%width, y%height);
    }



    public int compareTo(Animal second){
    return this.energy-second.getEnergy();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return   energy == animal.energy &&
                direction == animal.direction &&
                position.equals(animal.position) &&
                genes.equals(animal.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, position, genes, energy);
    }


    protected void paintComponent(Graphics g){ //rysowanie panelu po kliknięciu zwierzęcia na zatrzymanej symulacji
        try {
            g.drawImage(getIcon(), 25, 50, 100,100,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString("ZWIERZE",150, 100);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Wiek zwierzęcia: "+this.age,25, 175);
        g.drawString("Liczba dzieci: "+this.children.size(),25, 225);
        g.drawString("Aktualna energia: "+this.energy, 25,275);
        g.drawString("Aktualna pozycja: "+this.position,25,325);
        g.drawString("Genotyp:",25,375);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString(this.genes.toString(),25,425);
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        String fieldText = observeDaysField.getText();
        int value = Integer.parseInt(fieldText);
        if(!Float.isNaN(value)){
            observingBeginDay=((RectangularMap)map).getDay();
            observingDays=value;
            childrenBeforeObservation=children.size();
            descedantsBeforeObservation=numberOfDescedants();
            ((RectangularMap) map).setObservedAnimal(this);
        }
        SwingUtilities.getWindowAncestor(this).setVisible(false);

    }
    public int numberOfDescedants(){ //liczba potomkow zwierzecia
        int childrenNumber = children.size();
        for(Animal child: children){
            childrenNumber+=child.numberOfDescedants();
        }
        return childrenNumber;
    }
}
