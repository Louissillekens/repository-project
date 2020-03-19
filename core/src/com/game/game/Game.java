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
	private ModelBatch modelbatch;
	private ModelBuilder modelBuilder;
	private Model course;
	private ModelInstance courseInstance;
	private Environment environment;
	public static Vector3 origin = new Vector3(0f,0f,0f);
	private MeshBuilder meshBuilder;

	@Override
	public void create () {

		double[][] height_map = new double[10][10];
		for(int i = 0 ; i < height_map.length ; i++){
			for(int j = 0 ; j < height_map[0].length ; j++){
				height_map[i][j] = 1;
			}
		}

		double[][] friction_map = new double[10][10];
		for(int i = 0 ; i < friction_map.length ; i++){
			for(int j = 0 ; j < friction_map[0].length ; j++){
				friction_map[i][j] = 0.131;
			}
		}

		Vector2d start = new Vector2d(1,1);
		Vector2d hole = new Vector2d(8,8);



		camera = new PerspectiveCamera(
				75,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		// Move the camera 5 units back along the z-axis and look at the origin
		camera.position.set(0f,0f,7f);
		camera.lookAt(0f,0f,0f);

		// Near and Far (plane) represent the minimum and maximum ranges of the camera in, um, units
		camera.near = 0.1f;
		camera.far = 300.0f;

		// A ModelBuilder can be used to build meshes by hand
		modelBuilder = new ModelBuilder();
		modelbatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f,0.8f,0.8f, 1f));

		meshBuilder = new MeshBuilder();



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
}
