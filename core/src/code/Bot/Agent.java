package code.Bot;

import code.Screens.PuttingGameScreen;

import java.util.ArrayList;
import java.util.Arrays;

public class Agent {

    private ArrayList<float[]> outputData;

    public Agent() {

        outputData = PuttingGameScreen.getSensorsOutput();
        System.out.println(Arrays.deepToString(new ArrayList[]{outputData}));
    }

    public ArrayList<float[]> getOutputData() {
        return outputData;
    }
}
