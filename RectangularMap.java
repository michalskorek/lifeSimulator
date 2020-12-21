package lifeSimulator;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class RectangularMap extends JPanel implements IWorldMap {
    private static int height;
    private static int width;
    private final int jungleheight;
    private final int junglewidth;
    private static float jungleRatio;
    private static int dayEnergyCost;

    private final int jungleLeftTopX;
    private final int jungleLeftTopY;
    private final int widthScale; //rozmiar jednego "piksela" na mapie
    private final int heightScale;
    private final Stats stats; //statystyki mapy
    private int day=0; //dzien na mapie
    private Animal observedAnimal =null; //obserwowane zwierze
    public Map<Vector2D, LinkedList<Animal>> animals = new HashMap<>();
    public Map<Vector2D, Grass> grasses = new ConcurrentHashMap<>(); //concurrenthashmap zabezpiecza watek, dzieki czemu unikamy ConcurrentModificationException

    //getters
    public Map<Vector2D, LinkedList<Animal>> getAnimals() {
        return animals;
    }
    public int getDayEnergyCost(){
        return dayEnergyCost;
    }
    public Map<Vector2D, Grass> getGrasses() {
        return grasses;
    }
    public Stats getStats(){
        return stats;
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }
    public int getJungleheight() {
        return jungleheight;
    }
    public int getJunglewidth() {
        return junglewidth;
    }
    public int getJungleLeftTopX(){
        return jungleLeftTopX;
    }
    public int getJungleLeftTopY() {
        return jungleLeftTopY;
    }
    public int getDay() {
        return day;
    }
    public int getWidthScale() {
        return widthScale;
    }
    public int getHeightScale() {
        return heightScale;
    }


    //setters
    public static void setDayEnergyCost(int dayEnergyCost) {
        RectangularMap.dayEnergyCost = dayEnergyCost;
    }
    public static void setHeight(int height) {
        RectangularMap.height = height;
    }
    public static void setWidth(int width) {
        RectangularMap.width = width;
    }
    public static void setJungleRatio(float jungleRatio) {
        RectangularMap.jungleRatio = jungleRatio;
    }


    //konstruktor
    public RectangularMap() {
        this.stats=new Stats(this);
        this.jungleheight=(int) (height*jungleRatio);
        this.junglewidth=(int) (width*jungleRatio);
        this.jungleLeftTopX=(width-junglewidth)/2;
        this.jungleLeftTopY=(height-jungleheight)/2;
        widthScale = Math.round(800 / (float) width);
        heightScale = Math.round(800 / (float) height);


    }




    @Override
    public boolean place(Animal animal) {
        animals.computeIfAbsent(animal.getPosition(), k -> new LinkedList<Animal>());
        animals.get(animal.getPosition()).add(animal);
        Collections.sort(animals.get(animal.getPosition()));
        return true;
    }
    public boolean place(Grass grass){
        if(isOccupied(grass.getPosition())) return false;
        else grasses.put(grass.getPosition(),grass);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2D position) {
        return animals.get(position)!=null || grasses.get(position)!=null;
    }
    public void fixHashMap(){ //tworzy nowa hashmape z nowymi ruchami i usuwa martwe zwierzeta
        HashMap<Vector2D,LinkedList<Animal>> newAnimals= new HashMap<>();
        for(LinkedList<Animal> list: animals.values()) {
            for(Animal animal: list){
                if(animal.getEnergy()<=0) { //usuniecie martwego zwierzecia i poinformowania statystyk o jego życiu
                    stats.receiveDeadAnimal(animal);
                }
                else{
                    newAnimals.computeIfAbsent(animal.getPosition(), k -> new LinkedList<>());
                    newAnimals.get(animal.getPosition()).add(animal);
                    }
                }
            }
        for(LinkedList<Animal> list : newAnimals.values()){ //sortowanie każdej listy
            Collections.sort(list);
        }
        this.animals=newAnimals;
    }

    public void moveDay(){ //funkcja symulujaca dzien ruchow zwierzat
        for(LinkedList<Animal> list: animals.values()){
            {
                for(Animal animal: list) animal.move();
            }
        }
        fixHashMap(); //naprawa hashmapy po wszystkich ruchach
    }
    public void eatDay(){ //symulacja dnia jedzenia
        for(LinkedList<Animal> animalLinkedList: animals.values()){
            if(animalLinkedList.isEmpty()) continue; //jesli nie ma zwierzecia na tym polu to idziemy dalej
            if(grasses.get(animalLinkedList.getFirst().getPosition())==null) continue; //jesli na tym polu nie ma trawy to idziemy dalej
            int strongestAnimalsNumber=0; //liczba zwierzat o tej samej maksymalnej energii
            int maxPower=animalLinkedList.getLast().getEnergy();
            for(Animal animal: animalLinkedList){
                if(animal.getEnergy()==maxPower) strongestAnimalsNumber++;
            }
            for(Animal animal:animalLinkedList){
                if(animal.getEnergy()==maxPower) animal.setEnergy(animal.getEnergy()+(int)(Grass.getEatProfit()/strongestAnimalsNumber));
            }
            grasses.remove(animalLinkedList.getFirst().getPosition());
            Collections.sort(animalLinkedList);
        }
    }
    public void copulationDay(){ //symulacja dnia rozmnażania 
        LinkedList<Animal> newBorns = new LinkedList<>(); //nowonarodzone zwierzęta dodajemy po zakończeniu iteracji po mapie
        for(LinkedList<Animal> animalLinkedList: animals.values()){
            if(animalLinkedList.size()>=2){
                int listSize = animalLinkedList.size();
                Animal parent1 = animalLinkedList.get(listSize-1);
                Animal parent2 = animalLinkedList.get(listSize-2);
                if(parent1.getEnergy()>=0.5* Animal.getStartEnergy() && parent2.getEnergy()>=0.5* Animal.getStartEnergy()){
                    Animal newBorn = new Animal(this,parent1, parent2);
                    newBorns.add(newBorn);
                }
            }
        }
        for(Animal child: newBorns){ //umieszczenie noworodków na mapie
            this.place(child);
        }
        fixHashMap(); //naprawa mapy
    }
    public boolean isJungle(Vector2D position){ //sprawdza czy dane pole jest dżunglą
        int posX=position.getX();
        int posY=position.getY();
        if(posX<jungleLeftTopX) return false;
        else if (posX>=jungleLeftTopX+junglewidth) return false;
        else if (posY<jungleLeftTopY) return false;
        else return posY < jungleLeftTopY + jungleheight;
    }
    public void spawnGrassOutsideJungle(){ //generowanie nowej trawy poza dżunglą
        Random generator = new Random();
        LinkedList<Vector2D> nonOccupiedFields=new LinkedList<>();
        for(int i=0; i<height;i++){
            for(int j=0;j<width;j++){
                if(!isJungle(new Vector2D(j,i)) && !isOccupied(new Vector2D(j,i))) nonOccupiedFields.add(new Vector2D(j,i));
            }
        }

        if(nonOccupiedFields.isEmpty()) return;
        Vector2D grassPosition = nonOccupiedFields.get(generator.nextInt(nonOccupiedFields.size()));
        place(new Grass(this,grassPosition));
    }
    public void spawnGrassAtJungle(){ //generowanie nowej trawy w dżungli
        Random generator = new Random();
        LinkedList<Vector2D> nonOccupiedFields = new LinkedList<>();
        for(int i=jungleLeftTopY;i<jungleLeftTopY+jungleheight;i++){
            for(int j=jungleLeftTopX;j<jungleLeftTopX+junglewidth;j++){
                if(isJungle(new Vector2D(j,i)) && !isOccupied(new Vector2D(j,i))) nonOccupiedFields.add(new Vector2D(j,i));
            }
        }
        if(nonOccupiedFields.size()==0) return;
        Vector2D grassPosition = nonOccupiedFields.get(generator.nextInt(nonOccupiedFields.size()));
        place(new Grass(this,grassPosition));
    }
    
    public void reduceEnergy(){ //redukowanie energii każdemu zwierzęciu o wartość jednego dnia
        for(LinkedList<Animal> list: animals.values()){
            for(Animal animal: list){
                animal.setAge(animal.getAge()+1);
                animal.setEnergy(animal.getEnergy()-dayEnergyCost);
            }
        }
        fixHashMap();
    }



    public void newDay(){
        day++;
        reduceEnergy();
        spawnGrassOutsideJungle();
        spawnGrassAtJungle();
        moveDay();
        copulationDay();
        eatDay();

    }


    public void setObservedAnimal(Animal animal){
        observedAnimal=animal;
    }
    public boolean animalObservationEnded(){
        if(observedAnimal==null) return false;
        return observedAnimal.getObservingBeginDay() + observedAnimal.getObservingDays() == day;
    }
    public JFrame animalObservationResult() throws IOException {
        JFrame observationFrame = new JFrame("Wyniki obserwacji po "+observedAnimal.getObservingDays()+" dniach");
        observationFrame.setIconImage(Animal.getIcon());
        JPanel textToDisplay = new JPanel();
            if(observedAnimal.getEnergy()<=0){
                textToDisplay.add(new JLabel("Zwierze umarło"));
            }
            else{
                textToDisplay.add(new JLabel("Zwierze w tym czasie urodziło "+(observedAnimal.getChildren().size()-observedAnimal.getChildrenBeforeObservation())+" dzieci. Przybyło mu "+(observedAnimal.numberOfDescedants()-observedAnimal.getDescedantsBeforeObservation())+" potomków."));
            }
            observationFrame.add(textToDisplay);
            return observationFrame;

    }
}
