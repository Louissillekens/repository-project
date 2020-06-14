package code.Q_Learning;

public class Agent {
    double xPos;
    double yPos;
    double xFlag;
    double yFlag;

    double [] sensors;


    Agent(double xStart, double yStart, double xFlag, double yFlag){
        sensors  = new double[11];

        this.xFlag = xFlag;
        this.yFlag = yFlag;

        this.xPos = xStart;
        this.yPos = yStart;
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
}
