package code.Bot;

import code.Screens.PuttingGameScreen;

import java.util.ArrayList;

import static code.Screens.PuttingGameScreen.startAgent;

public class Agent {

    private ArrayList<float[]> outputData;

    public Agent() {
        PuttingGameScreen.freeToGo = false;

        startAgent();
        PuttingGameScreen.freeToGo = true;

        outputData = PuttingGameScreen.sensorCalc();

        /*for (int i = 0; i < outputData.size(); i++) {
            System.out.println("sensorsOutput = " + Arrays.toString(outputData.get(i)));
        }*/
    }

    public ArrayList<float[]> getOutputData() {
        return outputData;
    }
}
