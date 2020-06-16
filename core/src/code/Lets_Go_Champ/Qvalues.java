package code.Lets_Go_Champ;

import java.util.List;

public class Qvalues {

    static float[] getCurrent(DQN nn, List<Experience> experiences){
        float[] q_values = new float[experiences.size()]; // Store all Qvalues of action-state pair

        //For each sample
        for (int i = 0; i < experiences.size(); i++){
            float[] inputs = experiences.get(i).getState(); // Get the initial state
            float[] output = nn.forward(inputs); // Get all the output values

            q_values[i] = output[experiences.get(i).getAction()]; // Get the q value of the action
        }
        return q_values;
    }

    static float[] getNext(DQN nn, List<Experience> experiences){

    }

}
