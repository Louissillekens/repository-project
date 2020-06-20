package code.Bot;

import code.Screens.PuttingGameScreen;

import java.util.ArrayList;

public class Agent {

    private ArrayList<float[]> outputData;

    public Agent() {

        this.outputData = PuttingGameScreen.getSensorsOutput();
    }

    public ArrayList<float[]> getOutputData() {
        return outputData;
    }
}
