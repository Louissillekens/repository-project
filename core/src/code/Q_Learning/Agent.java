package code.Q_Learning;

import code.NeuralNet.Layer;

public class Agent {
    double xPos;
    double yPos;
    double xFlag;
    double yFlag;

    double [] sensors;

    float [] Qval;
    float [] nextQmaxVal;
    Layer [] layers;

    Agent(double xStart, double yStart, double xFlag, double yFlag){
        sensors  = new double[11];

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

    void updateSensors(double [] sensors){
        this.sensors = sensors;
    }

    void reset(int xStart, int yStart){
        this.xPos = xStart;
        this.yPos = yStart;
    }

    public void setSensors(double[] sensors) {
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

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public double getxFlag() {
        return xFlag;
    }

    public double getyFlag() {
        return yFlag;
    }

    public double[] getSensors() {
        return sensors;
    }

    public float[] getQval() {
        return Qval;
    }

    public float[] getNextQmaxVal() {
        return nextQmaxVal;
    }
}
