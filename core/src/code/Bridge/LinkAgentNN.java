package code.Bridge;

public class LinkAgentNN {

    private static boolean collision;
    private static boolean win_position;
    private static float xPosition;
    private static float yPosition;
    private static int action;


    public LinkAgentNN(int action){
        constructorCalc(/* YOUR METHOD THAT RETURNS A FLOAT ARRAY*/);
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
    }

    public static float getxPosition() {
        return xPosition;
    }

    public static float getyPosition() {
        return yPosition;
    }

    public static boolean getCollision() {
        return collision;
    }

    public static boolean getWinPosition() {
        return win_position;
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
