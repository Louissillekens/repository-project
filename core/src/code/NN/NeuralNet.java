package code.NN;

/**
 * @author Alexandre Martens
 */
public class NeuralNet {

    Layer[] layers; //OK
    float learningRate; //OK

    Data data;

    float nextStateQ; //Qval chosen and wich node it is
    float gamma = 0.8f; //How important furure rewards become (closer to 1 means giving more importance to future rewards)

    //Under the rewards cat.
    float costStep = -8;
    float localReward;

    /**
     * Creates the Neural Network Infrastructure
     */
    public NeuralNet(){
        // Set the range of the weights
        Neuron.setRangeWeight(-1,1);

        //Creating the layers
        this.layers = new Layer[4]; //4 layers: input, hidden and output
        this.layers[0] = null; // Input layer
        this.layers[1] = new Layer(11,64); // Hidden layer
        this.layers[2] = new Layer(64,64); // Hidden layer
        this.layers[3] = new Layer(64, 110); // Output layer
        System.out.println("NeuralNet: Created");
    }

    /**
     * @param nn Input nn caracteristics (layers, so values, weights...)
     * @param t Input data float vector
     * @return outputs data float vector
     */
    public float[] forward(NeuralNet nn, float[] t){
        this.layers = nn.layers; //Have the characteristics of the input nn

        Data data = new Data(t, new float[layers[layers.length-1].neurons.length]);
        layers[0] = new Layer(data.getDataInput()); // Loads the input of the data into the input layer (layer[0])

        for (int i = 1; i < layers.length; i++){ // Loop true all the layers (hidden and output) (=I)
            for (int j = 0; j < layers[i].neurons.length; j++){ // Loop true all the neurons in that layer
                float sum = 0;

                for (int k = 0; k < layers[i-1].neurons.length; k++){ // Loops true the neurons of each layer, starting from the input layer (1 layer earlier than j)
                    sum += (layers[i-1].neurons[k].value)*(layers[i].neurons[j].weights[k]); // Formula: SUM(all_weights*value_prev_neuron)
                }
                sum += layers[i].neurons[j].bias;

                if (i == layers.length-1){
                    layers[i].neurons[j].value = sum;
                    data.setDataOutput(layers[i].neurons[j].value,j);
                }else
                    //TODO should the last layer have the sum or an activation function or scaled
                    layers[i].neurons[j].value = MathWork.sigmoid(sum); // New value of the neuron by taking the sigmoid function
            }
        }
        //TODO check if outputs come out
        return data.getDataOutput();
    }


    /** Backward propagation
     * FIXED
     * Going from the end of the nn to the front, layer by layer going backwards
     * Methodology: Calculate the derivative, weights -> update all weights
     * Documentation source: https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
     * TODO implement rewards and costs
     *  https://stats.stackexchange.com/questions/200006/q-learning-with-neural-network-as-function-approximation
     */
    public void backprop(float[] loss, float[] actionCache) {
        int nMin1Layers = layers.length-1; // All hidden layers and the output layer

        // Go true each sample
        for(int s = 0; s < loss.length; s++) {

            // Update the output layer first and each neuron
            for (int n = 0; n < layers[nMin1Layers].neurons.length; n++) {
                float partialDerivative;

                // First calc the partial derivative to sigmoid
                if (n == actionCache[n]) { // Node is the action we took
                    float y = layers[layers.length-1].neurons[n].value; // = output policy network
                    partialDerivative = loss[s] * y * (1 - y); // Formula for the partial derivative for output: LOSS*derivative respect to function sigmoid and output
                } else {
                    partialDerivative = 0; // Node isn't the action we took. 0 because if loss = 0, all is 0 (look formula above)
                }

                layers[nMin1Layers].neurons[n].gradient = partialDerivative;

                // calculate all the new weights of n (not assigning them yet)
                for (int w = 0; w < layers[nMin1Layers].neurons[n].weights.length; w++) {
                    float prevY = layers[nMin1Layers - 1].neurons[w].value;

                    float deltaWeight = learningRate * partialDerivative * prevY; // Formula

                    float newWeight = layers[nMin1Layers].neurons[n].weights[w] - deltaWeight; // Formula
                    layers[nMin1Layers].neurons[n].weightsCache[w] = newWeight;
                }
                // Update bias
                float bias_correction = partialDerivative*learningRate;
                layers[nMin1Layers].neurons[n].bias -= bias_correction;
            }


            // Update the hidden layers
            for (int l = nMin1Layers - 1; l > 0; l--) {
                // Go trough each neuron
                for (int n = 0; n < layers[l].neurons.length; n++) {
                    float y = layers[l].neurons[n].value; // = output policy network
                    float sumOutputs = sumGradient(n, l + 1); // sum all outputs gradient connecting n to next layer
                    float partialDerivative = y * (1 - y) * sumOutputs;  // Formula for the partial derivative for hidden l: LOSS*derivative respect to function sigmoid and output

                    layers[l].neurons[n].gradient = partialDerivative;

                    // calculate all the new weights of n (not assigning them yet)
                    for (int w = 0; w < layers[l].neurons[n].weights.length; w++) {
                        float pervY = layers[l - 1].neurons[w].value;

                        float deltaWeight = learningRate * partialDerivative * pervY;

                        float newWeight = layers[l].neurons[n].weights[w] - deltaWeight;
                        layers[l].neurons[n].weightsCache[w] = newWeight;
                    }
                    // Update bias
                    float bias_correction = partialDerivative*learningRate;
                    layers[l].neurons[n].bias -= bias_correction;
                }
            }


            // Update the weights
            for (int i = 0; i < layers.length; i++) {
                for (int j = 0; j < layers[i].neurons.length; j++) {
                    layers[i].neurons[j].updateWeights();
                }
            }
        }
    }


    /** Sums of all the gradients connecting a given neuron to a given layer+1 (we'll loop through all of them to get the wieghts)
     * @param neuronIndex position of the neuron
     * @param next_layerIndex next layer index
     * @return sum of the gradients to the next layer
     */
    public  float sumGradient(int neuronIndex, int next_layerIndex) {
        float gradient_sum = 0;

        // Go trough all the neurons of that next layer
        // allN = neuron
        for(int allN = 0; allN < layers[next_layerIndex].neurons.length; allN++) {
            Neuron neuron = layers[next_layerIndex].neurons[allN]; // Neuron neuron = neuron in the allN'th place of the next layer
            gradient_sum += neuron.weights[neuronIndex]* neuron.gradient; // Grab the weight of neuron connecting neuronIndex * Grab the gradient of neuron
        }
        return gradient_sum;
    }


    /**
     * @param learningRate set the learning rate for the network
     */
    public void setLearningRate(float learningRate) {
        this.learningRate = learningRate;
    }
}
