package code.Q_Learning;

import code.NeuralNet.NeuralNet;
import code.NeuralNet.Visuals;

import java.util.ArrayList;
import java.util.Arrays;

public class QL {
    static int xStart = 2;
    static int yStart = 2;

    static int xFlag = 5;
    static int yFlag = 7;

    static int precision = 2; // 1 square will be traversed in 2 steps: 0.5/0.5

    static int maxEp = 200;


    public static void main(String[] args) {

        NeuralNet neuralNet = new NeuralNet();

        Agent agent = new Agent(xStart, yStart, xFlag, yFlag);
        agent.sensors = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

        neuralNet.createTrainingData(agent);
        for (int i = 0; i < 1; i++)
            neuralNet.forward();

    /* int episode = 0;
        while (episode < maxEp){
            while (compareState() == false){

            }
            agent.reset(xStart, yStart,precision);
            episode++;
        }*/



    }

    //Checks whether the next state reached its destination
    private static boolean compareState() {
        int [] flag = {xFlag, yFlag};
        int [] nextState = {};

        return Arrays.equals(flag, nextState);
    }
}
