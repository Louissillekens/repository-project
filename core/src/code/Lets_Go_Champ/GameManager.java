package code.Lets_Go_Champ;
//TODO need to adapt this
public class GameManager {

    private final float xStart;
    private final float yStart;
    private final float xFlag;
    private final float yFlag;

    private float xPos;
    private float yPos;

    private boolean done; // Tracks if we reached the flag = ended the episode
    private boolean current_screen; // Tracks if the game just started, meaning no prev actions //TODO utille pour non CNN?


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
        this.current_screen = false;
    }

    int takeAction(int action){
        int reward = 0;
        /* TODO
            * Map action to the velo/angle
            * Calc final position
            * Calc if we're in a final position: this.done = a method that checks it.
            * Calc reward with the substraction of A-A' where A is distance hole-agent
            * => Check the commented MathWorks method 'pyth'
            * I should also add penalties by taking more steps
            * updatePosition(xnew, ynew); From the new position
            * (comes after the reward calc because we still need the 'old' state)
            * Use the commented reward method
            * render the new position
            *
         */
        return reward;
    }

    /*int rewards(){
        localReward = costStep;

        //Perform the distance calc between the old state and the new one to check if the ball has advanced or not
        MathWork math = new MathWork();
        localReward += (math.pythFlag(originalAgent.getxPos(), originalAgent.getyPos()) - math.pythFlag(tempAgent.getxPos(), tempAgent.getyPos()));
        return localReward;
    }*/

    float[] getState(){
        //TODO returns the sensors of this agent at that position
        return null; //RETURNS NULL, WAITING TO IMPLEMETN THE AGENT OF CLEMENT
    }


    /**
     * Reset the position of the agent to a starting state
     */
    void reset(){
        this.xPos = xStart;
        this.yPos = yStart;
    }

    /**
     * @return boolean if we started in a new state or not
     */
    boolean justStarting(){
        return current_screen;
    }

    // TODO Should send the final action taken and render it on the gui (Agent moved)
    void render(){
        //TODO complete here
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
        return 110; //TODO should find a way to not make this like it is, method to calc
    }

    /**
     * @return bolean if the target state/final state is reached
     */
    public boolean isDone() {
        return done;
    }
}
