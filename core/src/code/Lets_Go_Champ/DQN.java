package code.Lets_Go_Champ;

import code.NN.NeuralNet;

import java.io.Serializable;

/**
 * @author Alexandre Martens
 */
public class DQN implements Serializable {

    private NeuralNet nn;
    private boolean trainingMode; // Check if it can backpropagate

    //Create the neural network
    DQN(){
        NeuralNet nn = new NeuralNet();
        this.nn = nn;

        this.trainingMode = true;
    }

    float [] forward(float[] t){
        return nn.forward(nn, t);
    }

    void backprop(float[] loss, float[] actionCache){
        if (this.trainingMode == true){
            nn.backprop(loss, actionCache);
        } else
            System.out.println("This network has not the permission to train");
    }

    public void copyLayers(DQN policy){
        this.nn = policy.nn;
    }

    public void setTrainingMode(boolean trainingMode) {
        this.trainingMode = trainingMode;
    }

    public void setLR(float lr){
        this.nn.setLearningRate(lr);
    }

    public NeuralNet getNn() {
        return nn;
    }

    public void setNn(NeuralNet nn) {
        this.nn = nn;
    }

    public void setActivationFunction(String activation_function){
        this.nn.setActivationFunction(activation_function);
    }
}
