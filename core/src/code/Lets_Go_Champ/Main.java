package code.Lets_Go_Champ;

import code.NN.MathWork;

import java.util.List;

/**
 * @author Alexandre Martens
 */
public class Main {

    static int memory_size = 100000; // Size of the number of experieces we can store
    static int batch_size = 256; // Number of experiences we take from the memory

    static float gamma = 0.9f; // Importance to future rewards

    static float eps_start = 1f;
    static float eps_end = 0.01f;
    static float eps_decay = 0.001f;

    static  int target_update = 10; // updating the target network every 10 episodes
    static  float lr = 0.01f; // Policy nn learning rate

    static int num_episodes = 1000; // Max number of episodes

    //TODO remove these TESTING PURPOSES
    private static final float xStart = 10;
    private static final float yStart = 10;
    private static final float xFlag = 19;
    private static final float yFlag = 17;

    private static int maxSteps = 25;

    public static void main(String[] args) {
        GameManager gm = new GameManager(xStart,yStart,xFlag,yFlag); // Create the game manager
        EpsilonGreedyStrat strategy = new EpsilonGreedyStrat(eps_start, eps_end, eps_decay); // Create the strat
        Agent agent = new Agent(strategy,gm.numActionsAvailable()); // Create the agent
        ReplayMemory memory = new ReplayMemory(memory_size); // Create the memory

        // Create the 2 networks
        DQN policy_net = new DQN(); // Create the policy network
        DQN target_net = new DQN(); // Create the target network

        // Set weights of target_net(downer one) = policy_net(upper one)
        target_net.copyLayers(policy_net);

        // Set the network train ability (extra security)
        policy_net.setTrainingMode(true); // Train and evaluate
        target_net.setTrainingMode(false); // Only for evaluation

        // Set the learning rate of our policy_net
        policy_net.setLR(lr);

        float[] total_rewards_episodes = new float[num_episodes]; //TODO Stores the total rewards obtained per episode

        for (int e = 0; e < num_episodes; e++){
            gm.reset(); //We start from the starting position
            float[] state= gm.getState(); // Get the starting state

            while (!gm.isDone()){ //Not in an ending state //TODO should put a limit of number of moves possible
                int action = agent.selectAction(state, policy_net); // Get an action [0;109] (The agent didnt move yet)
                int reward = gm.takeAction(action); //Get the reward from that action
                float[] next_state = gm.getState(); // Get the new state (has been updated in takeAction)

                memory.push(new Experience(state, action, next_state, reward)); // Add a new experience of this step
                state = next_state; //Update the new position numerically

                // If the number of experiences in the memory is >= batch size we need
                if (memory.canProvideSample(batch_size)){
                    List<Experience> experiences = memory.getSample(batch_size);

                    //First get arrays of Q(s'a) and Q'(s',a)
                    float[] current_q_values = Qvalues.getCurrent(policy_net, experiences); // Compute the current Q values
                    float[] next_q_values = Qvalues.getNext(target_net, experiences); // Compute the next state action pair Q max value

                    float[] target_q_values = Qvalues.getTarget(next_q_values, experiences, gamma); // Apply the formula to it
                    float[] loss = MathWork.squaredError(current_q_values, target_q_values); // Compute the loss for every current-target pair

                    policy_net.backprop(loss, Qvalues.getActionCache()); // Backprop the loss to update the weights and bias

                }

            }

            // Update the target_net
            if (e % target_update == 0){
                target_net.copyLayers(policy_net);
            }

            total_rewards_episodes[e] = gm.getRewards_total(); // Store the total rewards of an episode

        }

    }
}
