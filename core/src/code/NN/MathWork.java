package code.NN;

import java.util.ArrayList;
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

    /** Sigmoid activation function
     * @param s input value we wanna squeeze between 0-1
     * @return squeezed s
     */
    public static float sigmoid(float s) {
        return (float) (1 / (1 + Math.pow(Math.E, -s)));
    }

    // Derivative of the sigmoid function
    public static float SigmoidDerivative(float s) {
        return sigmoid(s) * (1 - sigmoid(s));
    }

    /** Relu activation function
     * @param s value for the relu funciton
     * @return floaat that is not negative
     */
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


    /** Loss function SE for ARRAYS!
     * @param output takes an array with 1pos = 1 output
     * @param target takes an array with 1pos = 1 target
     * @return array with 1 pos = mse for that specific output pos and target pos
     */
    // Used to calculate the overall error rate
    public static float[] squaredError(float[] output, float[] target) {
        float[] se = new float[output.length];

        for (int i = 0; i < se.length; i++){
            se[i] = (float) Math.pow((target[i] - output[i]),2); // Calc SE for every output-target pair
        }

        return se;
    }


    /**
     * @param xO position next state x
     * @param yO position next state y
     * @return //TODO is this returning the difference of 2 distance values?
     */
    /*   public float pythFlag(float xO, float yO){
        float deltaX = originalAgent.getxFlag() - xO;
        float deltaY = originalAgent.getyFlag() - yO;

        return (float) Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));
    }*/

    /**
     * @return The the max index of an array
     */
    public static int getMaxIndex(float[] t){
        List<Float> list = new ArrayList<Float>();

        for (int i = 0; i < t.length; i++){
            list.add(t[i]);
        }
        return list.indexOf(Collections.max(list));
    }


    /**
     * @return The the max value of an array
     */
    public static Float getMaxValue(float[] t){
        List<Float> list = new ArrayList<Float>();

        for (int i = 0; i < t.length; i++){
            list.add(t[i]);
        }
        return Collections.max(list);
    }

}
