package Bot;

import Board.PuttingCourse;
import Board.Vector2d;

public interface PuttingBot {

    Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position);
}
