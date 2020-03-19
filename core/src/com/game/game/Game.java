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
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Game extends ApplicationAdapter implements InputProcessor{

	private PerspectiveCamera camera;
	private Environment environment;

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

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

	}
	public void move ball(){
		while(a.get_V()!=(0,0)&&a.get_Vprev()!=(0,0)){
			a.update_all();
			//somehow make the ball move to it's new coordinates
			Thread.sleep(30);
		}
	}



	@Override
	public void render () {

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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

	@Override
	public void dispose () {

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
