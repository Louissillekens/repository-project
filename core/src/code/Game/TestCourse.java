package code.Game;

import code.Board.*;

public class TestCourse {



    public static void main(String[]args) {

        double[][] height_map = {{1   , 1.2 , 1.3 , 1.3 , 1.1 , 1   , 0   ,-1  },
                {1.05, 1.15, 1.2 , 1.2 , 1.1 , 0.9 , 0.55, 0  },
                {1.1 , 1.2 , 1.2 , 1.25, 1.34, 1   , 0.33, 0.1},
                {1.43, 1.23, 1.18, 0.9 , 0.4 , 0   ,-0.4 ,-1.2}};


        double[][] friction_map = new double[10][10];
        for(int i = 0 ; i < friction_map.length ; i++){
            for(int j = 0 ; j < friction_map[0].length ; j++){
                friction_map[i][j] = 0.131; //basic value for the friction
            }
        }

        double out_of_bounds_height = 1; //outside of the array the height is just 1
        double out_of_bounds_friction = 0.131;//outside the array the friction is just 0.131

        Vector2d start = new Vector2d(1.5,2);
        Vector2d flag = new Vector2d(2,5);

        Ball ball = new Ball(start, 5);

        final double gravity = 9.81;
        final double max_velocity = 3;
        final double hole_tolerance = 0.02;

        //fix this to the new constructor
        PuttingCourse test = new PuttingCourse(height_map, friction_map, start, flag, max_velocity, hole_tolerance, out_of_bounds_height, out_of_bounds_friction, ball);

        System.out.println("height at start: " + test.get_height().evaluate(start));
        System.out.println("friction at start: " + test.get_friction().evaluate(start));
        Vector2d p = test.get_height().gradient(start);
        System.out.println("the gradient at the start is a vector with x at: " + p.get_x() + " and y at: " + p.get_y());
    }
}