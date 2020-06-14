package code.NeuralNet;
//V2.0 trying to convert from classification to regression, added visuals and random shuffle of the data

import code.Q_Learning.Agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Alexandre Martens
 */
public class NeuralNet {

    Layer[] layers;
    TrainingData data;

    float[] currentState; //Qval chosen and wich node it is
    float nextStateQ; //Qval chosen and wich node it is
    float e = 0.9f; // Exploring value TODO should decrease as it goes
    float learningRate = 0.05f;

    public NeuralNet(){
        // Set the range of the weights
        Neuron.setRangeWeight(-1,1);

        //Creating the layers
        this.layers = new Layer[4]; //3 layers: input, hidden and output
        this.layers[0] = null; // Input layer, no previous neurons: 0,2
        this.layers[1] = new Layer(11,5); // Hidden layer: 2 neurons (input layer) coming in
        this.layers[2] = new Layer(5,5); // Hidden layer: 2 neurons (input layer) coming in
        this.layers[3] = new Layer(5, 10); // Output layer: 64 neurons (hidden layer), 2 neurons as final output
        System.out.println("NeuralNet: Created");
    }

    public void createTrainingData(Agent agent) {

        double [] sensors = agent.getSensors();

        float [] dataInput = new float[sensors.length];
        float [] dataOutput = new float[layers[layers.length-1].neurons.length]; // Will stay empty for init because no target valuese ftm
        // Fill all the data inputs of the NN
        for (int i = 0; i < sensors.length; i++){
            dataInput[i] = (float) sensors[i];
        }

        data = new TrainingData(dataInput, dataOutput);

        data = MathWork.sensorScaling(data, 0, sensors.length);

        System.out.println("Dataset: Created");
    }



    // Forward propagation
    //int nPass means if it's the first forward prop or 2nd one
    public void forward(int nPass){
        layers[0] = new Layer(data.getDataInput()); // Loads the input of the data into the input layer (layer[0])

        for (int i = 1; i < layers.length; i++){ // Loop true all the layers (hidden and output) (=I)
            for (int j = 0; j < layers[i].neurons.length; j++){ // Loop true all the neurons in that layer
                float sum = 0;

                for (int k = 0; k < layers[i-1].neurons.length; k++){ // Loops true the neurons of each layer, starting from the input layer (1 layer earlier than j)
                    sum += (layers[i-1].neurons[k].value)*(layers[i].neurons[j].weights[k]); // Formula: SUM(all_weights*value_prev_neuron)
                }
                sum += layers[i].neurons[j].bias;

                if (i == layers.length-1){
                    layers[i].neurons[j].value = MathWork.relu(sum);
                }else
                    //TODO should the last layer have the sum or an activation function or scaled
                    layers[i].neurons[j].value = MathWork.relu(sum); // New value of the neuron by taking the sigmoid function
            }
        }
        if (nPass == 1){
            currentState = epsilonSelection();
        } else if (nPass == 2){
            nextStateQ = maxQVal();
        }
        Visuals.neuronValue(layers, layers.length-1);
        Visuals.showWeights(layers);
    }

    /** Exploration/Exploitation dilemma
     * TODO should decrease the e over time or shouldn't i in a nn?
     * @return array of [Qval, pos Qval]
     */
    float[] epsilonSelection(){
        float random = (float) Math.random();

        if (random < e){
            int numb = ThreadLocalRandom.current().nextInt(0, layers[layers.length-1].neurons.length);
            System.out.println(numb);
            return new float[] {layers[layers.length-1].neurons[numb].value, numb};
        } else{
            Neuron [] neur = layers[layers.length-1].neurons;
            List<Float> list = new ArrayList<Float>();

            for (int i = 0; i < neur.length; i++){
                list.add(neur[i].value);
            }
            //find pos of that max val
            float max = Collections.max(list);
            int numb = 0;
            for (int i = 0; i < neur.length; i++){
                if (neur[i].value == max){
                    numb = i;
                    break;
                }
            }
            return new float[]{max, numb};
        }
    }


    /**
     * @return maximum value of the output layer
     */
    float maxQVal(){
        float max = 0;
        Neuron [] neur = layers[layers.length-1].neurons;
        List<Float> list = new ArrayList<Float>();

        for (int i = 0; i < neur.length; i++){
            list.add(neur[i].value);
        }
        return Collections.max(list);
    }


    /** Backward propagation
     * Hardest part to implement and understand
     * Going from the end of the nn to the front, layer by layer going backwards
     * Methodology: Calculate output layer weights -> calculate the hidden layers weights -> update all weights
     * Documentation source: https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
     * TODO improve the backpropagate gradient: https://medium.com/datathings/neural-networks-and-backpropagation-explained-in-a-simple-way-f540a3611f5e
     */
    public void backpropagate(){
        float targetQ = nextStateQ - currentState[0];
        /*TODO implement rewards and costs
            *https://stats.stackexchange.com/questions/200006/q-learning-with-neural-network-as-function-approximation
          */
        int nLayers = layers.length; // Input layer, all hidden layers and the output layer
        int nMin1Layers = nLayers-1; // All hidden layers and the output layer

        // Update the output layer first
        for (int i = 0; i < layers[nMin1Layers].neurons.length; i++){
            float derivative = targetQ; // Calc how close we are from the actual expectation
            float delta = derivative *(nextStateQ*(1-nextStateQ));

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
        System.out.println("=====");
        Visuals.neuronValue(layers, layers.length-1);
        Visuals.showWeights(layers);

    }


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


    // Forward propagation FOR THE VISUAL CLASS
    public void forwardVisuals(float [] dataInput){
        layers[0] = new Layer(dataInput); // Loads the input of the data into the input layer (layer[0])

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
        Visuals.showWeights(layers);
    }
}
