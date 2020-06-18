package code.astar;


public class Node {

    private final Node parent;
    private double x;
    private double z;
    private double angle;
    private double power;
    //this boolean will be set to true when we already computed the next nodes from here
    private boolean checked;
    //score should always be a double between 0 and 1
    private double score;

    public Node(Node parent, double x, double z){

        this.parent = parent;
        this.x = x;
        this.z = z;
        angle = 0;
        power = 0;
        checked = false;
        score = 0;
    }

    /**
     * make a node with a given score
     * P.S. this method exists so that we can make a node that scores 0 easily
     * necessary for some methods to work correctly
     * @param score the score this node has
     */
    public Node(double score){
        this.parent = null;
        this.score = score;
    }
    //optimisation idea: make a method that creates a shot specifically directed towards the goal
    // we call this method to make one of the ten shots for each node
    //this allows us to be sure that when the goal and the ball have no object inbetween
    // at least one shot will go in the right direction

    public void generateShot(){
        //TODO call generate angle and generate power to set these randomly
    }

    public void generateAngle(){
        //TODO generate a number between 0 and 360 for the angle
    }

    public void generatePower(double min, double max){
        //TODO generate a number between max power on the given course and the min power (perhaps slightly higher as 0.001 is not very useful)
        //this is why the min and max power arte given
    }

    /**
     * this method makes a new node that has the same location and parent as the original node
     * @return
     */
    public Node partialClone(){
        Node answer = new Node(this.getParent(), this.getX(),this.getZ());
        return answer;
    }

    /**
     * calculates the distance to another location by using the pythagorean theorem
     * @param x x-coordinate of location
     * @param z z-coordinate of location
     * @return distance between locations
     */
    public double CalculateDistTo(int x, int z){

        double x_dist = Math.abs(x - this.getX());
        double y_dist = Math.abs(z - this.getZ());

        return Math.sqrt((x_dist*x_dist) + (y_dist*y_dist));
    }

    public double[] executeShot(){
        //TODO should use one of the solvers to execute the shot and then return the data of the new location
        //TODO in an array of length 2 (x and y)
        //temp
        double[] data ={0,0};
        return data;
    }

    public boolean hasParent(){
        if(this.getParent() == null) return false;
        return true;
    }

    public Node getParent() {
        return parent;
    }

    public double getX(){
        return x;
    }

    public double getZ(){
        return z;
    }

    public double getAngle() {
        return angle;
    }

    public double getPower() {
        return power;
    }

    public boolean isChecked() {
        return checked;
    }

    public double getScore() {
        return score;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
