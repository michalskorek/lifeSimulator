package lifeSimulator;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class World {
    public static void main(String[] args) throws  InterruptedException, IOException, ParseException {
        IEngine engine;
        try{
            engine = new MultiSimulationEngine(3);
        }
       catch(IllegalArgumentException exception){
           engine = new MultiSimulationEngine(1);
       }
        engine.run();



    }
}
