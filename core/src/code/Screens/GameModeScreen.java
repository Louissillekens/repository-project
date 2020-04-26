package code.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.game.game.Game;

public class GameModeScreen extends ApplicationAdapter implements Screen {

    private final com.game.game.Game myGame;
    private Stage stage;
    private Image background;
    private Texture bgTexture;
    private Image gameModeImage;
    private Texture gameModeTexture;

    private Skin skin;

    private TextButton singlePlayer;
    private TextButton multiplayer;
    private TextButton bot;
    private TextButton botVSplayer;
    private TextButton botVSbot;

    public GameModeScreen(final Game myGame) {

        this.myGame = myGame;
        this.stage = new Stage(new StretchViewport(Game.WIDTH, Game.HEIGHT, myGame.camera));
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
        gameModeImage.setPosition(80, 400);
        gameModeImage.setSize(500, 100);
        stage.addActor(gameModeImage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        singlePlayer = new TextButton("Single Player", skin);
        singlePlayer.setPosition(100, 300);
        singlePlayer.setSize(200, 60);
        stage.addActor(singlePlayer);

        multiplayer = new TextButton("Player VS Player", skin);
        multiplayer.setPosition(450, 300);
        multiplayer.setSize(200, 60);
        stage.addActor(multiplayer);

        bot = new TextButton("Bot", skin);
        bot.setPosition(100, 200);
        bot.setSize(200, 60);
        stage.addActor(bot);

        botVSplayer = new TextButton("Bot VS Player", skin);
        botVSplayer.setPosition(450, 200);
        botVSplayer.setSize(200, 60);
        stage.addActor(botVSplayer);

        botVSbot = new TextButton("Bot VS Bot", skin);
        botVSbot.setPosition(100, 100);
        botVSbot.setSize(200, 60);
        stage.addActor(botVSbot);

        class SinglePlayerListener extends ChangeListener {

            private Game game;
            private Screen screen;

            public SinglePlayerListener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //this.game.setScreen(new SinglePlayerScreen(this.game));
                //this.screen.dispose();

               // new LwjglApplication(new PuttingGame());
            }
        }
        singlePlayer.addListener(new SinglePlayerListener(myGame, this));

        class MultiplayerListener extends ChangeListener {

            private Game game;
            private Screen screen;

            public MultiplayerListener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new MultiplayerScreen(this.game));
                this.screen.dispose();
            }
        }
        multiplayer.addListener(new MultiplayerListener(myGame, this));

        class BotListener extends ChangeListener {

            private Game game;
            private Screen screen;

            public BotListener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new BotScreen(this.game));
                this.screen.dispose();
            }
        }
        bot.addListener(new BotListener(myGame, this));

        class BotVSplayerListener extends ChangeListener {

            private Game game;
            private Screen screen;

            public BotVSplayerListener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new BotVSplayerScreen(this.game));
                this.screen.dispose();
            }
        }
        botVSplayer.addListener(new BotVSplayerListener(myGame, this));

        class BotVSbotListener extends ChangeListener {

            private Game game;
            private Screen screen;

            public BotVSbotListener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new BotVSbotScreen(this.game));
                this.screen.dispose();
            }
        }
        botVSbot.addListener(new BotVSbotListener(myGame, this));
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        super.render();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            //myGame.setScreen(new GameScreen(this.myGame));
            dispose();
        }
    }

    public void update(float delta) {

        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, false);
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
