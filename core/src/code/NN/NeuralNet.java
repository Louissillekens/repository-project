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
     * Hardest part to implement and understand
     * Going from the end of the nn to the front, layer by layer going backwards
     * Methodology: Calculate output layer weights -> calculate the hidden layers weights -> update all weights
     * Documentation source: https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
     * TODO improve the backpropagate gradient: https://medium.com/datathings/neural-networks-and-backpropagation-explained-in-a-simple-way-f540a3611f5e
     * TODO implement rewards and costs
     *  https://stats.stackexchange.com/questions/200006/q-learning-with-neural-network-as-function-approximation
     */
    public void backprop(float[] loss, float[] actionCache){

        int nMin1Layers = layers.length-1; // All hidden layers and the output layer

        // Go true each sample
        for(int s = 0; s < loss.length; s++){

            // Update the output layer first
            // For all neurons in that layers
            for (int i = 0; i < layers[nMin1Layers].neurons.length; i++){
                float delta = loss[s]; //TODO can i use loss as delta here?

                layers[nMin1Layers].neurons[i].gradient = delta;

                // And for all their weights
                for (int j = 0; j < layers[nMin1Layers].neurons[i].weights.length; j++) {
                    float previous_output = layers[nMin1Layers - 1].neurons[j].value;
                    float error = delta * previous_output;
                    layers[nMin1Layers].neurons[i].weightsCache[j] = layers[nMin1Layers].neurons[i].weights[j] - learningRate * error; // Formula: New weight = old weight — Derivative * learning rate
                }

                layers[nMin1Layers].neurons[i].bias = delta*learningRate; // Update bias
            }


            // Update the hidden layers
            for(int i = nMin1Layers-1; i > 0; i--) {

                // For all neurons in that layers
                for(int j = 0; j < layers[i].neurons.length; j++) {
                    float local_output = layers[i].neurons[j].value;
                    float gradient_sum = sumGradient(j,i+1);
                    float delta = (gradient_sum)*(local_output *(1- local_output));

                    layers[i].neurons[j].gradient = delta;

                    // And for all their weights
                    for(int k = 0; k < layers[i].neurons[j].weights.length; k++) {
                        float previous_output = layers[i-1].neurons[k].value;
                        float error = delta*previous_output;
                        layers[i].neurons[j].weightsCache[k] = layers[i].neurons[j].weights[k] - learningRate*error;
                    }

                    layers[i].neurons[j].bias = delta*learningRate; // Update bias
                }
            }

            // Update all the weights
            for(int i = 0; i< layers.length;i++) {
                for(int j = 0; j < layers[i].neurons.length;j++) {
                    layers[i].neurons[j].updateWeights();
                }
            }
        }
    }

    /* Alternative backprop i need to recheck
    public void applyBackpropagation() {
        int nMin1Layers = layers.length-1; // All hidden layers and the output layer

        float [] targets_OutLAYER = originalAgent.getNextQmaxVal();// maxQvalnextstate
        float [] outputs_OutLAYER = originalAgent.getQval();// Qval output orgininal bot

        layers = originalAgent.getLayers(); // Put the nn back to it's state

        for (int n = 0; n < layers[nMin1Layers].neurons.length; n++) {

            float y = outputs_OutLAYER[n];
            float dy = targets_OutLAYER[n];
            float partialDerivative = (dy - y) * y * (1 - y);

            layers[nMin1Layers].neurons[n].gradient = partialDerivative;

            for (int connection = 0; connection < layers[nMin1Layers].neurons[n].weights.length; connection++) {
                float prevY = layers[nMin1Layers-1].neurons[connection].value;

                float deltaWeight = learningRate * partialDerivative * prevY;

                float newWeight = layers[nMin1Layers].neurons[n].weights[connection] + deltaWeight;
                layers[nMin1Layers].neurons[n].weightsCache[connection] = newWeight;
                //connection.setWeight(newWeight + momentum * connection.getPrevDeltaWeight());
            }

            // Update bias
            layers[nMin1Layers].neurons[n].bias = partialDerivative*learningRate;
        }

        for (int l = nMin1Layers - 1; l > 0; l--) { //TODO >=0 should be >0 maybe prob

            //recheck if needed
            for (int n = 0; n < layers[l].neurons.length; n++) {
                float y = layers[l].neurons[n].value;

                float sumOutputs = sumGradient(n,l+1);
                float partialDerivative = y * (1 - y) * sumOutputs;

                layers[l].neurons[n].gradient = partialDerivative;

                for (int connection = 0; connection < layers[l].neurons[n].weights.length; connection++) {
                    float pervY = layers[l-1].neurons[connection].value;
                    float deltaWeight = learningRate * partialDerivative * pervY;
                    float newWeight = layers[l].neurons[n].weights[connection] + deltaWeight;

                    layers[l].neurons[n].weightsCache[connection] = newWeight;

                    //connection.setWeight(newWeight + momentum * connection.getPrevDeltaWeight());
                }
                // Update bias
                layers[l].neurons[n].bias = partialDerivative*learningRate;
            }
        }


        // Update the weights
        for(int i = 0; i< layers.length;i++) {
            for(int j = 0; j < layers[i].neurons.length;j++) {
                layers[i].neurons[j].updateWeights();
            }
        }
        Visuals.printBias(layers);

    }*/


    /** Sums of all the gradient connecting a given neuron in a given layer
     * @param neuronIndex position of the neuron
     * @param layerIndex layer
     * @return sum of the gradients
     */
    // Sums of all the gradient connecting a given neuron in a given layer
    public  float sumGradient(int neuronIndex, int layerIndex) {
        float gradient_sum = 0;
        Layer current_layer = layers[layerIndex];

        for(int i = 0; i < current_layer.neurons.length; i++) {
            Neuron current_neuron = current_layer.neurons[i];
            gradient_sum += current_neuron.weights[neuronIndex]*current_neuron.gradient;
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
