package code.Board;


/**
 * class for a simple Vector in 2d holding an x and y coordinate
 */
public class Vector2d {

    private double x;
    private double y;

    /**
     * parametric constructor
     * @param x x-coordinate of the vector
     * @param y y-coordinate of the vector
     */
    public Vector2d(double x, double y) {

        this.x = x;
        this.y = y;
    }

    /**
     * getter for the x-coordinate of the vector
     * @return a double holding the x-value
     */
    public double get_x() {

        return x;
    }

    /**
     * getter for the y-coordinate of the vector
     * @return a double holding the y-value
     */
    public double get_y() {

        return y;
    }

    /**
     * setter for the x-coordinate
     * @param newx the new value of x
     */
    public void change_x(double newx){
        x = newx;
    }

    /**
     * setter for the y-coordinate
     * @param newy the new value of y
     */
    public void change_y(double newy){
        y = newy;
    }

    /**
     * setter for the entire vector
     * @param newx the new value of x
     * @param newy the new value of y
     */
    public void change_both(double newx, double newy){
        x = newx;
        y = newy;
    }

    public boolean equals(Vector2d aVector){

        if(this.x == aVector.get_x() && this.y == aVector.get_y()) return true;
        return false;
    }

}
