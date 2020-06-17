package code.Q_Learning;

import code.NeuralNet.Layer;

public class Agent {
    float xPos;
    float yPos;
    float xFlag;
    float yFlag;

    float rewards;

    float [] sensors;

    float [] Qval; //Used to store the originalAgent Qvalues to avoid losing them
    float [] nextQmaxVal; //Used to store the (Reward + maxQ(s',a')) of each Qval

    Layer [] layers; //Save the whole network bias, weights and values //TODO optimise this by removing the qval i think

    Agent(float xStart, float yStart, float xFlag, float yFlag){
        sensors  = new float[11];

        rewards = 0;

        this.xFlag = xFlag;
        this.yFlag = yFlag;

        this.xPos = xStart;
        this.yPos = yStart;

        Qval = new float[sensors.length*10];
        nextQmaxVal = new float[Qval.length];
    }

    public void giveAction(int actionNumber){
        /*TODO
            * map the actionN to the velocity and angle the ball needs to take
            * enter that angle and velo in the solver
            * get the new pos and move the agent to that pos
            * update the position of the agent and it's sensors
            *
        * */
    }

    void updatePos(int x, int y){
        this.xPos += x;
        this.yPos += y;
    }

    void updateSensors(float [] sensors){
        this.sensors = sensors;
    }

    void reset(float xStart, float yStart){
        this.xPos = xStart;
        this.yPos = yStart;

        this.rewards = 0;

        float [] sensors = null;
        float [] Qval = null;
        float [] nextQmaxVal = null;

        Layer [] layers = null;

    }

    public void setSensors(float[] sensors) {
        this.sensors = sensors;
    }

    public void setQval(float[] qval) {
        Qval = qval;
    }

    public void setNextQmaxVal(int position, float maxQval) {
        nextQmaxVal[position] = maxQval;
    }

    public void setLayers(Layer[] layers) {
        this.layers = layers;
    }

    public Layer[] getLayers() {
        return layers;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public float getxFlag() {
        return xFlag;
    }

    public float getyFlag() {
        return yFlag;
    }

    public float[] getSensors() {
        return sensors;
    }

    public float[] getQval() {
        return Qval;
    }

    public float[] getNextQmaxVal() {
        return nextQmaxVal;
    }
}
