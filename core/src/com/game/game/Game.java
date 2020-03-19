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

	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 720);
		batch = new SpriteBatch();
		bitmapFont = new BitmapFont();
		

		this.setScreen(myScreen =new GameScreen(this));
		
	}

	public void create_ball(){
		golfball.reset_ball();
		Vector2d position = golfball.get_P();
		double x= position.get_x();
		double y = position.get_y();
		myScreen.draw_ball(x, y);
	}
	//this takes in start acceleration
	public void move_ball(double ax, double ay){
		golfball.set_start_acceleration(ax, ay);
		//need to add that when the Position of the ball is in the hole that it does something!
		while(golfball.get_V()!=(0,0) && golfball.get_a()!=(0,0){
			Thread.sleep(30);
			golfball.update_all();
			Vector2d position = golfball.get_P();
			double x= position.get_x();
			double y = position.get_y();
			myScreen.draw_ball(x, y);			
		}
	}

	@Override
	public void render() {

		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
/*
package com.game.game;

		import code.Board.PuttingCourse;
		import code.Board.Vector2d;
		import code.Game.PuttingSimulator;
		import code.Physics.PhysicsEngine;
		import com.badlogic.gdx.ApplicationAdapter;
		import com.badlogic.gdx.Gdx;
		import com.badlogic.gdx.Input;
		import com.badlogic.gdx.InputProcessor;
		import com.badlogic.gdx.graphics.*;
		import com.badlogic.gdx.graphics.g2d.SpriteBatch;
		import com.badlogic.gdx.graphics.g3d.*;
		import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
		import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
		import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
		import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
		import com.badlogic.gdx.math.Vector3;

public class Game extends ApplicationAdapter implements InputProcessor{

	private PerspectiveCamera camera;

	@Override
	public void create () {

		PuttingSimulator a = new PuttingSimulator(TestCourse,EulerSolver);

		camera = new PerspectiveCamera(
				75,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		// Move the camera 10 units back along the y-axis and look at the origin (above course)
		camera.position.set(0f,10f,0f);
		camera.lookAt(0f,0f,0f);

		// Near and Far (plane) represent the minimum and maximum ranges of the camera in, um, units
		camera.near = 0.1f;
		camera.far = 300.0f;



	}
	public void move_ball() throws InterruptedException {
		while(a.get_V()!=(0,0)&& a.get_Vprev()!=(0,0)){
			a.update_all();
			//somehow make the ball move to it's new coordinates
			Thread.sleep(30);
		}
	}



	@Override
	public void render () {


	}


	@Override
	public boolean keyDown(int keycode) {

		if(keycode == Input.Keys.LEFT)
			camera.translate(1f,0f,0f);
		if(keycode == Input.Keys.RIGHT)
			camera.translate(-1f,0f,0f);
		if(keycode == Input.Keys.UP)
			camera.translate(0f,1f,0f);
		if(keycode == Input.Keys.DOWN)
			camera.translate(0f,-1f,0f);

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}*/
