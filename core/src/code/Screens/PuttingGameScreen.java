package code.Screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.game.game.Game;

public class PuttingGameScreen implements Screen {

    private Game game;
    private Stage stage;

    // Instance for the scene
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private ModelBuilder modelBuilder;
    private Environment environment;
    private MeshPartBuilder meshPartBuilder;

    // Instance for the ball
    private Model ball;
    private ModelInstance ballInstance;
    private float ballSize = 0.2f;

    // Instance slopeModel for the field
    private Model flatField;
    private ModelInstance[] fieldInstance;
    private Model slopeModel;
    private ModelInstance[] slopeInstance;

    // Instance for the height
    private static double[][] heightStorage;

    // Instance vector for the camera
    private static Vector3 vector1;
    private static Vector3 vector2;

    // Instance for the time between two frame
    private final float delta = 1/60f;

    private GameMode gameMode;

    private TextButton backButton;
    private Skin skin;

    public PuttingGameScreen(final Game game, GameMode gameMode) {

        this.game = game;
        this.gameMode = gameMode;

        this.createField();
        this.render(delta);
    }

    public void createField() {

        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(3f, 3f, 3f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 400f;

        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();

        flatField = modelBuilder.createBox(50, 1, 50,
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );

        //environment = new Environment();
        //environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f,0.8f,0.8f,1f));

        // Creation of the ball
        ball = modelBuilder.createSphere(ballSize, ballSize, ballSize, 10, 10,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ballInstance = new ModelInstance(ball, 0, 0.1f, 0);

        modelBuilder.begin();
        meshPartBuilder = modelBuilder.part("grid", GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)));
        buildTerrain(meshPartBuilder);
        slopeModel = modelBuilder.end();

        fieldInstance = new ModelInstance[100];
        slopeInstance = new ModelInstance[100];
        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                fieldInstance[i * 10 + k] = new ModelInstance(flatField, -i * 50, -1, -k * 50);
                slopeInstance[i * 10 + k] = new ModelInstance(slopeModel, -i * 50, 0, -k * 50);
            }
        }

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1.f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -1.0f, 1f));
    }

    public static void buildTerrain(MeshPartBuilder b){

        int gridWidth=50;
        int gridDepth=50;
        heightStorage = new double[gridWidth+1][gridDepth+1];
        Vector3 pos1,pos2,pos3,pos4;
        Vector3 nor1,nor2,nor3,nor4;
        MeshPartBuilder.VertexInfo v1,v2,v3,v4;
        for(int i=-gridWidth/2;i<gridWidth/2;i++)
            for (int j = -gridDepth / 2; j < gridDepth / 2; j++) {

                pos1 = new Vector3(i, (float) (Math.sin(i) + Math.sin(j)) / 3f, j);
                pos2 = new Vector3(i, (float) (Math.sin(i) + Math.sin(j + 1)) / 3f, j + 1);
                pos3 = new Vector3(i + 1, (float) (Math.sin(i + 1) + Math.sin(j + 1)) / 3f, j + 1);
                pos4 = new Vector3(i + 1, (float) (Math.sin(i + 1) + Math.sin(j)) / 3f, j);

                heightStorage[((int) pos1.x)+25][((int) pos1.z)+25] = pos1.y;
                heightStorage[((int) pos2.x)+25][((int) pos2.z)+25] = pos2.y;
                heightStorage[((int) pos3.x)+25][((int) pos3.z)+25] = pos3.y;
                heightStorage[((int) pos4.x)+25][((int) pos4.z)+25] = pos4.y;

                nor1 = new Vector3((float) -Math.cos(i) / 3f, 1, (float) -Math.cos(j) / 3f);
                nor2 = new Vector3((float) -Math.cos(i) / 3f, 1, (float) -Math.cos(j + 1) / 3f);
                nor3 = new Vector3((float) -Math.cos(i + 1) / 3f, 1, (float) -Math.cos(j + 1) / 3f);
                nor4 = new Vector3((float) -Math.cos(i + 1) / 3f, 1, (float) -Math.cos(j) / 3f);

                v1 = new MeshPartBuilder.VertexInfo().setPos(pos1).setNor(nor1).setCol(null).setUV(0.5f, 0.0f);
                v2 = new MeshPartBuilder.VertexInfo().setPos(pos2).setNor(nor2).setCol(null).setUV(0.0f, 0.0f);
                v3 = new MeshPartBuilder.VertexInfo().setPos(pos3).setNor(nor3).setCol(null).setUV(0.0f, 0.5f);
                v4 = new MeshPartBuilder.VertexInfo().setPos(pos4).setNor(nor4).setCol(null).setUV(0.5f, 0.5f);

                b.rect(v1, v2, v3, v4);
            }
    }

    // method to get the value of the height y at a certain point (x, z)
    public double getHeight(double x, double z) {

        return heightStorage[((int) x)+25][((int) z)+25];
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        modelBatch.begin(camera);
            modelBatch.render(ballInstance);
            //for (int i = 0; i < 100; i++) {
            //  modelBatch.render(fieldInstance[i], environment);
            //  modelBatch.render(slopeInstance[i], environment);
            //}
            modelBatch.render(fieldInstance[0], environment);
            modelBatch.render(slopeInstance[0], environment);
        modelBatch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.lookAt(0,0,0);
            camera.rotateAround(vector1 = new Vector3(0f, 0f, 0f), vector2 = new Vector3(0f, 1f, 0f), -1f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.lookAt(0,0,0);
            camera.rotateAround(vector1 = new Vector3(0f, 0f, 0f), vector2 = new Vector3(0f, 1f, 0f), 1f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.lookAt(0,0,0);
            if ((camera.position.y > 1)) {
                camera.translate(0, -0.1f, 0);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.lookAt(0,0,0);
            if (camera.position.y < 15) {
                camera.translate(0, 0.1f, 0);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.B)) {
            this.game.setScreen(new GameModeScreen(this.game));
        }
    }

    @Override
    public void show() {

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

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        modelBatch.dispose();
    }
}
