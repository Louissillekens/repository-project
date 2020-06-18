package code.NN;

import java.io.Serializable;

/**
 * @author Alexandre Martens
 */
public class Neuron implements Serializable {

    static float minWeightValue;
    static float maxWeightValue;

    float[] weights;
    float[] weightsCache; // Used in backpropagation. Stores the new calc weights without replacing the original ones (= weights). We need them to calculate the next layer
    float gradient; // Speed up calculations, because a lot of reuse of data
    float bias;
    float value = 0;


    // Constructor input neurons
    public Neuron(float value){
        this.value = value;

        this.bias = -1;
        this.gradient = -1;
        this.weights = null;
        this.weightsCache = null;
    }

    // Constructor hidden/output neurons
    public Neuron(float[] weights, float bias){
        this.weights = weights;
        this.weightsCache = this.weights;
        this.bias = bias;
        this.gradient = 0;
    }

    // Set min and max range for the weights
    public static void setRangeWeight(float min, float max){
        minWeightValue = min;
        maxWeightValue = max;
    }

    // At the end of the backpropagation, replace the old/original weights (= weights) with the new calculated ones (= weightsCache)
    public void updateWeights(){
        this.weights = this.weightsCache;
    }

    public float getValue() {
        return value;
    }
}
