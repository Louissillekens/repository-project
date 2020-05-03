package code.Board;

import code.Physics.EulerSolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/**
 * class Ball describes a ball in the golf simulator
 * defined by a location, mass, and color
 */
public class Ball {

    private ModelBuilder modelBuilder;

    private Model ball;
    private ModelInstance ballInstance;
    private float ballSize;
    private float xCurrent;
    private float zCurrent;

    /**
     * parametric constructor for a ball
     */
    public Ball(Model ball, ModelInstance ballInstance, float ballSize, float xCurrent, float zCurrent) {

        this.ball = ball;
        this.ballInstance = ballInstance;
        this.ballSize = ballSize;
        this.xCurrent = xCurrent;
        this.zCurrent = zCurrent;
    }

    // isHit() method to only check the first shoot (at location (0,y,0))
    // Need to update that in the future (when the ball can move)
    public boolean isHit() {

        if (xCurrent != 0f && zCurrent != 0f) {
            return true;
        }
        else {
            return false;
        }
    }



}

