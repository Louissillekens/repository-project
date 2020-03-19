package code.Board;

/**
 * class that holds the friction of the course at multiple points
 * implements interface Function2d
 */
public class Friction_function implements Function2d{

    private double out_of_bounds_value;
    private double[][] frictionMap;

    /**
     * parametric constructor
     * @param array array that holds the friction at various vectors of the course
     * @param value a value that gives the friction everywhere outside the array
     */
    public Friction_function(double[][] array, double value){

        frictionMap = array;
        out_of_bounds_value = value;
    }

    /**
     * evaluates a certain vector (finds the value of friction at this point, based on the array
     * @param p a vector to check friction for
     * @return double value representing the friction at point p
     */
    @Override
    public double evaluate(Vector2d p) {

        double x = p.get_x();
        double y = p.get_y();
        if (x < 0 || y < 0 || x > frictionMap.length-1 || y > frictionMap[0].length-1) {
            return out_of_bounds_value;
        }
        if (x == Math.floor(x) && y == Math.floor(y)) {
            return frictionMap[(int) x][(int) y];
        }
        double x_diff = Math.floor(x);
        double y_diff = Math.floor(y);

        return frictionMap[(int) x_diff][(int) y_diff];
    }

    /**
     * gadient method is never used for friction
     * @param p point to look at
     * @return null
     */
    @Override
    public Vector2d gradient(Vector2d p) {
        return null;
    }
}
