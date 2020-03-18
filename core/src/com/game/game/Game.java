package com.game.game;

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

public class Game extends ApplicationAdapter implements InputProcessor {

	private PerspectiveCamera cam;
	private ModelBatch modelbatch;
	private ModelBuilder modelbuilder;
	private Model box;
	private ModelInstance modelInstance;
	private Environment environment;
	public static Vector3 origin = new Vector3(0f,0f,0f);

	@Override
	public void create () {

		cam = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 0f, 5f);
		cam.lookAt(origin);
		cam.near = 0.1f;
		cam.far = 250f;

		modelbatch = new ModelBatch();
		modelbuilder = new ModelBuilder();

		box = modelbuilder.createBox(2f,2f,2f, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);

		modelInstance = new ModelInstance(box,0,0,0);
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f,0.8f,0.8f, 1f));

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);

		cam.update();
		modelbatch.begin(cam);
		modelbatch.render(modelInstance);
		modelbatch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.LEFT)
			cam.rotateAround(new Vector3(0f,0f,0f), new Vector3(0f,1f,0f), 1f);
		if(keycode == Input.Keys.RIGHT)
			cam.rotateAround(new Vector3(0f,0f,0f), new Vector3(0f,1f,0f), -1f);
		if(keycode == Input.Keys.UP)
			cam.rotateAround(new Vector3(0f,0f,0f), new Vector3(1f,0f,0f), 1f);
		if(keycode == Input.Keys.DOWN)
			cam.rotateAround(new Vector3(0f,0f,0f), new Vector3(1f,0f,0f), -1f);


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
