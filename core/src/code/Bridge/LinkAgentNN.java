package code.Bridge;

import code.Bot.Agent;
import code.Screens.PuttingGameScreen;

import java.util.ArrayList;
import java.util.Arrays;

public class LinkAgentNN {

    private static boolean collision;
    private static boolean win_position;
    private static float xPosition;
    private static float yPosition;
    private static float[] sensors;
    private ArrayList<float[]> sensorsOutput;
    private float[] actionSensorOutput;
    public static boolean newAgent = false;


    public LinkAgentNN(int action) {

        PuttingGameScreen.action = action;

        while (!PuttingGameScreen.isReadyToTrain) {
            //System.out.println("action = " + PuttingGameScreen.action);
            newAgent = true;

        }

        newAgent = false;

        // Object agent that (for now) can just return the output data from PuttingGameScreen
        Agent agent = new Agent();

        // ArrayList of float[] that corresponds to the 0-109 different actions
        sensorsOutput = agent.getOutputData();
        System.out.println(sensorsOutput.getClass().getSimpleName());
        // Simple float[] that contains data you need for that specific action
        constructorCalc(sensorsOutput.get(action));
    }

    private void constructorCalc(float[] arr){
        // Collision
        if (arr[0] == 1)
            this.collision = true;
        else
            this.collision = false;

        // Win position
        if (arr[1] == 1)
            this.win_position = true;
        else
            this.win_position = false;

        // Update the position
        this.xPosition = arr[2];
        this.yPosition = arr[3];

        // Sensor Calculation
        this.sensors = Arrays.copyOfRange(arr, 4, arr.length);
    }

    public float getxPosition() {
        return xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public boolean getCollision() {
        return collision;
    }

    public boolean getWinPosition() {
        return win_position;
    }

    public static float[] getSensors(){
        return sensors;
    }

    /*    public LinkAgentNN(ArrayList<boolean[]> booleansOutput, ArrayList<float[]> dataOutput) {

        boolean[] checkOutputBooleanStep = booleansOutput.get(action);
        float[] checkOutputDataStep = dataOutput.get(action);

        collision = checkOutputBooleanStep[0];
        win_position = checkOutputBooleanStep[1];
        xPosition = checkOutputDataStep[3];
        yPosition = checkOutputDataStep[4];
    }*/
}