package code.NeuralNet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexandre Martens
 */
public class MathWork extends NeuralNet {

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

    public static float squaredCalc(float output, float target) {
        float calc = (float) Math.pow((target - output),2);
        return calc;
    }

    // Used to calculate the overall error rate
    public static float meanSquaredError(float output, float target) {
        return squaredCalc(output, target);
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
    public static TrainingData sensorScaling(TrainingData data, int minVal, int maxVal) {

        // Scale the data
        for (int i = 0; i < data.dataInput.length; i++) { // Loop true the dataset
            data.dataInput[i] = (data.dataInput[i] - minVal) / (maxVal - minVal);
        }

        return data;
    }

    // Min-Max descaling
    public static TrainingData sensorDescaling(TrainingData data, int minVal, int maxVal) {

        // Descale the data
        for (int i = 0; i < data.dataInput.length; i++) { // Loop true the dataset
            data.dataInput[i] = (data.dataInput[i] * (maxVal - minVal) + minVal);
        }

        return data;
    }

    public float pythFlag(float xO, float yO){
        float deltaX = originalAgent.getxFlag() - xO;
        float deltaY = originalAgent.getyFlag() - yO;

        return (float) Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));
    }

    // Min-Max descaling
/*
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
*/

/*
    public static float minMaxDescalingIndividual(float val, String IorO) {
        if (IorO == "input"){
            return ((val) * (maxInput1 - minInput1) + minInput1);
        } else {
            return ((val) * (maxOutput1 - minOutput1) + minOutput1);
        }
    }
*/
}
