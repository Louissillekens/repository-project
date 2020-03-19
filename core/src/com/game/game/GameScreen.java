package com.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen implements Screen {

    private final Game myGame;
    private Stage stage;
    private Image background;
    private Image ball_img;
    private Image hole_img;
    private Image hole_cirlce_img;
    private ImageButton take_shot_1;
    private ImageButton take_shot_2;

    public GameScreen(final Game myGame) {

        this.myGame = myGame;
        this.stage = new Stage(new StretchViewport(Game.WIDTH, Game.HEIGHT, myGame.camera));
        Gdx.input.setInputProcessor(stage);

        Texture bg_texture = new Texture(Gdx.files.internal("ground.jpg"));
        bg_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Image(bg_texture);
        background.setPosition(0, 0);
        background.setSize(Game.WIDTH, Game.HEIGHT);
        stage.addActor(background);


        Texture hole_texture = new Texture(Gdx.files.internal("hole.jpg"));
        hole_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hole_img = new Image(hole_texture);
        // add the position from the hole vector
        hole_img.setPosition(800, 500);
        hole_img.setSize(150, 150);
        stage.addActor(hole_img);

        Texture hole_circle_texture = new Texture(Gdx.files.internal("hole_tolerance.jpg"));
        hole_circle_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hole_cirlce_img = new Image(hole_circle_texture);
        // add the same position as the hole
        hole_cirlce_img.setPosition(800, 500);
        // add the tolerance size from the putting course constructor
        hole_cirlce_img.setSize(150, 150);
        stage.addActor(hole_cirlce_img);

        Texture button_1_texture = new Texture(Gdx.files.internal("take_shot_1.jpg"));
        TextureRegion button_1_region = new TextureRegion(button_1_texture);
        TextureRegionDrawable button_1_drawable = new TextureRegionDrawable(button_1_region);
        take_shot_1 = new ImageButton(button_1_drawable);
        take_shot_1.setPosition(50, 600);
        take_shot_1.setSize(250, 100);
        stage.addActor(take_shot_1);

        Texture button_2_texture = new Texture(Gdx.files.internal("take_shot_2.jpg"));
        TextureRegion button_2_region = new TextureRegion(button_2_texture);
        TextureRegionDrawable button_2_drawable = new TextureRegionDrawable(button_2_region);
        take_shot_2 = new ImageButton(button_2_drawable);
        take_shot_2.setPosition(50, 520);
        take_shot_2.setSize(250, 100);
        stage.addActor(take_shot_2);
    }
    public void draw_ball(double x , double y){
        float xx = (float)x;
        float yy = (float)y;
        Texture ball_texture = new Texture(Gdx.files.internal("Ball.jpg"));
        ball_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ball_img = new Image(ball_texture);
        // add the position from the ball vector constructor
        ball_img.setPosition(xx, yy);
        ball_img.setSize(30, 30);
        stage.addActor(ball_img);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        myGame.batch.begin();
        //myGame.bitmapFont.draw(myGame.batch, "hello", 120, 120);
        myGame.batch.end();
    }

    public void update(float delta) {

        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void hide() {

    }
}

