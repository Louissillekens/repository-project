package code.Lets_Go_Champ;


public class EpsilonGreedyStrat {

    private final float start;
    private final float end;
    private final float decay;

    /**
     * @param start Starting value of epsilon (MAX. value possible)
     * @param end Ending value of epsilon (MIN. value possible)
     * @param decay Rate at which we decrease epsilon. Over time the agent
     *              should exploit more and explore less because of the knowledge
     *              it gained over time
     */
    EpsilonGreedyStrat(float start, float end, float decay){

        this.start = start;
        this.end = end;
        this.decay = decay;
    }

    /**
     * @param current_step The step at which the agent is currently at in the environment
     * @return The epsilon (taken into account the decay ofc)
     */
    float getExplorationRate(int current_step){
        return (float) (end + (start - end)*(Math.exp(-1*decay*current_step))); // Formula
    }
}
