package code.Bridge;

import java.util.Arrays;

public class LinkAgentNN {

    private static boolean collision;
    private static boolean win_position;
    private static float xPosition;
    private static float yPosition;
    private static float[] sensors;


    public LinkAgentNN(int action){
        //constructorCalc(/* YOUR METHOD THAT RETURNS A FLOAT ARRAY*/);
    }

    private void constructorCalc(float[] arr){
        // Collision
        if (arr[3] == 1)
            this.collision = true;
        else
            this.collision = false;

        // Win position
        if (arr[4] == 1)
            this.win_position = true;
        else
            this.win_position = false;

        // Update the position
        this.xPosition = arr[5];
        this.yPosition = arr[6];

        // Sensor Calculation
        this.sensors = Arrays.copyOfRange(arr, 7, arr.length);
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
