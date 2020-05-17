package code.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.game.Game;

/**
 * class GameModeScreen this class implements the screen on which to play our game
 */
public class GameModeScreen implements Screen {

    private final com.game.game.Game myGame;
    private Stage stage;

    // Instances for the background and title images + textures
    private Image background;
    private Texture bgTexture;
    private Image gameModeImage;
    private Texture gameModeTexture;

    // Package with the design used for the buttons
    private Skin skin;

    // Instances for the different game modes buttons
    private TextButton singlePlayer;
    private TextButton multiplayer;
    private TextButton bot;
    private TextButton botVSplayer;
    private TextButton botVSbot;

    // Instances for the text creation
    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont font = new BitmapFont();
    private GlyphLayout layout = new GlyphLayout();

    /**
     * a parametric constructor that takes a Game and makes all visuals for the course
     * @param game the game for which to make the GUI
     */
    public GameModeScreen(final Game game) {

        this.myGame = game;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        bgTexture = new Texture(Gdx.files.internal("IntroBackground.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Image(bgTexture);
        background.setPosition(0, 0);
        background.setSize(com.game.game.Game.WIDTH, com.game.game.Game.HEIGHT);
        stage.addActor(background);

        gameModeTexture = new Texture(Gdx.files.internal("GameMode.png"));
        gameModeTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gameModeImage = new Image(gameModeTexture);
        gameModeImage.setPosition(80, 405);
        gameModeImage.setSize(550, 95);
        stage.addActor(gameModeImage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        singlePlayer = new TextButton("Single Player", skin);
        singlePlayer.setPosition(100, 300);
        singlePlayer.setSize(200, 60);
        stage.addActor(singlePlayer);

        multiplayer = new TextButton("Player VS Player", skin);
        multiplayer.setPosition(100, 200);
        multiplayer.setSize(200, 60);
        //stage.addActor(multiplayer);

        bot = new TextButton("Bot", skin);
        bot.setPosition(400, 300);
        bot.setSize(200, 60);
        stage.addActor(bot);

        botVSplayer = new TextButton("Bot VS Player", skin);
        botVSplayer.setPosition(450, 200);
        botVSplayer.setSize(200, 60);
        //stage.addActor(botVSplayer);

        botVSbot = new TextButton("Bot VS Bot", skin);
        botVSbot.setPosition(100, 100);
        botVSbot.setSize(200, 60);
        //stage.addActor(botVSbot);

        // Every inner class are called when a button is pressed
        // It changes the actual screen to the game screen with selected game mode
        class Single_Player_Listener extends ChangeListener {

            private Game game;
            private Screen screen;
            private String gameName = "Single_Player";

            private Single_Player_Listener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new SolverScreen(myGame, new GameMode(gameName)));
                this.screen.dispose();
            }
        }
        singlePlayer.addListener(new Single_Player_Listener(game, this));

        class Multi_Player_Listener extends ChangeListener {

            private Game game;
            private Screen screen;
            private String gameName = "Multi_Player";

            private Multi_Player_Listener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new PuttingGameScreen(myGame, new GameMode(gameName)));
                this.screen.dispose();
            }
        }
        multiplayer.addListener(new Multi_Player_Listener(game, this));

        class Bot_Listener extends ChangeListener {

            private Game game;
            private Screen screen;
            private String gameName = "Bot";

            private Bot_Listener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new PuttingGameScreen(myGame, new GameMode(gameName)));
                this.screen.dispose();
            }
        }
        bot.addListener(new Bot_Listener(game, this));

        class Bot_VS_player_Listener extends ChangeListener {

            private Game game;
            private Screen screen;
            private String gameName = "Bot_VS_Player";

            private Bot_VS_player_Listener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new PuttingGameScreen(myGame, new GameMode(gameName)));
                this.screen.dispose();
            }
        }
        botVSplayer.addListener(new Bot_VS_player_Listener(game, this));

        class Bot_VS_Bot_Listener extends ChangeListener {

            private Game game;
            private Screen screen;
            private String gameName = "Bot_VS_Bot";

            private Bot_VS_Bot_Listener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new PuttingGameScreen(myGame, new GameMode(gameName)));
                this.screen.dispose();
            }
        }
        botVSbot.addListener(new Bot_VS_Bot_Listener(game, this));
    }

    /**
     * Method used to draw a string on the screen
     * @param message to draw on the screen
     */
    public void displayMessage(String message, float x, float y){

        font.getData().setScale(1.75f, 1.75f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setColor(Color.RED);
        layout.setText(font, message);

        batch.begin();
        font.draw(batch, message, x, y);
        batch.end();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

        displayMessage("Game controls:", 100, 260);
        displayMessage("Left and Right arrow - rotate the camera and change the angle of the shot", 100, 210);
        displayMessage("Top and Bottom arrow - increase or decrease the power of the shot", 100, 170);
        displayMessage("Space - take a shot with the chosen power and angle ", 100, 130);
        displayMessage("R - reset the ball location to its previous position", 100, 90);
        displayMessage("B - return to the game mode menu selector", 100, 50);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {
    }
}
