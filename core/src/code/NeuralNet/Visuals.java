package code.NeuralNet;

import java.util.Arrays;

/**
 * @author Alexandre Martens
 */
public class Visuals extends NeuralNet {

    // Print the final result in the output layer
    public void printOutput(TrainingData[] trainingDataSet, int nFirst, String whatToPrint){
        /*TrainingData[] test = MathWork.minMaxDescaling(trainingDataSet, "input");
        test = MathWork.minMaxDescaling(test, "output");*/
        if (whatToPrint == "target"){
            System.out.println("First '" + nFirst + "' target values:");

            for (int i = 0; i < nFirst; i++){
                System.out.println(Arrays.toString(trainingDataSet[i].dataOutput));
            }
            System.out.println();
        }

        else if (whatToPrint == "results"){
            System.out.println("\n" + "First '" + nFirst + "' output values after training:");
            System.out.println("\t" + "Goal:" + "\t" + "Output:");

            for (int i = 0; i < nFirst; i++){
                forwardVisuals(trainingDataSet[i].dataInput);
                //float result = MathWork.minMaxDescalingIndividual(layers[2].neurons[0].value, "output");
                //float goal = MathWork.minMaxDescalingIndividual(trainingDataSet[i].dataOutput[0],"output");
                //System.out.println("\t" + goal + "\t" + result);
            }
            System.out.println();
        }
    }


    // Print the whole dataset, input and outputs
    public static void printDataSet(TrainingData data){
        System.out.println(Arrays.toString(data.dataInput) +
                " -> " + Arrays.toString(data.dataOutput));
    }

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

    // Print the mean square error
    public static void loss(float outputs, float targets){
        System.out.println("Mean Square Error: " + MathWork.meanSquaredError(outputs, targets));
    }

    public static void printNeuronValue(Layer[] layers, int layerN){
        for (int i = 0; i < layers[layerN].neurons.length; i++){
            System.out.println(layers[layerN].neurons[i].value);
        }
    }
}
