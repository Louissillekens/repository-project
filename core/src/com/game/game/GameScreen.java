package com.game.game;

import code.Board.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private Image water_img;
    private ImageButton take_shot_1;
    private ImageButton take_shot_2;
    private Function2d height;
    private PuttingCourse test;
    private Vector2d start = new Vector2d(1.5,2);
    private Vector2d flag = new Vector2d(2,5);
    private Ball ball = new Ball();
    private double out_of_bounds_height = 1; //outside of the array the height is just 1
    private double out_of_bounds_friction = 0.131;//outside the array the friction is just 0.131
    final double max_velocity = 3;
    final double hole_tolerance = 0.02;
    private double[][] height_map = {{1   , 1.2 , 1.3 , 1.3 , 1.1 , 1   , 0   , 1  },
                                     {1.05, 1.15, 1.2 , 1.2 , 1.1 , 0.9 , 0.55, 0  },
                                     {1.1 , 1.2 , -1.2 , -1.25, 1.34, 1   , 0.33, 0.1},
                                     {1.43, 1.23, -1.18, 0.9 , 0.4 , 0   ,0.4 ,1.2}};

    double[][] friction_map = new double[10][10];


    /**
     * parametric constructor
     * @param Game object
     */
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
        hole_img.setPosition(800, 500);
        hole_img.setSize(150, 150);
        stage.addActor(hole_img);

        Texture hole_circle_texture = new Texture(Gdx.files.internal("hole_tolerance.jpg"));
        hole_circle_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        hole_cirlce_img = new Image(hole_circle_texture);
        hole_cirlce_img.setPosition(800, 500);
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

        draw_water(height_map);
        draw_ball(50, 50);
    }

    /**
     * void method that draw a ball on the field
     * @param x and y values used for the location of the ball
     */
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

    /**
     * void method that draw water fields according to the height of this one
     * @param 2d array that contains the height at each location
     */
    public void draw_water(double[][] height_map) {

        Vector2d p;
        height = new Height_function(height_map, 1);
        double step = 0.25;
        double i = 0;
        while (i < height_map.length) {
            double j = 0;
            while (j < height_map[0].length) {
                p = new Vector2d(i, j);
                if (height.evaluate(p) < 0) {
                    Texture water_texture = new Texture(Gdx.files.internal("water_field.jpg"));
                    water_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                    water_img = new Image(water_texture);
                    water_img.setPosition((float) i*72, (float) j*108);
                    water_img.setSize(80, 80);
                    stage.addActor(water_img);
                }
                j+=step;
            }
            i+=step;
        }
    }


    /**
     * libGDX method
     * @param float delta
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        for(int i = 0 ; i < friction_map.length ; i++){
            for(int j = 0 ; j < friction_map[0].length ; j++){
                friction_map[i][j] = 0.131; //basic value for the friction
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            test = new PuttingCourse(height_map, friction_map, start, flag, max_velocity, hole_tolerance, out_of_bounds_height, out_of_bounds_friction, ball);
        }
    }

    /**
     * libGDX method
     * @param float delta
     */
    public void update(float delta) {

        stage.act(delta);
    }

    /**
     * libGDX method
     * @param int width and int height of the window
     */
    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, false);
    }

    /**
     * libGDX method
     */
    @Override
    public void show() {

    }

    /**
     * libGDX method
     */
    @Override
    public void pause() {

    }

    /**
     * libGDX method
     */
    @Override
    public void resume() {

    }

    /**
     * libGDX method
     */
    @Override
    public void dispose() {

    }

    /**
     * libGDX method
     */
    @Override
    public void hide() {

    }
}


