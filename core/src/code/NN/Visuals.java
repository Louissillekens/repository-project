package code.NN;

import code.Lets_Go_Champ.DQN;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Alexandre Martens
 */
public class Visuals extends NeuralNet implements Serializable {

    // Print the weights of the nn
    public static void printWeights(Layer[] layers, int layerN){
        for (int i = 0; i < layers[layerN].neurons.length; i++){
            System.out.println(Arrays.toString(layers[layerN].neurons[i].weights));
        }
        System.out.println("- - - - -");
    }


    public static void printAllWeights(Layer[] layers){
        for (int i = 0; i < layers.length; i++){
            for (int j = 0; j < layers[i].neurons.length; j++){
                System.out.println(Arrays.toString(layers[i].neurons[j].weights));
            }
        }
        System.out.println("- - - - -");
    }


    // Print the bias of the nn
    public static void printBias(Layer[] layers){
        for (int i = 0; i < layers.length; i++){
            for (int j = 0; j < layers[i].neurons.length; j++){
                System.out.println(layers[i].neurons[j].bias);
            }
        }
        System.out.println("- - - - -");
    }


    public static void printNeuronValue(Layer[] layers, int layerN){
        for (int i = 0; i < layers[layerN].neurons.length; i++){
            System.out.println(layers[layerN].neurons[i].value);
        }
    }

    public static void printDQN(DQN nn){
        Layer neuralnet = nn.getNn().layers[2];
        System.out.println(Arrays.toString(neuralnet.neurons[4].weights));

        /*for (int i = 0; i < neuralnet.neurons.length; i++){
            System.out.println(Arrays.toString(neuralnet.neurons[i].weights));
        }*/
    }

}
