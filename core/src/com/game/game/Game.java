package com.game.game;

import code.Screens.IntroScreen;
import code.Screens.PuttingGameScreen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends com.badlogic.gdx.Game{

	public static final String TITLE = "Crazy Putting";
	public static final int WIDTH = 1080;
	public static final int HEIGHT = 720;

	public SpriteBatch batch;

	public BitmapFont bitmapFont;

	/**
	 * libGDX method that creates the frame
	 */
	@Override
	public void create () {

		batch = new SpriteBatch();
		bitmapFont = new BitmapFont();

		this.setScreen(new IntroScreen(this));
		//this.setScreen(new PuttingGameScreen());
	}

	/*
	public void create_ball(){
		golfball.reset_ball();
		Vector2d position = golfball.get_P();
		double x = position.get_x();
		double y = position.get_y();
		//myScreen.draw_ball(x, y);
	}

	/**
	 * void method to move the ball
	 * @param ax and ay that correspond to the acceleration
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
			//myScreen.draw_ball(x, y);
		}
	}*/

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

