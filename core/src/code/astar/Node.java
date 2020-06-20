package code.astar;


import code.Physics.Rungekuttasolver;
import code.Screens.PuttingGameScreen;

import java.util.Random;

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

    /**
     * standard constructor for a node
     * @param parent the parent of the node
     * @param x the x-coordinate of the node
     * @param z the z-coordinate of the node
     */
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

    /**
     * generate a random shot with an angle and shot power
     * @param min min shot power
     * @param max max shot power
     */
    public void generateShot(double min, double max){
        generateAngle();
        generatePower(min, max);
    }

    /**
     * generate a random angle for the shot to be taken
     */
    public void generateAngle(){
        Random r = new Random();
        int min = 0;
        int max = 360;
        angle = min + (max - min) * r.nextDouble();

    }

    /**
     * generate a random power for a shot within a given range
     * @param min the minimum power
     * @param max the maximum power
     */
    public void generatePower(double min, double max){

        Random r = new Random();
        power = min + (max - min) * r.nextDouble();
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
    public double CalculateDistTo(double x, double z){

        double x_dist = Math.abs(x - this.getX());
        double y_dist = Math.abs(z - this.getZ());

        return Math.sqrt((x_dist*x_dist) + (y_dist*y_dist));
    }

    /**
     * this method simulates the shot internally and returns the x and y coordinates of the resulting location
     * @return returns a double array of length 2 holding an x and y coordinate
     */
    public double[] executeShot(){

        //Convert degrees to radians, radians is the argument for Math.sin or Math.cos
        double angle = (getAngle()*Math.PI)/180;

        //Split the velocity vector into x,y components
        double vxi = (getPower())*Math.cos(angle);
        double vyi = (getPower())*Math.sin(angle);

        Rungekuttasolver rk = new Rungekuttasolver();
        System.out.println("starts calculations for location now");
        return  rk.startRK4(getX(), getZ(), vxi, vyi);
    }



    /**
     * checks if the node has a parent
     * @return boolean containing whether the node has a parent or not
     */
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

    @Override
    public String toString(){
        //deliberately did not give info about parent as it would give too much info
        return "Node with coordinates (" + getX() + ", " + getZ() + "),\n" +
                " an angle of " + getAngle() + " and a power of " + getPower() +
                "It has a score of " + getScore();
    }
}
