package code.Lets_Go_Champ;

import code.NN.NeuralNet;

public class DQN {
    NeuralNet nn;

    //Create the neural network
    DQN(){
        NeuralNet nn = new NeuralNet();
        this.nn = nn;
    }

    float [] forward(NeuralNet nn, float[] t){
        return nn.forward(nn, t);
    }
}
