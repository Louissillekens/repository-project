package code.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.game.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class IntroScreen extends ApplicationAdapter implements Screen {

    private final com.game.game.Game myGame;
    //private Stage stage;
    private Image background;
    private Texture bgTexture;
    private Image title;
    private Texture titleTexture;
    private Image subTitle;
    private Texture subTitleTexture;
    private OrthographicCamera camera;

    public IntroScreen(final Game myGame) {

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Game.WIDTH, Game.HEIGHT);
        this.myGame = myGame;
        //this.stage = new Stage();
        //Gdx.input.setInputProcessor(stage);

        bgTexture = new Texture(Gdx.files.internal("IntroBackground.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Image(bgTexture);
        //background.setPosition(0, 0);
        //background.setSize(com.game.game.Game.WIDTH, com.game.game.Game.HEIGHT);
        //stage.addActor(background);

        titleTexture = new Texture(Gdx.files.internal("Title.png"));
        titleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        title = new Image(titleTexture);
        //title.setPosition(100, 330);
        //title.setSize(600, 150);
        //stage.addActor(title);

        subTitleTexture = new Texture(Gdx.files.internal("SubTitle.png"));
        subTitleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        subTitle = new Image(subTitleTexture);
        //subTitle.setPosition(100, 200);
        //subTitle.setSize(500, 100);
        //stage.addActor(subTitle);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update(delta);

        this.camera.update();

        // stage.draw();

        this.myGame.batch.begin();

            this.myGame.batch.draw(bgTexture, 0,0, Game.WIDTH, Game.HEIGHT);
            this.myGame.batch.draw(titleTexture, 100,330, 600, 150);
            this.myGame.batch.draw(subTitleTexture, 100,200, 500, 100);

        this.myGame.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            myGame.setScreen(new GameModeScreen(this.myGame));
            dispose();
        }
    }

    public void update(float delta) {

        // stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

        // stage.getViewport().update(width, height, false);
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
