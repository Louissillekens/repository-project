package code.Lets_Go_Champ;

import code.Bridge.LinkAgentNN;
import code.NN.MathWork;
import code.Screens.PuttingGameScreen;

/**
 * @author: Alexandre Martens
 */
public class GameManager {

    private final float xStart;
    private final float yStart;
    private final float xFlag;
    private final float yFlag;

    private static float xPos;
    private static float yPos;

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

        this.done = true;
        this.current_screen = true;
        this.rewards_total = 0;
    }

    /**
     * @param action action number we took [0-109]
     * @return reward of that action
     */
    public int takeAction(int action){
        int reward = 0;

        LinkAgentNN bridge = new LinkAgentNN(action);

        boolean final_destination = bridge.getWinPosition();
        boolean collision = bridge.getCollision();

        float new_xPos = bridge.getxPosition();
        float new_yPos = bridge.getyPosition();

        System.out.println("new_xPos = " + new_xPos);
        System.out.println("new_yPos = " + new_yPos);
        //System.exit(0);

        // The ball is at the final destination
        if (final_destination ==  true){
            this.done = true;

            updatePosition(new_xPos, new_yPos);

            int doneReward = 100;
            rewards_total += doneReward;

            return doneReward;
        }

        // Valid move = the ball is not at the final destination and did not collide
        else if (final_destination == false && collision == false){
            // Do not update the this.done = false because it will stay false

            reward = rewards(new_xPos, new_yPos);
            rewards_total += reward;

            updatePosition(new_xPos, new_yPos);

            return reward;
        }

        // Invalid move (collision) = the ball is not at the final destination and did collide
        else if (final_destination == false && collision == true){
            // Do not update the this.done = false because it will stay false

            int collision_reward = -100;
            rewards_total += collision_reward;

            // Do not update the position of the agent because when there is a collision it just start again
            return collision_reward;
        }

        // If something goes wrong
        return -100000;
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
    public float[] getState(){
        return LinkAgentNN.getSensors();
    }


    public float getTotalRewards() {
        return rewards_total;
    }

    /**
     * Reset the position of the agent to a starting state
     */
    public void reset(){
        if (isDone() == true){
            this.xPos = PuttingGameScreen.getStartingPositionX();
            this.yPos = PuttingGameScreen.getFlagPositionZ();
        }else {

            this.xPos = xStart;
            this.yPos = yStart;
        }
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

    public static float getxPos() {
        return xPos;
    }

    public static float getyPos() {
        return yPos;
    }
}
