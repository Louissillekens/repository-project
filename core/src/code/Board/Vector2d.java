package code.Board;

public class Vector2d {

    private double x;
    private double y;

    public Vector2d(double x, double y) {

        this.x = x;
        this.y = y;
    }

    // Get the x position of a vector
    public double get_x() {

        return x;
    }

    // Get the y position of a vector
    public double get_y() {

        return y;
    }
    public void change_x(double newx){
        x = newx;
    }
    public void change_y(double newy){
        y = newy;
    }
    public void change_both(double newx, double newy){
        x = newx;
        y = newy;
    }

}
