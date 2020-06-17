package code.Lets_Go_Champ;

import code.NN.MathWork;

import java.util.List;

public class Qvalues {

    static float[] actionCache; // Keeps track of which action has been taken in each epsilon but is now in 1 vector.

    /**
     * Note that we'll only extract the State and the Action.
     * The next state and reward of the experience object is not needed.
     * @param policy_net The neural network we need to perform forward prop to get the q values
     * @param experiences the batch
     * @return array with all the q values corresponding to the action-state pair
     */
    static float[] getCurrent(DQN policy_net, List<Experience> experiences){
        float[] q_values = new float[experiences.size()]; // Store all Qvalues of action-state pair

        //For each sample
        for (int i = 0; i < experiences.size(); i++){
            float[] inputs = experiences.get(i).getState(); // Get the initial state
            float[] output = policy_net.forward(inputs); // Get all the output values

            q_values[i] = output[experiences.get(i).getAction()]; // Get the q value of the action
            actionCache[i] = experiences.get(i).getAction(); // Will be used in backprop to map loss and action neuron
        }
        return q_values;
    }

    /**
     * @param target_net The neural network we need to perform forward prop to get the q values
     * @param experiences the batch
     * @return array with the max q value corresponding to the state'
     */
    static float[] getNext(DQN target_net, List<Experience> experiences){
        float[] max_q_values = new float[experiences.size()]; // Store all max Qvalues of state'

        //For each sample
        for (int i = 0; i < experiences.size(); i++){
            float[] inputs = experiences.get(i).getNextState(); // Get the next state
            float[] output = target_net.forward(inputs); // Get all the output values

            max_q_values[i] = MathWork.getMaxValue(output); // Get the max q value
        }
        return max_q_values;
    }

    /**
     * @param next_q_values The already calculated next state q* values
     * @param experiences the batch
     * @param gamma the 'importance to future rewards' parameter
     * @return array with target values
     */
    static float[] getTarget(float[] next_q_values, List<Experience> experiences, float gamma){
        float[] target_q_values = new float[experiences.size()]; // Store all target values

        //For each sample
        for (int i = 0; i < experiences.size(); i++){
            target_q_values[i] = (next_q_values[i]*gamma) + experiences.get(i).getReward(); // Formula
        }
        return target_q_values;
    }

    public static float[] getActionCache() {
        return actionCache;
    }
}
