package code.Q_Learning;

import code.NeuralNet.NeuralNet;

import java.util.Arrays;

public class QL {
    static float xStart = 2;
    static float yStart = 2;

    static float xFlag = 5;
    static float yFlag = 7;

    static int maxEp = 200;
    static int maxSteps = 15;


    public static void main(String[] args) {

        NeuralNet neuralNet = new NeuralNet();

        Agent agentOriginal = new Agent(xStart, yStart, xFlag, yFlag); //TODO has to update it's sensors imed in constructor
        agentOriginal.setSensors(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});

        int episode = 0; //number of times we arrived to the flag
        int ms = 0; //Number of steps before arriving at the goal

        while (episode < maxEp){
            while (compareState(agentOriginal) == false && ms < maxSteps){
                neuralNet.createTrainingData(agentOriginal,true); //0 means original agent that we save
                neuralNet.forward(true); //Original agent with Qval
                neuralNet.forwardQMax(); //All maxQ val mapped with every action possible from the original agent
                neuralNet.backprop(); //TODO fix the backprop completely. Bias >0 and values ridiculous big. cost is q-q'**2
                agentOriginal = neuralNet.update();
                ms++;
            }
            //Reset position, reward
            agentOriginal.reset(xStart, yStart); //TODO implement reset sensor calc
            agentOriginal.setSensors(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
            episode++;
            //decayepsilon
        }



        /*TODO take action:
         *use the value in the currentState array to identify which action to take
         *Take that action action with the agent and update the agent
         *Take the sensor values again of the agent and redo a trainingset
         * get the max Q value of that new state
         * do the calc and backprop the network
         */


    }

    //Checks whether the next state reached its destination
    private static boolean compareState(Agent agent) {
        float [] flag = { xFlag, yFlag};
        float [] state = {agent.getxPos(),agent.getyPos()};
        //TODO checks if ball is in hole or raduis
        //Arrays.equals(flag, nextState)
        return false;
    }
}
