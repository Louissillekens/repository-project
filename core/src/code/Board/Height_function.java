package code.Board;

public class Height_function implements Function2d{


    private double out_of_bounds_value;
    private double[][] heightMap;

    public Height_function(double[][] array, double value){

        heightMap = array;
        out_of_bounds_value = value;
    }

    @Override
    public double evaluate(Vector2d p) {

        double x = p.get_x();
        double y = p.get_y();
        if (x < 0 || y < 0 || x > heightMap.length-1 || y > heightMap[0].length-1) {
            return out_of_bounds_value;
        }
        if (x == Math.floor(x) && y == Math.floor(y)) {
            return heightMap[(int) x][(int) y];
        }
        double x_diff = x - Math.floor(x);
        double y_diff = y - Math.floor(y);
        double n1 = heightMap[(int) Math.floor(x)][(int) Math.floor(y)];
        double n2;
        if (x >= heightMap.length-1 || y >= heightMap[0].length-1) {
            n2 = 1;
        }
        else {
            n2 =  heightMap[(int) Math.floor(x + 1)][(int) Math.floor(y + 1)];
        }

        return ((x_diff + y_diff)/2) * (n2-n1)+n1;
    }


    @Override
    public Vector2d gradient(Vector2d p) {

        double x = p.get_x();
        double y = p.get_y();

        double x1;
        double y1;

        double step = 0.1;

        x1 = evaluate(new Vector2d(x+step, y)) - evaluate(new Vector2d(x-step, y));
        y1 = evaluate(new Vector2d(x, y+step)) - evaluate(new Vector2d(x, y-step));

        p = new Vector2d(x1, y1);
        return p;
    }
}
