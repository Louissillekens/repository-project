package code.Lets_Go_Champ;

public class Experience {
    private final float[] state;
    private final int action;
    private final float[] nextState;

    private final float reward;

    public Experience(float[] state, int action, float[] nextState, float reward){

        this.state = state;
        this.action = action;
        this.nextState = nextState;
        this.reward = reward;
    }

    public float[] getState() {
        return state;
    }

    public int getAction() {
        return action;
    }

    public float[] getNextState() {
        return nextState;
    }

    public float getReward() {
        return reward;
    }

}
