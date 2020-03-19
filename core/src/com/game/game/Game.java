package com.game.game;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import code.Board.*;
public class Game extends com.badlogic.gdx.Game{

	public static final String TITLE = "Crazy Putting";
	public static final int WIDTH = 1080;
	public static final int HEIGHT = 720;

	public OrthographicCamera camera;
	public SpriteBatch batch;

	public BitmapFont bitmapFont;
	public GameScreen myScreen;
	public Ball golfball;

	/**
	 * libGDX method that creates the frame
	 */
	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 720);
		batch = new SpriteBatch();
		bitmapFont = new BitmapFont();

		this.setScreen(myScreen =new GameScreen(this));
	}

	/**
	 * void method that creates a ball
	 */
	public void create_ball(){
		golfball.reset_ball();
		Vector2d position = golfball.get_P();
		double x= position.get_x();
		double y = position.get_y();
		myScreen.draw_ball(x, y);
	}

	/**
	 * void method to move the ball
	 * @param double ax and ay that correspond to the acceleration
	 */
	public void move_ball(double ax, double ay){
		golfball.set_start_acceleration(ax, ay);
		//need to add that when the Position of the ball is in the hole that it does something!
		while((golfball.get_V().get_x() != 0 && golfball.get_V().get_y() != 0)
				&& ((golfball.get_a().get_x() != 0 && golfball.get_a().get_y() != 0) && !golfball.inhole())){

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			golfball.update_all();
			Vector2d position = golfball.get_P();
			double x= position.get_x();
			double y = position.get_y();
			myScreen.draw_ball(x, y);
		}
		if(golfball.inhole() == true){
			//display course complete
		}else if(golfball.inwater()==true){
			golfball.reset_ball();
		}else{
			//swing again
		}
	}

	/**
	 * libGDX method
	 */
	@Override
	public void render() {

		super.render();
	}

	/**
	 * libGDX method
	 */
	@Override
	public void dispose() {
		batch.dispose();
	}
}

