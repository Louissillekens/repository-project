package code.Board;

import code.Physics.EulerSolver;

/**
 * class Ball describes a ball in the golf simulator
 * defined by a location, mass, and color
 */
public class Ball extends EulerSolver{

    private int color; //int that can be converted to a color in the graphics engine

    /**
     * parametric constructor for a ball
     */
    public Ball(){
        color = 1; //standard color 1 ---> white
    }


    /**
     * getter for the color of the ball
     * @return int later to be mapped to a color
     */
    public int getColor(){
        return color;
    }

    /**
     * change the color of the ball
     * @param newColor the color of the ball after method gets executed
     */
    public void setColor(int newColor){
        color = newColor;
    }

}

