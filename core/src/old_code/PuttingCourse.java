package old_code;

import code.Board.Ball;
import code.Board.Vector2d;

/**
 * this class describes a course with all variables that belong to it such as height, friction, location of hole, etc...
 */
public class PuttingCourse{

    private Function2d height;
    private Function2d friction;
    private double maximum_velocity;
    private double hole_tolerance;
    private Vector2d start;
    private Vector2d flag;
    private Ball ball;

    /**
     * parametric constructor for a course
     * @param height_map an array holding the height of the course at certain points
     * @param friction_map an array holding the friction of the course at certain points
     * @param start the vector at which the ball spawns
     * @param flag the location of the hole
     * @param maximum_velocity maximum speed the ball can reach in this simulation
     * @param hole_tolerance gives a specification as to how close the ball should be to the hole to count as a win
     * @param out_of_bounds_height the height for all points outside the array
     * @param out_of_bounds_friction the friction for all points outside the array
     * @param ball the ball we use on this map
     */
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

    /**
     * constructor where hole_tolerance, max_speed, ball, and out_of_bounds_values are already defined
     * only a course has to be given
     * counts more or less as "default constructor"
     * @param height_map the height at certain points put in an array
     * @param friction_map the friction at certain points put in an array
     * @param start the location where the ball spawns
     * @param flag the location of the hole
     */
    public PuttingCourse(double[][] height_map, double[][] friction_map, Vector2d start, Vector2d flag) {

        double out_of_bounds_height = -1;//water around map
        double out_of_bounds_friction = 0.131; //the friction like in the example given in course manual
        height = new Height_function(height_map, out_of_bounds_height);
        friction = new Friction_function(friction_map, out_of_bounds_friction);
        this.flag = flag;
        this.start = start;

        //standard values like in the project manual
        maximum_velocity = 3;
        hole_tolerance = 0.2;
    }


    /**
     * getter for the height_map
     * @return the array holding all heights
     */
    public Function2d get_height() {

        return height;
    }

    /**
     * getter for the friction_map
     * @return the array holding all friction values
     */
    public Function2d get_friction(){
        return friction;
    }

    /**
     * getter for the position of the flag/hole
     * @return a Vector2d with the location of the flag
     */
    public Vector2d get_flag_position() {

        return flag;
    }

    /**
     * getter for the start position
     * @return a Vector2d with the location of the start
     */
    public Vector2d get_start_position() {

        return start;
    }


    /**
     * Return the maximum velocity to be reached on this course
     * @return a Vector2d holding the max_speed possible
     */
    public double get_maximum_velocity() {

        return maximum_velocity;
    }

    /**
     * getter for the winning tolerance of the hole
     * @return a double value (in meters) holding the max distance of the hole that is considered a "win"
     */
    public double get_hole_tolerance() {

        return hole_tolerance;
    }

    /**
     * getter for the ball
     * @return an object of type Ball
     */
    public Ball getBall(){
        return ball;
    }
}