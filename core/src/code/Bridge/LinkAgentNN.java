package code.Bridge;

import java.util.ArrayList;

public class LinkAgentNN {

    private static boolean checkCollision;
    private static boolean checkWinPosition;
    private static float xPosition;
    private static float yPosition;
    private static int action;


    public LinkAgentNN(ArrayList<boolean[]> booleansOutput, ArrayList<float[]> dataOutput) {

        boolean[] checkOutputBooleanStep = booleansOutput.get(action);
        float[] checkOutputDataStep = dataOutput.get(action);

        checkCollision = checkOutputBooleanStep[0];
        checkWinPosition = checkOutputBooleanStep[1];
        xPosition = checkOutputDataStep[3];
        yPosition = checkOutputDataStep[4];
    }

    public static void setAction(int action) {
        LinkAgentNN.action = action;
    }

    public static int getAction() {
        return action;
    }

    public static float getxPosition() {
        return xPosition;
    }

    public static float getyPosition() {
        return yPosition;
    }

    public static boolean getcheckCollision() {
        return checkCollision;
    }

    public static boolean getcheckWinPosition() {
        return checkWinPosition;
    }
}
