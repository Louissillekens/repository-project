package code.Board;

public class PuttingCourse{

    private Function2d height;
    private Function2d friction;
    private double maximum_velocity;
    private double hole_tolerance;
    private Vector2d start;
    private Vector2d flag;
    private Ball ball;


    public PuttingCourse(double[][] height_map,  double[][] friction_map, Vector2d start, Vector2d flag, double maximum_velocity,
                         double hole_tolerance, double out_of_bounds_height, double out_of_bounds_friction, Ball ball) {

        height = new Height_function(height_map, out_of_bounds_height);
        friction = new Friction_function(friction_map, out_of_bounds_friction);
        this.flag = flag;
        this.start = start;
        this.maximum_velocity = maximum_velocity;
        this.hole_tolerance = hole_tolerance;
        this.ball = ball;
    }

    //constructor where hole_tolerance, max_speed, ball, and out_of_bounds_values are already defined
    //only a course has to be given
    //counts more or less as "default constructor"
    public PuttingCourse(double[][] height_map, double[][] friction_map, Vector2d start, Vector2d flag) {

        double out_of_bounds_height = -1;//water around map
        double out_of_bounds_friction = 0.131; //the friction like in the example given in course manual
        height = new Height_function(height_map, out_of_bounds_height);
        friction = new Friction_function(friction_map, out_of_bounds_friction);
        this.flag = flag;
        this.start = start;

        //standard values like in the project manual
        ball = new Ball();
        maximum_velocity = 3;
        hole_tolerance = 0.2;
    }


    // Return an object of type Function2d
    // Access to the height of a location on the field by doing get_height.evaluate_height(Vector2d p);
    public Function2d get_height() {

        return height;
    }

    public Function2d get_friction(){
        return friction;
    }

    // Return the flag given in the constructor
    // Access to the position with the Vector2d methods
    public Vector2d get_flag_position() {

        return flag;
    }

    // Return the given start vector in the constructor
    // Access to the position with the Vector2d methods
    public Vector2d get_start_position() {

        return start;
    }


    // Return the maximum velocity of the start vector
    public double get_maximum_velocity() {

        return maximum_velocity;
    }

    // Return the winning tolerance of the hole
    public double get_hole_tolerance() {

        return hole_tolerance;
    }


    public Ball getBall(){
        return ball;
    }
}