package code.Bot;

import code.Board.*;

public interface PuttingBot {

    Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position);
}
