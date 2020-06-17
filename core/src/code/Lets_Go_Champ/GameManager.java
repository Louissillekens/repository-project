package code.Lets_Go_Champ;

import code.NN.MathWork;

/**
 * @author: Alexandre Martens
 */
public class GameManager {

    private final float xStart;
    private final float yStart;
    private final float xFlag;
    private final float yFlag;

    private float xPos;
    private float yPos;

    private boolean done; // Tracks if we reached the flag = ended the episode
    private boolean current_screen; // Tracks if the game just started, meaning no prev actions //TODO utille pour non CNN?
    private float rewards_total;

    /**
     * @param xStart fixed starting position of the ball
     * @param yStart fixed starting position of the ball
     * @param xFlag fixed flag position
     * @param yFlag fixed flag position
     */
    GameManager(float xStart, float yStart, float xFlag, float yFlag){

        this.xStart = xStart;
        this.yStart = yStart;
        this.xFlag = xFlag;
        this.yFlag = yFlag;


        this.xPos = xStart;
        this.yPos = yStart;

        this.done = false;
        this.current_screen = true;
        this.rewards_total = 0;
    }

    /**
     * @param action action number we took [0-109]
     * @return reward of that action
     */
    int takeAction(int action){
        int reward = 0;
        /* TODO
             * Bot takes as input: action. (action [0-109])
             *   The bot then find the corresponding velocity and angle to execute
             *   The bot executes the action and updates itself on the gui
             * The bot returns: final position and boolean: arrived destination and boolean collision
             *
             * If final destination is true: this.done = true;
             *                             : updatePosition(finalPosX, finalPosY); // From bot return
             *                             : int doneReward = 100;
             *                             : rewards_total += doneReward;
             *                             : return doneReward;
             *
             * else if final destination is false and collision is false: // this.done is not updated = still false
             *                              : int reward = rewards(newXpos, newYpos);
             *                              : rewards_total += reward;  //add reward to rewards_total, used in the main
             *                              : updatePosition(finalPosX, finalPosY); // from bot return
             *                              : return reward;
             *
             * else final destination is false and collision is true: // this.done is not updated = still false
             *                              : int collision_reward = -100;
             *                              : rewards_total += collision_reward;  //add reward to rewards_total, used in the main
             *                              : DO NOT UPDATE AGENT POSTITION
             *                              : return collision_reward;
         */
        return reward;
    }

    /**
     * @param newXpos new x position of the agent after the shot
     * @param newYpos new y postition of the agent after the shot
     * @return reward of that change in postition
     */
    int rewards(float newXpos, float newYpos){

        // Calculate the rewards of going forward
        float distanceReward = 2 * MathWork.distanceGain(xPos, yPos, newXpos, newYpos, xFlag, yFlag);

        float cost_per_step = -5f; // Cost of each time hitting the ball

        float localReward = cost_per_step + distanceReward;

        return (int) localReward;
    }

    /**
     * @return sensor state values
     */
    float[] getState(){
        //float [] sensors = bot.getSensors; //TODO returns the sensors of this agent at that position
        return null; // return sensors;
    }


    public float getTotalRewards() {
        return rewards_total;
    }

    /**
     * Reset the position of the agent to a starting state
     */
    void reset(){
        this.xPos = xStart;
        this.yPos = yStart;

        this.done = false;
        this.rewards_total = 0;
    }


    /**
     * @return boolean if we started in a new state or not
     */
    boolean justStarting(){
        return current_screen;
    }


    /**
     * @param newX new x coordinate
     * @param newY new y coodinate
     */
    void updatePosition(float newX, float newY){
        this.xPos = newX;
        this.yPos = newY;
    }


    /**
     * @return number of actions possible for the agent
     */
    int numActionsAvailable(){
        return 110;
    }


    /**
     * @return boolean if the target state/final state is reached
     */
    public boolean isDone() {
        return done;
    }

}
