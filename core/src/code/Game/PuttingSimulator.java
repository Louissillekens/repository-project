package code.Game;

import code.Board.*;
import code.Physics.EulerSolver;
import code.Physics.PhysicsEngine;

public class PuttingSimulator extends EulerSolver {

    private PuttingCourse course;
    private PhysicsEngine engine;
    private Vector2d acceleration;
    Vector2d position = new Vector2d(0, 0);
    Vector2d velocity = new Vector2d(0, 0);

    public PuttingSimulator(PuttingCourse course, PhysicsEngine engine) {

        this.course = course;
        this.engine = engine;
    }

    public void set_ball_position(Vector2d p) {

        Vector2d np = new Vector2d(p.get_x(), p.get_y());
        course.getBall().setLocation(p);
    }

    public Vector2d get_ball_position() {

        return course.getBall().getLocation();
    }

    public void take_shot(Vector2d initial_ball_acceleration){
        acceleration = initial_ball_acceleration;
    }
}
