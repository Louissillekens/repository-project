package Board;

public class Friction_function implements Function2d{

    private double out_of_bounds_value;
    private double[][] frictionMap;

    public Friction_function(double[][] array, double value){

        frictionMap = array;
        out_of_bounds_value = value;
    }

    //must be redone so it works how we want to for friction
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
        double x_diff = x - Math.floor(x);
        double y_diff = y - Math.floor(y);
        double n1 = frictionMap[(int) Math.floor(x)][(int) Math.floor(y)];
        double n2;
        if (x >= frictionMap.length-1 || y >= frictionMap[0].length-1) {
            n2 = 1;
        }
        else {
            n2 =  frictionMap[(int) Math.floor(x + 1)][(int) Math.floor(y + 1)];
        }

        return ((x_diff + y_diff)/2) * (n2-n1)+n1;
    }

    @Override
    public Vector2d gradient(Vector2d p) {
        return null;
    }
}

