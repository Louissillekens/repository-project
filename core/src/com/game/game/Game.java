package com.game.game;

import code.Screens.IntroScreen;
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

