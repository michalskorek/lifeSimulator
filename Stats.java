package lifeSimulator;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Stats {
    private final RectangularMap map;
    private int deadAnimalsNumber=0;
    private long deadAnimalsAgeSum=0;
    DecimalFormat displayFormat = new DecimalFormat("#.##");
    public Stats(RectangularMap map){
        this.map=map;
    }
    public float averageAge(){
        float ageSum=0;
        int numberOfAnimals=0;
        for(LinkedList<Animal> list: map.getAnimals().values()){
            numberOfAnimals+=list.size();
            for(Animal animal:list){
                ageSum+=animal.getAge();
            }
        }
        return ageSum/numberOfAnimals;
    }
    public float averageChildrenNumber(){
        float childrenSum=0;
        int numberOfAnimals=0;
        for(LinkedList<Animal> list: map.getAnimals().values()){
            numberOfAnimals+=list.size();
            for(Animal animal:list){
                childrenSum+=animal.getChildren().size();
            }
        }
        return childrenSum/numberOfAnimals;
    }
    public int numberOfAnimals(){
        int numberOfAnimals=0;
        for(LinkedList<Animal> list: map.getAnimals().values()){
            numberOfAnimals+=list.size();
        }
        return numberOfAnimals;
    }
    public float averageEnergy(){
        long sumEnergy=0;
        for(LinkedList<Animal> list:map.getAnimals().values()){
            for(Animal animal:list){
                sumEnergy+=animal.getEnergy();
            }
        }
        return sumEnergy/(float)numberOfAnimals();
    }
    public int numberOfGrasses(){
        return map.getGrasses().size();
    }
    public Genes mostCommonGenotype() {
        HashMap<Genes,Integer> allGenes = new HashMap<>();
        for(LinkedList<Animal> list: map.getAnimals().values()){
            for(Animal animal:list){
                if(allGenes.containsKey(animal.getGenes())) allGenes.put(animal.getGenes(), allGenes.get(animal.getGenes())+1);
                else allGenes.put(animal.getGenes(), 1);
            }
        }
        int mostCommonGenotypeNumber=0;
        Genes mostCommonGenotype= new Genes();
        for (Map.Entry<Genes, Integer> entry : allGenes.entrySet()) {
            Genes key = entry.getKey();
            int value = entry.getValue();
            if(value>mostCommonGenotypeNumber) {
                mostCommonGenotypeNumber=value;
                mostCommonGenotype=key;
            }
        }
        return mostCommonGenotype;
    }
    public String toString(){
        StringBuilder stats = new StringBuilder();
        stats.append("Day "+map.getDay()+": ");
        stats.append("average age of alive animals: "+displayFormat.format(averageAge())+", ");
        stats.append("average age of dead animals: "+displayFormat.format(averageDeadAge())+", ");
        stats.append("average energy: "+displayFormat.format(averageEnergy())+", ");
        stats.append("average number of children: "+displayFormat.format(averageChildrenNumber())+", ");
        stats.append("Most common genotype: "+mostCommonGenotype()+"\n");
        return stats.toString();
    }
    public void receiveDeadAnimal(Animal animal){
        deadAnimalsNumber++;
        deadAnimalsAgeSum+=animal.getAge();
    }

    public float averageDeadAge(){
        return (float) deadAnimalsAgeSum/deadAnimalsNumber;
    }
}
