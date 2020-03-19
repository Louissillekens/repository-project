package code.Bot;

import code.Board.*;

/**
 * interface for any and all bots to be made in the future
 */
public interface PuttingBot {

    /**
     * method that chooses the velocity of the ball based on given course and ball_position
     * @param course the course on which the bot operates
     * @param ball_position the position of the ball on which the bot operates
     * @return the velocity in the form of a Vector2d object
     */
    Vector2d shot_velocity(PuttingCourse course, Vector2d ball_position);
}
