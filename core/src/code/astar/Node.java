package code.astar;

import java.util.ArrayList;
import java.util.List;

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

    public void generateShot(){
        //TODO call generate angle and generate power to set these randomly
    }

    public void generateAngle(){
        //TODO
    }

    public void generatePower(){
        //TODO
    }

    public Node clone(){
        //TODO clone all the fields of this node to a new node
        //temp so no errors occur
        return new Node(null,0,0);
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
