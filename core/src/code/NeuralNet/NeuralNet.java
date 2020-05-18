package code.NeuralNet;
//V2.0 trying to convert from classification to regression, added visuals and random shuffle of the data

/**
 * @author Alexandre Martens
 */
public class NeuralNet {

    static Layer[] layers;
    static TrainingData[] trainingDataSet;

    // Main method
    public static void main(String[] args) {
        System.out.println("\n" + "NeuralNet is creating the network... ");

        // Set the range of the weights
        Neuron.setRangeWeight(-1,1);

        //Creating the layers
        layers = new Layer[3]; //3 layers: input, hidden and output
        layers[0] = null; // Input layer, no previous neurons: 0,2
        layers[1] = new Layer(2,64); // Hidden layer: 2 neurons (input layer) coming in
        layers[2] = new Layer(64, 2); // Output layer: 64 neurons (hidden layer), 2 neurons as final output

        System.out.println("Network creation: Check" + "\n");
        createTrainingData();

        // Print target outputs
        //Visuals.printOutput(trainingDataSet, 10, "target");

        System.out.println("\n" + "Training is starting... " + "\n");

        train(500000, 0.05f); // The small 'f' is to explicitly state it's a float and not a double

        System.out.println("\n" + "Training: Check");

        // Print results
        Visuals.printOutput(trainingDataSet, 10, "results");

        /*// Test with an unknown value
        float[] testValuesInput = {20};
        float[] testValuesOutput = {25};


        TrainingData testInput = new TrainingData(testValuesInput, testValuesOutput);
        forward(testInput.dataInput);

        System.out.println("New data final result: " + layers[2].neurons[0].value);*/
    }


    public static void createTrainingData() {
        // FUNCTION: float field1 = (float) (((Math.sin(i) + Math.sin(j))/3)+0.5)
        // RESISTANCE:
        // Bot x,y starting always at 0.0

        int angleRange = 20;
        int veloRange = 10;

        System.out.println("Data managing is starting... ");

        FileMaker file = new FileMaker(angleRange,veloRange);
        file.createFile();
        file.convertFile();
        trainingDataSet = FileMaker.getTrainingDataSet();

        // Shuffle the data
        trainingDataSet = MathWork.randomShuffle(trainingDataSet);

        // Print the whole dataset
        // Visuals.printDataSet(trainingDataSet);

        // Scale the whole dataset for the training
        MathWork.minMaxScaling(trainingDataSet,"input"); // Inputs
        MathWork.minMaxScaling(trainingDataSet, "output");// Outputs
        System.out.println("\t" + "Data has been scaled for the NeuralNet!");

        // Print the whole scaled dataset
        // Visuals.printDataSet(trainingDataSet);

        System.out.println("Data managing: Check");


    }

    // Train the network with forward and backward propagation
    // Epoch is 1 iteration true the whole training dataset
    public static void train(int epochs, float learningRate){
        float[] targets = new float[trainingDataSet.length];

        MathWork.minMaxDescaling(trainingDataSet, "output");
        for (int i = 0; i < trainingDataSet.length; i++){
            targets[i] = trainingDataSet[i].dataOutput[0];
        }
        MathWork.minMaxScaling(trainingDataSet, "output");

        for (int i = 0; i < epochs; i++){

            float[] outputs = new float[trainingDataSet.length];

            for (int j = 0; j < trainingDataSet.length; j++){
                forward(trainingDataSet[j].dataInput);
                backward(learningRate, trainingDataSet[j]);

                outputs[j] = MathWork.minMaxDescalingIndividual(layers[2].neurons[0].value, "output");
            }
            if (i%1000 == 0) {
                Visuals.loss(outputs, targets);
                System.out.println("Epoch: " + i + "/" + epochs);
            }
        }
    }

    // Forward propagation
    public static void forward(float[] inputs){
        layers[0] = new Layer(inputs); // Loads the input of the data into the input layer (layer[0])

        for (int i = 1; i < layers.length; i++){ // Loop true all the layers (hidden and output) (=I)
            for (int j = 0; j < layers[i].neurons.length; j++){ // Loop true all the neurons in that layer
                float sum = 0;

                for (int k = 0; k < layers[i-1].neurons.length; k++){ // Loops true the neurons of each layer, starting from the input layer (1 layer earlier than j)
                    sum += (layers[i-1].neurons[k].value)*(layers[i].neurons[j].weights[k]); // Formula: SUM(all_weights*value_prev_neuron)
                }
                sum += layers[i].neurons[j].bias;
                layers[i].neurons[j].value = MathWork.sigmoid(sum); // New value of the neuron by taking the sigmoid function
            }
        }
    }

    /** Backward propagation
     * Going from the end of the nn to the front, layer by layer going backwards
     * Methodology: Calculate output layer weights -> calculate the hidden layers weights -> update all weights
     * Documentation source: https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
     * tdata is loaded as parameter because we want to check the expected (right answer) output
     * TODO improve the backprop gradient: https://medium.com/datathings/neural-networks-and-backpropagation-explained-in-a-simple-way-f540a3611f5e
     */
    public static void backward(float learningRate, TrainingData tData){

        int nLayers = layers.length; // Input layer, all hidden layers and the output layer
        int nMin1Layers = nLayers-1; // All hidden layers and the output layer

        // Update the output layer first
        for (int i = 0; i < layers[nMin1Layers].neurons.length; i++){
            float output = layers[nMin1Layers].neurons[i].value; // Value of the current neuron
            float target = tData.dataOutput[i]; // Expected value (good answer)
            float derivative = output-target; // Calc how close we are from the actual expectation
            float delta = derivative *(output*(1-output));

            layers[nMin1Layers].neurons[i].gradient = delta;

            for (int j = 0; j < layers[nMin1Layers].neurons[i].weights.length; j++){
                float previous_output = layers[nMin1Layers-1].neurons[j].value;
                float error = delta*previous_output;
                layers[nMin1Layers].neurons[i].weightsCache[j] = layers[nMin1Layers].neurons[i].weights[j] - learningRate*error; // Formula: New weight = old weight — Derivative * learning rate
            }

            // Update bias
            layers[nMin1Layers].neurons[i].bias = delta*learningRate;
        }


        // Update the hidden layers
        for(int i = nMin1Layers-1; i > 0; i--) {
            // For all neurons in that layers
            for(int j = 0; j < layers[i].neurons.length; j++) {
                float output = layers[i].neurons[j].value;
                float gradient_sum = sumGradient(j,i+1);
                float delta = (gradient_sum)*(output*(1-output));

                layers[i].neurons[j].gradient = delta;

                // And for all their weights
                for(int k = 0; k < layers[i].neurons[j].weights.length; k++) {
                    float previous_output = layers[i-1].neurons[k].value;
                    float error = delta*previous_output;
                    layers[i].neurons[j].weightsCache[k] = layers[i].neurons[j].weights[k] - learningRate*error;
                }

                // Update bias
                layers[i].neurons[j].bias = delta*learningRate;
            }
        }

        // Update the weights
        for(int i = 0; i< layers.length;i++) {
            for(int j = 0; j < layers[i].neurons.length;j++) {
                layers[i].neurons[j].updateWeights();
            }
        }
    }

    // Sums of all the gradient connecting a given neuron in a given layer
    public static float sumGradient(int neuronIndex, int layerIndex) {
        float gradient_sum = 0;
        Layer current_layer = layers[layerIndex];
        for(int i = 0; i < current_layer.neurons.length; i++) {
            Neuron current_neuron = current_layer.neurons[i];
            gradient_sum += current_neuron.weights[neuronIndex]*current_neuron.gradient;
        }
        return gradient_sum;
    }

}
