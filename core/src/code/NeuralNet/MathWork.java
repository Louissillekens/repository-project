package code.NeuralNet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MathWork {
    private static float minInput1;
    private static float minInput2;
    private static float minOutput1;
    private static float minOutput2;

    private static float maxInput1;
    private static float maxInput2;
    private static float maxOutput1;
    private static float maxOutput2;


    /**
     * @param min minimum value in the range inclusive
     * @param max maximum value in the range exclusive
     * @return random number (pos or neg) in the given range
     */
    public static float randomN(float min, float max) {
        float d = (float) Math.random();
        float number = min + (float) Math.random() * (max - min);

        if (d < 0.5)
            return number;
        else
            return -number;
    }

    // Sigmoid function
    public static float sigmoid(float s) {
        return (float) (1 / (1 + Math.pow(Math.E, -s)));
    }

    // Derivative of the sigmoid function
    public static float SigmoidDerivative(float s) {
        return sigmoid(s) * (1 - sigmoid(s));
    }

    // Re function
    public static float relu(float s) {
        return (float) Math.max(0.0, s);
    }

    // Used for the backpropagation
    public static float squaredError(float output, float target) {
        float calc = (float) (0.5 * Math.pow(2,(target - output)));
        return calc;
    }

    // Used to calculate the overall error rate
    public static float sumSquaredError(float[] outputs, float[] targets) {
        float sum = 0;

        for (int i = 0; i < outputs.length; i++) {
            sum += squaredError(outputs[i], targets[i]);
        }
        return sum;
    }

    public static float squareMCalc(float output, float target) {
        float calc = (float) Math.pow((target - output),2);
        return calc;
    }

    // Used to calculate the overall error rate
    public static float meanSquaredError(float[] outputs, float[] targets) {
        float sum = 0;

        for (int i = 0; i < outputs.length; i++) {
            sum += squareMCalc(outputs[i], targets[i]);
        }
        return sum/(float) outputs.length;
    }

    //Random shuffle an array
    public static TrainingData[] randomShuffle(TrainingData[] data) {
        List<TrainingData> list = Arrays.asList(data); // Convert array to list
        Collections.shuffle(list); // Shuffle the list

        //Convert list to array
        data = new TrainingData[list.size()];
        data = list.toArray(data);

        return data;
    }

    // Min-Max scaling
    public static TrainingData[] minMaxScaling(TrainingData[] data, String IorO) {

        if (IorO == "input") {
            float[] array1 = new float[data.length]; // Length dataset
            float[] array2 = new float[data.length]; // Length dataset

            // Find the min and max in the dataset input 1
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                array1[i] = data[i].dataInput[0];
            }
            // Find the min and max in the dataset input 2
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                array2[i] = data[i].dataInput[1];
            }

            Arrays.sort(array1); // Sort the array to get the min first and max last
            Arrays.sort(array2); // Sort the array to get the min first and max last

            minInput1 = array1[0];
            maxInput1 = array1[data.length - 1];
            minInput2 = array2[0];
            maxInput2 = array2[data.length - 1];

            // Scale the data 1
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                    data[i].dataInput[0] = (data[i].dataInput[0] - minInput1) / (maxInput1 - minInput1);
                }

            // Scale the data 2
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                    data[i].dataInput[1] = (data[i].dataInput[1] - minInput2) / (maxInput2 - minInput2);
                }

            return data;

    } else {
            float[] array1 = new float[data.length]; // Length dataset
            float[] array2 = new float[data.length]; // Length dataset

            // Find the min and max in the dataset output 1
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                array1[i] = data[i].dataOutput[0];
            }
            // Find the min and max in the dataset output 2
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                array2[i] = data[i].dataOutput[1];
            }

            Arrays.sort(array1); // Sort the array to get the min first and max last
            Arrays.sort(array2); // Sort the array to get the min first and max last

            minOutput1 = array1[0];
            maxOutput1 = array1[data.length - 1];
            minOutput2 = array2[0];
            maxOutput2 = array2[data.length - 1];

            // Scale the data 1
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                data[i].dataOutput[0] = (data[i].dataOutput[0] - minOutput1) / (maxOutput1 - minOutput1);
            }

            // Scale the data 2
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                data[i].dataOutput[1] = (data[i].dataOutput[1] - minOutput2) / (maxOutput2 - minOutput2);
            }

            return data;
    }
}

    // Min-Max descaling
    public static TrainingData[] minMaxDescaling(TrainingData[] data, String IorO) {

        if (IorO == "input") {
            // Descale the input data
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                    data[i].dataInput[0] = (data[i].dataInput[0] * (maxInput1 - minInput1) + minInput1);
            }
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                data[i].dataInput[1] = (data[i].dataInput[1] * (maxInput2 - minInput2) + minInput2);
            }
            return data;

        } else {
            // Descale the output data
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                    data[i].dataOutput[0] = (data[i].dataOutput[0] * (maxOutput1 - minOutput1) + minOutput1);
            }
            for (int i = 0; i < data.length; i++) { // Loop true the dataset
                data[i].dataOutput[1] = (data[i].dataOutput[1] * (maxOutput2 - minOutput2) + minOutput2);
            }
            return data;
        }
    }

    public static float minMaxDescalingIndividual(float val, String IorO) {
        if (IorO == "input"){
            return ((val) * (maxInput1 - minInput1) + minInput1);
        } else {
            return ((val) * (maxOutput1 - minOutput1) + minOutput1);
        }
    }
}
