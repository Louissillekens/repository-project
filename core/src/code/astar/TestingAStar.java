package code.astar;

import code.Screens.GameMode;
import code.Screens.PuttingGameScreen;
import com.game.game.Game;

public class TestingAStar {

    public static void main(String[] args) {

        PuttingGameScreen game = new PuttingGameScreen(new Game(), new GameMode("AStar"));
        System.out.println("Game made with Ball at (" + game.getBallPositionX() + ", " + game.getBallPositionZ() + ")");
        System.out.println("has goal at (" + game.getFlagPositionX() + ", " + game.getFlagPositionZ() + ")");
        AStar a = new AStar(game);
    }
}
