package old_code.OldImplementationGDX;

import code.Board.*;
import code.Screens.GameModeScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.game.Game;

public class MultiplayerScreen implements Screen {

    private final Game game;
    private Stage stage;
    private Image background;
    private Image ball_img;
    private Image hole_img;
    private Image hole_cirlce_img;
    private Image water_img;
    private Skin skin;
    private TextButton take_shot_1;
    private TextButton take_shot_2;
    private TextButton backButton;
    private Function2d height;
    private Vector2d start = new Vector2d(1.5,2);
    private Vector2d flag = new Vector2d(2,5);

    /**
     * parametric constructor
     * @param myGame object
     */
    public MultiplayerScreen(final Game myGame) {

        this.game = myGame;
        this.stage = new Stage();
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

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        take_shot_1 = new TextButton("Take shot mode 1", skin);
        take_shot_1.setPosition(40, 600);
        take_shot_1.setSize(150, 40);
        stage.addActor(take_shot_1);

        take_shot_2 = new TextButton("Take shot mode 2", skin);
        take_shot_2.setPosition(40, 550);
        take_shot_2.setSize(150, 40);
        stage.addActor(take_shot_2);

        backButton = new TextButton("Back", skin);
        backButton.setPosition(40, 650);
        backButton.setSize(60, 40);
        stage.addActor(backButton);

        class BackButtonListener extends ChangeListener {

            private Game game;
            private Screen screen;

            public BackButtonListener(final Game game, Screen screen) {

                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new GameModeScreen(this.game));
                this.screen.dispose();
            }
        }
        backButton.addListener(new BackButtonListener(myGame, this));

        // draw_water(height_map);
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
     * @param height_map that contains the height at each location
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
     * @param delta
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
/*
        for(int i = 0 ; i < friction_map.length ; i++){
            for(int j = 0 ; j < friction_map[0].length ; j++){
                friction_map[i][j] = 0.131; //basic value for the friction
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
             test = new PuttingCourse(height_map, friction_map, start, flag, max_velocity, hole_tolerance, out_of_bounds_height, out_of_bounds_friction, ball);
        }
        */
    }
    /**
     * libGDX method
     * @param delta
     */
    public void update(float delta) {

        stage.act(delta);
    }

    /**
     * libGDX method
     * @param width and int height of the window
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


