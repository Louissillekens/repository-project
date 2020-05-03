package code.Screens;

import code.Controller.InputHandler;
import code.Board.Ball;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    // Instance for the ball in the game
    private Ball ball;
    private Model ballModel;
    private ModelInstance ballInstance;
    private float ballSize = 0.2f;
    private float xPosition = 0;
    private float zPosition = 0;

    // Instance for the 3D field
    private Model flatField;
    private ModelInstance[] fieldInstance;
    private Model slopeModel;
    private ModelInstance[] slopeInstance;

    // Instance for the height storage
    private static double[][] heightStorage;

    // Instance vector for the camera
    public static Vector3 vector1;
    public static Vector3 vector2;

    // Instance for the time between two frame
    private final float delta = 1/60f;

    // Instance fot the previously chosen game mode
    private GameMode gameMode;

    private static float[] fieldArray;
    private int numberOfFields;

    //these variables are to decide the shot_speed
    private final double POWER_INCREMENT = 0.01;//m/s
    private final double MAX_SPEED = 3;//for now the max speed is 3 (should be possible to change per course)
    private final double STARTING_SHOT_POWER = 0;
    private double shot_Power = STARTING_SHOT_POWER;//m/s


    // Constructor that creates the 3D field + corresponding game mode
    public PuttingGameScreen(final Game game, GameMode gameMode) {

        this.game = game;

        this.createField();
        this.render(delta);

        this.gameMode = new GameMode(gameMode.gameName);
    }

    // Method that creates the 3D field
    public void createField() {

        // Creation of the 3D camera
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(4f, 3f, 4f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 400f;

        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();

        // Creation of a blue flat field that corresponds to the water (need to verify the height of this field)
        flatField = modelBuilder.createBox(50, 1f, 50,
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );

        // Different pair of 2 fields can be played together
        // int fieldModel is used to select which terrain we want to apply
        int fieldModel = 1;

        numberOfFields = 2;
        fieldInstance = new ModelInstance[numberOfFields];
        slopeInstance = new ModelInstance[numberOfFields];

        for (int index = fieldModel; index < numberOfFields+fieldModel; index++) {

            // Adding all the mesh (green triangle) to the method that build the field
            modelBuilder.begin();
            meshPartBuilder = modelBuilder.part("field", GL20.GL_TRIANGLES,
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                    new Material(ColorAttribute.createDiffuse(Color.GREEN)));
            buildField(meshPartBuilder, index);
            slopeModel = modelBuilder.end();

            fieldInstance[index-fieldModel] = new ModelInstance(flatField, 0f, -1f, (-index+fieldModel) * 50);
            slopeInstance[index-fieldModel] = new ModelInstance(slopeModel, 0f, 0f, (-index+fieldModel) * 50);
        }

        // Creation of the ball
        ballModel = modelBuilder.createSphere(ballSize, ballSize, ballSize, 10, 10,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ballInstance = new ModelInstance(ballModel, xPosition, (getHeight(0,0, fieldModel))+(ballSize/2), zPosition);

        // Adding an environment which is used for the luminosity of the frame
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1.f));
        environment.add(new DirectionalLight().set(0.6f, 0.6f, 0.6f, -1f, -1.0f, 1f));
    }

    // Method that defines the function we use to create the slope of the field
    public static float[] defineFunction(double i, double j) {

        fieldArray = new float[5];

        float field0 = (float) (0);
        float field1 = (float) (Math.sin(i) + Math.sin(j))/3;
        float field2 = (float) (Math.sin(i))/3;
        float field3 = (float) -((Math.atan(i) + Math.atan(j))/2);
        float ripple = (float) ((0.55)+Math.sin((0.4)*(Math.pow(i,2)+Math.pow(j,2))/10));

        fieldArray[0] = field0;
        fieldArray[1] = field1;
        fieldArray[2] = field2;
        fieldArray[3] = field3;
        fieldArray[4] = ripple;

        return fieldArray;
    }

    // Method that build the 3D field from a mesh
    // Need to be improved to give another function as input to have other kinds of fields
    public static void buildField(MeshPartBuilder b, int index){

        int gridWidth=50;
        int gridDepth=50;
        heightStorage = new double[gridWidth+1][gridDepth+1];
        Vector3 p1,p2,p3,p4;
        Vector3 n1,n2,n3,n4;
        MeshPartBuilder.VertexInfo v1,v2,v3,v4;
        for(int i = -gridWidth/2; i < gridWidth/2; i++) {
            for (int j = -gridDepth / 2; j < gridDepth / 2; j++) {

                float[] p1StepArray = defineFunction(i, j);
                float[] p2StepArray = defineFunction(i, j+1);
                float[] p3StepArray = defineFunction(i+1, j+1);
                float[] p4StepArray = defineFunction(i+1, j);

                float p1Step = p1StepArray[index];
                float p2Step = p2StepArray[index];
                float p3Step = p3StepArray[index];
                float p4Step = p4StepArray[index];

                p1 = new Vector3(i, p1Step, j);
                p2 = new Vector3(i, p2Step, j+1);
                p3 = new Vector3(i+1, p3Step, j+1);
                p4 = new Vector3(i+1, p4Step, j);

                float[] n1StepArray = defineFunction(i, 0);
                float[] n2StepArray = defineFunction(i+1, 0);
                float[] n3StepArray = defineFunction(0, j);
                float[] n4StepArray = defineFunction(0, j+1);

                float n1Step = -(n1StepArray[index]);
                float n2Step = -(n2StepArray[index]);
                float n3Step = -(n3StepArray[index]);
                float n4Step = -(n4StepArray[index]);

                n1 = new Vector3(-n1Step, 1, -n3Step);
                n2 = new Vector3(-n1Step, 1, -n4Step);
                n3 = new Vector3(-n2Step, 1, -n4Step);
                n4 = new Vector3(-n2Step, 1, -n3Step);

                v1 = new MeshPartBuilder.VertexInfo().setPos(p1).setNor(n1).setCol(null).setUV(0.5f, 0.0f);
                v2 = new MeshPartBuilder.VertexInfo().setPos(p2).setNor(n2).setCol(null).setUV(0.0f, 0.0f);
                v3 = new MeshPartBuilder.VertexInfo().setPos(p3).setNor(n3).setCol(null).setUV(0.0f, 0.5f);
                v4 = new MeshPartBuilder.VertexInfo().setPos(p4).setNor(n4).setCol(null).setUV(0.5f, 0.5f);

                b.rect(v1, v2, v3, v4);

            }
        }
    }

    // Method to get the value of the height y at a certain point (x, z)
    public float getHeight(double x, double z, int index) {

        float[] heightArray = defineFunction(x, z);
        return heightArray[index];
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();

        // Adding the ModelInstance array to the model
        // for the moment I only chose to take the first element of the array of size 100
        // If we want a bigger field we can take the following indexes (every index holds a field of the same size)
        modelBatch.begin(camera);
            modelBatch.render(ballInstance);
            for (int i = 0; i < numberOfFields; i++) {
                modelBatch.render(fieldInstance[i], environment);
                modelBatch.render(slopeInstance[i], environment);
            }
            //modelBatch.render(fieldInstance[0], environment);
            //modelBatch.render(slopeInstance[0], environment);
        modelBatch.end();

        InputHandler.checkForInput(this);
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

    public PerspectiveCamera getCamera(){
        return camera;
    }

    public Game getGame(){
        return game;
    }

    public void IncrementShotPower(int amount){
        shot_Power += amount*POWER_INCREMENT;
    }

    public double getShot_Power(){
        return shot_Power;
    }

    public void setShot_Power(double power){
        shot_Power = power;
    }

    public double getPOWER_INCREMENT(){
        return POWER_INCREMENT;
    }

    public double getMAX_SPEED(){
        return MAX_SPEED;
    }

    public double getSTARTING_SHOT_POWER(){
        return STARTING_SHOT_POWER;
    }
}
