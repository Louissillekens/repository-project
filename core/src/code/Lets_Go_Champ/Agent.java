package code.Lets_Go_Champ;

import code.NN.MathWork;

import java.util.Random;

public class Agent {

    private EpsilonGreedyStrat strategy;

    private int nActions;
    private int current_step; // AgentTest current step number in the environment

    /**
     * @param strategy EpsilonGreedyStrat for the epsilon and will use this to
     *                 choose if it should explore or exploit
     * @param nActions All the possible actions the agent can take (110)
     */
    Agent(EpsilonGreedyStrat strategy, int nActions){

        this.strategy = strategy;
        this.nActions = nActions;

        this.current_step = 0;
    }

    /**
     * @param state current state of the agent
     * @param policy_nn policy network to get the max Q val if needed
     * @return
     */
    int selectAction(float[] state, DQN policy_nn){
        Random random = new Random();

        float rate = strategy.getExplorationRate(current_step);
        current_step++;

        if (rate > Math.random()){
            Random rand = new Random();

            return rand.nextInt(nActions); // Explore
        }
        else {

            float [] results = policy_nn.forward(state); //Exploit, find the best action
            return MathWork.getMaxIndex(results); //  Return the position of that best action
        }
    }
}
