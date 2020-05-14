package code.Controller;

import code.Physics.Rungekuttasolver;
import code.Screens.GameModeScreen;
import code.Screens.PuttingGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InputHandler {



    /**
     * checks for input and updates the given PuttingGameScreen accordingly
     * @param gamescreen the game we want the input to affect
     */
    public static void checkForInput(PuttingGameScreen gamescreen){

        // Some key pressed input to rotate the camera and also zoom in zoom out
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            gamescreen.getCamera().rotateAround(PuttingGameScreen.vector1 = new Vector3(gamescreen.getBallPositionX(), PuttingGameScreen.defineFunction(gamescreen.getBallPositionX(), gamescreen.getBallPositionZ()), gamescreen.getBallPositionZ()),
                    PuttingGameScreen.vector2 = new Vector3(0f, -1f, 0f), 1f);
            gamescreen.getCamera().lookAt(gamescreen.getBallPositionX(), PuttingGameScreen.defineFunction(gamescreen.getBallPositionX(), gamescreen.getBallPositionZ()), gamescreen.getBallPositionZ());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            gamescreen.getCamera().rotateAround(PuttingGameScreen.vector1 = new Vector3(gamescreen.getBallPositionX(), PuttingGameScreen.defineFunction(gamescreen.getBallPositionX(), gamescreen.getBallPositionZ()), gamescreen.getBallPositionZ()),
                    PuttingGameScreen.vector2 = new Vector3(0f, 1f, 0f), 1f);
            gamescreen.getCamera().lookAt(gamescreen.getBallPositionX(), PuttingGameScreen.defineFunction(gamescreen.getBallPositionX(), gamescreen.getBallPositionZ()), gamescreen.getBallPositionZ());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //round the shot power to two decimal places to avoid errors where the power would get above max power
            double exact_shot_power = round(gamescreen.getShot_Power(), 2);
            gamescreen.setShot_Power(exact_shot_power);
            if(gamescreen.getShot_Power() < gamescreen.getMAX_SPEED() - gamescreen.getPOWER_INCREMENT()){
                gamescreen.IncrementShotPower(1);
            }
            //System.out.println("shot power now at: " + gamescreen.getShot_Power());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            //round the shot power to two decimal places to avoid errors where the power would get below 0
            double exact_shot_power = round(gamescreen.getShot_Power(), 2);
            gamescreen.setShot_Power(exact_shot_power);
            if(gamescreen.getShot_Power() > 0 + gamescreen.getPOWER_INCREMENT()){
                gamescreen.IncrementShotPower(-1);
            }
            //System.out.println("shot power now at: " + gamescreen.getShot_Power());
        }
        // Key pressed input to be back on the game mode screen
        if(Gdx.input.isKeyPressed(Input.Keys.B)) {
            gamescreen.getGame().setScreen(new GameModeScreen(gamescreen.getGame()));
        }
        // Key pressed input to quit
        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            Gdx.app.exit();
        }

        // Key press input R that return to the place of the previous shot
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {

            // Condition that checks if the ball shot can be reset to the previous one
            if (gamescreen.getCanReset()) {
                // Condition that checks if the camera can be moved after pushing keyboard command R
                if (gamescreen.getTrackShot()) {
                    gamescreen.getCamera().translate(-(gamescreen.getTranslateX()[gamescreen.getCountIndex() - 1]), -0.001f, -(gamescreen.getTranslateZ()[gamescreen.getCountIndex() - 1]));
                }
                gamescreen.setTrackShot(false);
                gamescreen.setCanTranslateCam(false);
                // Call of the method that reset the ball to the previous place
                gamescreen.resetBallShot();
                gamescreen.getCamera().lookAt(gamescreen.getBallPositionX(), gamescreen.defineFunction(gamescreen.getBallPositionX(), gamescreen.getBallPositionZ()), gamescreen.getBallPositionZ());
            }
        }

        //System.out.println("x : " + gamescreen.getCamera().direction.x);
        //System.out.println("y : " + gamescreen.getCamera().direction.y);
        //System.out.println("z : " + gamescreen.getCamera().direction.z);
    }

    /**
     * this simple method can round a number to a number of decimal places
     * @param value the value we wish to round
     * @param places the places after the comma to round to
     * @return the rounded value
     */
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
