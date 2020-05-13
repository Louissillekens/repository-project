package code.NeuralNet;

// The purpose of this class is to hold all the neurons
// Only for fully connected feed forward nn

/**
 * @author Alexandre Martens
 */
public class Layer {
    public Neuron[] neurons;

    /**
     * Constructor hidden & output layer
     * @param numberWeights number of neurons (= weights too) of the previous layer
     * @param numberNeurons number of neurons in the layer
     */
    public Layer(int numberWeights, int numberNeurons ){

        // Creates the layer (neurons) made of 'numberNeurons' neurons from the Neuron class in it
        this.neurons = new Neuron[numberNeurons];

        for (int i = 0; i < numberNeurons; i++){
            /*
             * Creates an array to store the weights per neuron. Remember that the number of inputs/weights per neuron
             * depends on the number of neurons in the previous layer (fully connected layers).
             * The input weights of that neuron, illustration: =I
             * '=' representing the weights (2 in this case)
             * 'I' the layer with n neurons,
             */
            float[] weights = new float[numberWeights];
            for (int j = 0; j < numberWeights; j++){
                weights[j] = MathWork.randomN(Neuron.minWeightValue, Neuron.maxWeightValue); // Initialise the weights at random
            }
            /*
             * Assign the weights that we just randomly initialised to that neuron.
             * Assign a random bias value between 0-1
             * It's the hidden/output constructor in the class Neuron.java.
             */
            neurons[i] = new Neuron(weights, MathWork.randomN(0,1));
        }
    }

    public Layer(float input[]){
        this.neurons = new Neuron[input.length]; // Creates the layer (neurons) made of 'input.length' neurons from the Neuron class in it

        for (int i = 0; i < input.length; i++){
            /*
             * Assign the value of each information to that neuron.
             * It's the input constructor in the class Neuron.java.
             */
            this.neurons[i] = new Neuron(input[i]);
        }
    }
}
