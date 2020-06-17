package code.Lets_Go_Champ;

import code.NN.NeuralNet;

/**
 * @author Alexandre Martens
 */
public class DQN {

    private NeuralNet nn;
    private boolean trainingMode; //TODO use this to check if it can backprop

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
        nn.backprop(loss, actionCache);
    }

    public void copyLayers(DQN policy){
        this.nn = policy.nn;
    }

    public void setTrainingMode(boolean trainingMode) {
        this.trainingMode = trainingMode;
    }

    public void setLR(float lr){
        nn.setLearningRate(lr);
    }

    public NeuralNet getNn() {
        return nn;
    }

    public void setNn(NeuralNet nn) {
        this.nn = nn;
    }
}
