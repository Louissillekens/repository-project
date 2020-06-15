package code.Q_Learning;

import code.NeuralNet.NeuralNet;

import java.util.Arrays;

public class QL {
    static double xStart = 2;
    static double yStart = 2;

    static double xFlag = 5;
    static double yFlag = 7;

    static int precision = 2; // 1 square will be traversed in 2 steps: 0.5/0.5

    static int maxEp = 200;


    public static void main(String[] args) {

        NeuralNet neuralNet = new NeuralNet();

        Agent agentOriginal = new Agent(xStart, yStart, xFlag, yFlag);
        agentOriginal.setSensors(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});

        neuralNet.createTrainingData(agentOriginal,true); //0 means original agent that we save
        neuralNet.forward(true); //Original agent with Qval
        neuralNet.forwardQMax(); //All maxQ val mapped with every action possible from the original agent
        neuralNet.backprop(); //TODO fix the backprop completely. Bias >0 and values ridiculous big
        neuralNet.update();
        /*TODO take action:
           *use the value in the currentState array to identify which action to take
           *Take that action action with the agent and update the agent
           *Take the sensor values again of the agent and redo a trainingset
           * get the max Q value of that new state
           * do the calc and backprop the network
         */

        neuralNet.backprop();
        //neuralNet.forward(1);


    /* int episode = 0;
        while (episode < maxEp){
            while (compareState() == false){

            }
            agent.reset(xStart, yStart,precision);
            episode++;
        }*/
    }

    //Checks whether the next state reached its destination
/*    private static boolean compareState() {
        int [] flag = {xFlag, yFlag};
        int [] nextState = {};

        return Arrays.equals(flag, nextState);
    }*/
}
