package lifeSimulator;

import java.util.*;

public class Genes {

    final private int numberOfGenes=32;
    private MoveDirection [] genes=new MoveDirection[numberOfGenes];

    public MoveDirection[] getGenes(){
        return genes;
    }

    //constructors
    public Genes(){ //generator losowych genów, z warunkiem, że istnieje min 1 gen każdego typu
        Random generator=new Random();
        MoveDirection [] allMoves=MoveDirection.values();
        for(int i=0;i<numberOfGenes;i++){
            genes[i]=allMoves[generator.nextInt(allMoves.length)];
        }
        while(!containsAll()){
            genes[generator.nextInt(numberOfGenes)]=allMoves[generator.nextInt(allMoves.length)];
        }
        Arrays.sort(genes);

    }

    public Genes(Genes firstParent, Genes secondParent){ //konstruktor genow na podstawie rodzicow
        Random generator=new Random();
        MoveDirection [] firstParentGenes = firstParent.genes;
        MoveDirection [] secondParentGenes = secondParent.genes;

        //losowanie miejsc podzialu grup genów
        int firstGroupBegin = 0;
        int secondGroupBegin = generator.nextInt(numberOfGenes-1)+1;
        int thirdGroupBegin = generator.nextInt(numberOfGenes-1)+1;
        while (thirdGroupBegin==secondGroupBegin) thirdGroupBegin=generator.nextInt(31)+1;
        if(secondGroupBegin>thirdGroupBegin){
            int tmp = secondGroupBegin;
            secondGroupBegin = thirdGroupBegin;
            thirdGroupBegin = tmp;
        }


        int [] groupSeparators = {firstGroupBegin,secondGroupBegin, thirdGroupBegin};
        boolean firstParentGivesMore = generator.nextBoolean(); //losowanie ktory rodzic da dziecku dwie grupy genów
        if(firstParentGivesMore){
            genes=firstParentGenes;
            int secondParentGroup = groupSeparators[generator.nextInt(3)]; //losujemy grupe ktora da rodzic od którego dziedziczy tylko jedną grupę
            //nadpisujemy wylosowana grupe genami rodzica
            if(secondParentGroup==firstGroupBegin) for(int i=firstGroupBegin;i<secondGroupBegin;i++) genes[i]=secondParentGenes[i];
            if(secondParentGroup==secondGroupBegin) for(int i=secondGroupBegin;i<thirdGroupBegin;i++) genes[i]=secondParentGenes[i];
            if(secondParentGroup==thirdGroupBegin) for(int i=thirdGroupBegin;i<numberOfGenes;i++) genes[i]=secondParentGenes[i];
        }
        else{ //jak wyzej, tylko jesli drugi rodzic da mniej
            genes=secondParentGenes;
            int firstParentGroup = groupSeparators[generator.nextInt(3)];
            if(firstParentGroup==firstGroupBegin) for(int i=firstGroupBegin;i<secondGroupBegin;i++) genes[i]=firstParentGenes[i];
            if(firstParentGroup==secondGroupBegin) for(int i=secondGroupBegin;i<thirdGroupBegin;i++) genes[i]=firstParentGenes[i];
            if(firstParentGroup==thirdGroupBegin) for(int i=thirdGroupBegin;i<numberOfGenes;i++) genes[i]=firstParentGenes[i];
        }

        boolean containsAll=false;
        while(!containsAll){ //sprawdzamy czy dziecko ma gen kazdego typu
            List<MoveDirection> list = Arrays.asList(genes);
            containsAll=true;
            for(MoveDirection move:MoveDirection.values()){
                if(!list.contains(move)){
                    containsAll=false;
                    genes[generator.nextInt(numberOfGenes)]=move;
                }
            }
        }

        Arrays.sort(genes);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genes genes1 = (Genes) o;
        return Arrays.equals(genes, genes1.genes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }


    public boolean containsAll(){ //sprawdzanie czy istnieje gen kazdego typu w genotypie
        TreeSet<MoveDirection> present = new TreeSet<>(Arrays.asList(genes));
        return present.containsAll(Arrays.asList(MoveDirection.values()));
    }
    public String toString() { //wypisanie genotypu w formie np [000122344566777]
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (MoveDirection gene : genes) {
            result.append(gene.getLevel());
        }
        result.append("]");
        return result.toString();
    }

}
