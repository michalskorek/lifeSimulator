package lifeSimulator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

//klasa odpowiedzialna za obsluge wielu symulacji

public class MultiSimulationEngine implements IEngine {
    private SimulationEngine [] simulations;
    private static int refreshTime;

    public static void setRefreshTime(int refreshTime) {
        MultiSimulationEngine.refreshTime = refreshTime;
    }

    public MultiSimulationEngine(int numberOfSimulations) throws IOException, ParseException {
        loadSettings();
        if(numberOfSimulations<1) throw new IllegalArgumentException("Simulation number must be at least 1!");
        simulations=new SimulationEngine[numberOfSimulations];
        for(int i=0;i<numberOfSimulations;i++) simulations[i]=new SimulationEngine(new RectangularMap(),i);
    }
    @Override
    public void run() throws InterruptedException, IOException {
        int stoppedSimulations;
        while(true){
            stoppedSimulations=0;
            for(SimulationEngine simulation:simulations){ //obliczanie liczby zamkniętych symulacji
                if(!simulation.getFrame().isVisible()) stoppedSimulations++;
                else simulation.run();
            }
            TimeUnit.MILLISECONDS.sleep(refreshTime);
            if(stoppedSimulations==simulations.length) {
                System.out.println("Zamykam aplikacje. Wszystkie symulacje zostały zamknięte");
                return;
            }
        }
    }
    public static void loadSettings() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/lifeSimulator/parameters.json"));
        JSONObject jsonObject = (JSONObject) obj;
        Long width =  (Long) (jsonObject.get("width"));
        Long height = (Long) jsonObject.get("height");
        Long plantEnergy = (Long) jsonObject.get("plantEnergy");
        Long startEnergy = (Long) jsonObject.get("startEnergy");
        Long moveEnergy = (Long) jsonObject.get("moveEnergy");
        Double jungleRatio = (Double) jsonObject.get("jungleRatio");
        Long initialAnimalsNumber = (Long) jsonObject.get("initialAnimalsNumber");
        Long refreshTime = (Long) jsonObject.get("refreshTime");
        Animal.setStartEnergy(startEnergy.intValue());
        Grass.setEatProfit(plantEnergy.intValue());
        RectangularMap.setHeight(height.intValue());
        RectangularMap.setWidth(width.intValue());
        RectangularMap.setJungleRatio(jungleRatio.floatValue());
        RectangularMap.setDayEnergyCost(moveEnergy.intValue());
        SimulationEngine.setInitialAnimalsNumber(initialAnimalsNumber.intValue());
        MultiSimulationEngine.setRefreshTime(refreshTime.intValue());
    }
}
