package code.Controller;

import code.Screens.GameModeScreen;
import code.Screens.PuttingGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

public class InputHandler {

    /**
     * checks for input and updates the given PuttingGameScreen accordingly
     * @param gamescreen the game we want the input to affect
     */
    public static void checkForInput(PuttingGameScreen gamescreen){

        // Some key pressed input to rotate the camera and also zoom in zoom out
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            gamescreen.getCamera().lookAt(0,0,0);
            gamescreen.getCamera().rotateAround(gamescreen.vector1 = new Vector3(0f, 0f, 0f), gamescreen.vector2 = new Vector3(0f, 1f, 0f), -1f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            gamescreen.getCamera().lookAt(0,0,0);
            gamescreen.getCamera().rotateAround(gamescreen.vector1 = new Vector3(0f, 0f, 0f), gamescreen.vector2 = new Vector3(0f, 1f, 0f), 1f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            gamescreen.getCamera().lookAt(0,0,0);
            if ((gamescreen.getCamera().position.y > 2)) {
                gamescreen.getCamera().translate(0, -0.1f, 0);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            gamescreen.getCamera().lookAt(0,0,0);
            if (gamescreen.getCamera().position.y < 15) {
                gamescreen.getCamera().translate(0, 0.1f, 0);
            }
        }
        // Key pressed input to be back on the game mode screen
        if(Gdx.input.isKeyPressed(Input.Keys.B)) {
            gamescreen.getGame().setScreen(new GameModeScreen(gamescreen.getGame()));
        }
        // Key pressed input to quit
        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            Gdx.app.exit();
        }

        //key input for deciding the amount of power to strike the ball with
        if(Gdx.input.isKeyPressed(Input.Keys.W)){

            if(gamescreen.getShot_Power() < gamescreen.getMAX_SPEED() - gamescreen.getPOWER_INCREMENT()){
                gamescreen.IncrementShotPower(1);
            }
            System.out.println("shot power now at: " + gamescreen.getShot_Power());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            if(gamescreen.getShot_Power() > 0 + gamescreen.getPOWER_INCREMENT()){
                //TODO fix bug that could happen when the ball moves slower than the increment (ball speed could become negative)
                //TODO should not be able to arise when our starting speed keeps to the rules but just to be sure
                gamescreen.IncrementShotPower(-1);
            }
            System.out.println("shot power now at: " + gamescreen.getShot_Power());
        }
        //key input to take shot
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            double x_direction = gamescreen.getCamera().direction.x;
            double y_direction = gamescreen.getCamera().direction.y;
            double power = gamescreen.getShot_Power();
            //TODO make code to take shot based on ball class
            /*gamescreen.ball.hit(stuff here that uses power and angle of camera)*/
            System.out.println("shot taken with power: " + power + " and x_direction: " + x_direction + " and y_direction: " + y_direction);
        }


    }
}
