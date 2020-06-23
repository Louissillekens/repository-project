package code.Lets_Go_Champ;

import code.NN.ExportNeuralNets;
import code.NN.MathWork;

import java.util.List;

public class Alex_Clem {
    private static float xStart ;
    private static float yStart;
    private static float xFlag;
    private static float yFlag;

    static int memory_size = 100000; // Size of the number of experieces we can store
    public static int batch_size = 256; // Number of experiences we take from the memory

    public static float gamma = 0.9f; // Importance to future rewards

    static float eps_start = 1f;
    static float eps_end = 0.01f;
    static float eps_decay = 0.001f;

    public static int target_network_update = 10; // updating the target network every 10 episodes
    static float lr = 0.01f; // Policy nn learning rate

    private static int num_episodes = 1000; // Max number of episodes
    public float [] total_rewards_episodes;
    public GameManager gm;
    public Agent agent;
    private EpsilonGreedyStrat strategy;
    public ReplayMemory memory;
    public DQN policy_net;
    public DQN target_net;

    private int e = 0;
    public float[] state;

    private boolean destination = true; // TODO WHERE used


    public Alex_Clem(float xStart, float yStart, float xFlag, float yFlag) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xFlag = xFlag;
        this.yFlag = yFlag;

        this.gm = new GameManager(xStart,yStart,xFlag,yFlag); // Create the game manager
        this.strategy = new EpsilonGreedyStrat(eps_start, eps_end, eps_decay); // Create the strat
        this.agent = new Agent(strategy,gm.numActionsAvailable()); // Create the agent
        this.memory = new ReplayMemory(memory_size); // Create the memory

        // Create the 2 networks
        this.policy_net = new DQN(); // Create the policy network
        this.target_net = new DQN(); // Create the target network

        // Set weights of target_net(downer one) = policy_net(upper one)
        this.target_net.copyLayers(policy_net);

        // Set the network train ability (extra security)
        this.policy_net.setTrainingMode(true); // Train and evaluate
        this.target_net.setTrainingMode(false); // Only for evaluation

        // Set the activation function for the networks
        try {
            this.policy_net.setActivationFunction("relu"); // set activation function, relu by default
            this.target_net.setActivationFunction("relu"); // set activation function, relu by default
        } catch (ExceptionHandeling exceptionHandeling) {
            exceptionHandeling.printStackTrace();
        }

        // Set the learning rate of our policy_net
        this.policy_net.setLR(lr);

        // Save the networks
        ExportNeuralNets.exportNetworks(policy_net, target_net);

        this.total_rewards_episodes = new float[num_episodes];
    }

    public void executeBG(){
        for (int e = 0; e < num_episodes; e++){
            gm.reset(); //We start from the starting position
            float[] state= gm.getState(); // Get the starting state

            while (!gm.isDone()){ //Not in an ending state //TODO should put a limit of number of moves possible

                int action = agent.selectAction(state, policy_net); // Decide an action [0;109] from Q(s,a) = policy net (The agent didnt move yet)
                int reward = gm.takeAction(action); //Get the reward from that action
                System.out.println(reward);
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
                System.out.println("ntn");
            }

            total_rewards_episodes[e] = gm.getTotalRewards(); // Store the total rewards of an episode

            // Update the target_net check
            if (e % target_network_update == 0){
                target_net.copyLayers(policy_net);
            }
            ExportNeuralNets.exportNetworks(policy_net, target_net);
        }
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public static int getNum_episodes() {
        return num_episodes;
    }

    public boolean isDestination() {
        return destination;
    }

    public void setDestination(boolean destination) {
        this.destination = destination;
    }

    public float[] getState() {
        return state;
    }

    public void setState(float[] state) {
        this.state = state;
    }



}
