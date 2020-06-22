package code.Screens;

import code.Bot.PuttingBotDeployement;
import code.Physics.Rungekuttasolver;
import code.Physics.VerletSolver;
import code.util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputHandler {

    private PuttingGameScreen game;

    public InputHandler(PuttingGameScreen game) {

        this.game = game;
    }

    public void checkForSpaceInput(){

        //key input to take a shot with the given power
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){

            double power = game.getShot_Power();

            PuttingGameScreen.ballStop = false;

            // Condition that checks the game mode chosen by the user before taking the shot
            if (game.getGameMode().gameName.equals("Single_Player")) {

                // Instance of the RK4 solver
                // Used to compute the next position of the ball based on the current position, the camera direction and the power
                if (SolverScreen.getSolverName().equals("RK4")) {

                    if (Math.abs(game.getCamera().direction.x) > 0.001 && Math.abs(game.getCamera().direction.z) > 0.001) {

                        PuttingGameScreen.countIndex++;

                        game.takeRK4shot((float) power, 500);
                        PuttingGameScreen.countTries++;
                    }
                }

                // Instance of the Verlet solver
                // Used to compute the next position of the ball based on the current position, the camera direction and the power
                if (SolverScreen.getSolverName().equals("Verlet")) {

                    int scalar = 500;
                    float directionX = game.getCamera().direction.x;
                    float directionZ = game.getCamera().direction.z;

                    VerletSolver Verlet = new VerletSolver();

                    Verlet.setValues(game.getBallPositionX(), game.getBallPositionZ(), (directionX * power) * scalar, (directionZ * power) * scalar);

                    Verlet.Verlet();
                    PuttingGameScreen.newBallPositionX = (float) Verlet.getX();
                    PuttingGameScreen.newBallPositionZ = (float) Verlet.getY();

                    PuttingGameScreen.countTries++;

                    PuttingGameScreen.countIndex++;
                }
            }
        }
    }

    /**
     * checks for input and updates the given PuttingGameScreen accordingly
     */
    public void checkForInput(){

        if (game.getGameMode().gameName.equals("Single_Player")) {

            // Some key pressed input to rotate the camera and also zoom in zoom out
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                game.cameraRotation(-1);
                System.out.println("dir x = " + game.getCamera().direction.x);
                System.out.println("dir z = " + game.getCamera().direction.z);
                System.out.println();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                game.cameraRotation(1);

                System.out.println("dir x = " + game.getCamera().direction.x);
                System.out.println("dir z = " + game.getCamera().direction.z);
                System.out.println();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
                //round the shot power to two decimal places to avoid errors where the power would get above max power
                PuttingGameScreen.shot_Power = Util.round(PuttingGameScreen.shot_Power, 2);
                if(PuttingGameScreen.shot_Power < PuttingGameScreen.getMAX_SPEED() - game.getPOWER_INCREMENT()){
                    game.IncrementShotPower(1);
                }
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                //round the shot power to two decimal places to avoid errors where the power would get below 0
                PuttingGameScreen.shot_Power = Util.round(PuttingGameScreen.shot_Power, 2);
                if(PuttingGameScreen.shot_Power > 0 + game.getPOWER_INCREMENT()){
                    game.IncrementShotPower(-1);
                }
            }
            // Key pressed input to quit
            if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
                Gdx.app.exit();
            }
            // Key press input R that return to the place of the previous shot
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {

                // Condition that checks if the ball shot can be reset to the previous one
                if (PuttingGameScreen.canReset) {
                    // Condition that checks if the camera can be moved after pushing keyboard command R
                    if (PuttingGameScreen.trackShot) {
                        game.getCamera().translate(-(PuttingGameScreen.translateX[game.getCountIndex() - 1]), -0.001f, -(PuttingGameScreen.translateZ[game.getCountIndex() - 1]));
                    }
                    PuttingGameScreen.trackShot = false;
                    PuttingGameScreen.canTranslateCam = false;
                    // Call of the method that reset the ball to the previous place
                    game.resetBallShot();
                    game.getCamera().lookAt(game.getBallPositionX(), PuttingGameScreen.defineFunction(game.getBallPositionX(), game.getBallPositionZ()), game.getBallPositionZ());
                }
            }
        }
    }
}
