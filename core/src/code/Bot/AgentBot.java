package code.Bot;

import code.Physics.Rungekuttasolver;
import code.Screens.PuttingGameScreen;

import java.util.Arrays;

public class AgentBot {

    private float[] sensorSize;
    private float[] sensorsAngleX;
    private float[] sensorsAngleZ;
    private float[] maxPositionX;
    private float[] maxPositionZ;
    private float[] sensorsWeight;
    private float ballX;
    private float ballZ;
    private int bestSensor;

    private boolean[] canHitFlag;

    public AgentBot(float[] sensorsSize, float[] sensorsAngleX, float[] sensorsAngleZ, float[] maxPositionX, float[] maxPositionZ,  boolean[] canHitFlag, float ballX, float ballZ) {

        this.sensorSize = sensorsSize;
        this.sensorsAngleX = sensorsAngleX;
        this.sensorsAngleZ = sensorsAngleZ;
        this.maxPositionX = maxPositionX;
        this.maxPositionZ = maxPositionZ;
        this.canHitFlag = canHitFlag;
        this.ballX = ballX;
        this.ballZ = ballZ;

        this.sensorsWeight = new float[sensorsSize.length];
        this.sensorsWeight[0] = 15;
        this.sensorsWeight[1] = 10;
        this.sensorsWeight[2] = 7;
        this.sensorsWeight[3] = 4;
        this.sensorsWeight[4] = 2;
        this.sensorsWeight[5] = 1;
        this.sensorsWeight[6] = 2;
        this.sensorsWeight[7] = 4;
        this.sensorsWeight[8] = 7;
        this.sensorsWeight[9] = 10;
        this.sensorsWeight[10] = 15;

        System.out.println("sensorsSize = " + Arrays.toString(sensorsSize));
        System.out.println("sensorsAngleX = " + Arrays.toString(sensorsAngleX));
        System.out.println("sensorsAngleZ = " + Arrays.toString(sensorsAngleZ));
        System.out.println("maxSensorsX = " + Arrays.toString(maxPositionX));
        System.out.println("maxSensorsZ = " + Arrays.toString(maxPositionZ));
        System.out.println("canHitFlag = " + Arrays.toString(canHitFlag));
    }

    public float[] startBot() {

        float[] sensorsCost = new float[sensorSize.length];
        for (int i = 0; i < sensorSize.length; i++) {
            sensorsCost[i] = costFunction(i);
        }

        bestSensor = 0;
        float minimumCost = sensorsCost[0];
        for (int i = 0; i < sensorsCost.length; i++) {
            if (sensorsCost[i] < minimumCost) {
                minimumCost = sensorsCost[i];
                bestSensor  = i;
            }
        }

        System.out.println("sensorsCost = " + Arrays.toString(sensorsCost));
        System.out.println("bestSensor = " + (bestSensor+1));
        System.out.println("best angle x = " + sensorsAngleX[bestSensor]);
        System.out.println("best angle z = " + sensorsAngleZ[bestSensor]);
        System.out.println();

        Rungekuttasolver RK4 = new Rungekuttasolver();

        float powerScalar = PuttingGameScreen.evaluatePowerRK4(ballX, ballZ, maxPositionX[bestSensor], maxPositionZ[bestSensor], sensorsAngleX[bestSensor], sensorsAngleZ[bestSensor]);

        if (sensorsCost[bestSensor] == 0) {
            powerScalar += (1/(sensorSize[bestSensor]/3))*powerScalar;
        }

        RK4.setValues(ballX, ballZ, (sensorsAngleX[bestSensor]*powerScalar), (sensorsAngleZ[bestSensor]*powerScalar));
        RK4.RK4();

        float newBallX = (float) RK4.getX();
        float newBallZ = (float) RK4.getY();

        /*
        System.out.println("ballX = " + ballX);
        System.out.println("ballZ = " + ballZ);
        System.out.println("newBallX = " + newBallX);
        System.out.println("newBallZ = " + newBallZ);
        System.out.println();
        */

        return new float[]{newBallX, newBallZ};
    }

    public float costFunction(int index) {

        if (canHitFlag[index]) {
            return 0;
        }
        return sensorsWeight[index]/sensorSize[index];
    }

    public int getBestSensor() {
        return bestSensor;
    }
}
