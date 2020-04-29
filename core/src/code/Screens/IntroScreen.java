package code.Screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.game.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class IntroScreen implements Screen {

    private final com.game.game.Game myGame;

    // Instances for the background and title images + textures
    // You can find the path to them in the assets folder
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

        bgTexture = new Texture(Gdx.files.internal("IntroBackground.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Image(bgTexture);

        titleTexture = new Texture(Gdx.files.internal("Title.png"));
        titleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        title = new Image(titleTexture);

        subTitleTexture = new Texture(Gdx.files.internal("SubTitle.png"));
        subTitleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        subTitle = new Image(subTitleTexture);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.camera.update();

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
