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

public class Game extends ApplicationAdapter{

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

	public static void buildTerrain(MeshPartBuilder b){
		int gridWidth=50;
		int gridDepth=50;
		float scale = 1f;
		Vector3 pos1,pos2,pos3,pos4;
		Vector3 nor1,nor2,nor3,nor4;
		MeshPartBuilder.VertexInfo v1,v2,v3,v4;
		for(int i=-gridWidth/2;i<gridWidth/2;i++){
			for(int k=-gridDepth/2;k<gridDepth/2;k++){
				pos1 = new Vector3 (i,(float)(Math.sin(i)+Math.sin(k))/3f,k);
				pos2 = new Vector3 (i,(float)(Math.sin(i)+Math.sin(k+1))/3f,k+1);
				pos3 = new Vector3 (i+1,(float)(Math.sin(i+1)+Math.sin(k+1))/3f,k+1);
				pos4 = new Vector3 (i+1,(float)(Math.sin(i+1)+Math.sin(k))/3f,k);

				nor1 = (new Vector3((float)-Math.cos(i)/3f,1,0).add(new Vector3(0,1,(float)-Math.cos(k)/3f)));
				nor2 = (new Vector3((float)-Math.cos(i)/3f,1,0).add(new Vector3(0,1,(float)-Math.cos(k+1)/3f)));
				nor3 = (new Vector3((float)-Math.cos(i+1)/3f,1,0).add(new Vector3(0,1,(float)-Math.cos(k+1)/3f)));
				nor4 = (new Vector3((float)-Math.cos(i+1)/3f,1,0).add(new Vector3(0,1,(float)-Math.cos(k)/3f)));

				v1 = new MeshPartBuilder.VertexInfo().setPos(pos1).setNor(nor1).setCol(null).setUV(0.5f, 0.0f);
				v2 = new MeshPartBuilder.VertexInfo().setPos(pos2).setNor(nor2).setCol(null).setUV(0.0f, 0.0f);
				v3 = new MeshPartBuilder.VertexInfo().setPos(pos3).setNor(nor3).setCol(null).setUV(0.0f, 0.5f);
				v4 = new MeshPartBuilder.VertexInfo().setPos(pos4).setNor(nor4).setCol(null).setUV(0.5f, 0.5f);

				b.rect(v1, v2, v3, v4);
			}
		}

		//nor2.sub(pos1).crs(nor4.sub(pos1)).toString();
		//System.out.println(nor2.toString());

//		VertexInfo v5 = new VertexInfo().setPos(1, 0, 0).setNor(0, 1, 0).setCol(null).setUV(0.5f, 0.0f);
	}

	@Override
	public void render () {


	}


}
