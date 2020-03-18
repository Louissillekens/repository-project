package src.Board;

public class Friction_function implements Function2d{

    private double out_of_bounds_value;
    private double[][] frictionMap;

    public Friction_function(double[][] array, double value){

        frictionMap = array;
        out_of_bounds_value = value;
    }

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

    @Override
    public Vector2d gradient(Vector2d p) {
        return null;
    }
}
