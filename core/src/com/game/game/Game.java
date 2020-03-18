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
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Game extends ApplicationAdapter{

	private PerspectiveCamera camera;
	private ModelBatch modelbatch;
	private ModelBuilder modelbuilder;
	private Model course;
	private ModelInstance courseInstance;
	private Environment environment;
	public static Vector3 origin = new Vector3(0f,0f,0f);
	private PuttingSimulator simulator;

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
		PuttingCourse game_course = new PuttingCourse(height_map, friction_map, start, hole);
		PhysicsEngine game_engine = new PhysicsEngine();
		simulator = new PuttingSimulator(game_course, game_engine);



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
		

	}

	@Override
	public void render () {


	}


}
