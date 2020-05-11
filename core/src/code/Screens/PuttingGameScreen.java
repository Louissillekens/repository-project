package code.Screens;

import code.Controller.InputHandler;
import code.Physics.Rungekuttasolver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.game.Game;

import static com.badlogic.gdx.graphics.GL20.GL_TRIANGLES;

public class PuttingGameScreen implements Screen {

    private Game game;
    private Stage stage;

    // Instance for the scene
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private static ModelBuilder modelBuilder;
    private Environment environment;
    private MeshPartBuilder meshPartBuilder;

    // Instance for the ball in the game
    private Model ball;
    private ModelInstance ballInstance;
    private float ballSize = 0.2f;
    private float ballPositionX;
    private float ballPositionZ;

    // Instance for the 3D field
    private Model flatField;
    private ModelInstance[] fieldInstance;
    private Model slopeModel;
    private ModelInstance[] slopeInstance;
    private Model arrow;
    private ModelInstance arrowInstance;
    private static Color fieldColor;

    // Instance vector for the camera
    public static Vector3 vector1;
    public static Vector3 vector2;

    // Instance for the time between two frame
    private final float delta = 1/60f;

    // Instance fot the previously chosen game mode
    private GameMode gameMode;

    //these variables are to decide the shot_speed
    private final double POWER_INCREMENT = 0.01;//m/s
    private final double MAX_SPEED = 3;//for now the max speed is 3 (should be possible to change per course)
    private final double STARTING_SHOT_POWER = 0;
    private double shot_Power = STARTING_SHOT_POWER;//m/s

    // Instance for the power shoot
    private Rectangle rect1;
    private Rectangle rect2;
    private ShapeRenderer shapeRenderer;

    // Instances for the flag
    private Model flag1;
    private ModelInstance flag1Instance;
    private Model flag2;
    private ModelInstance flag2Instance;
    private static float flagPositionX = 20;
    private static float flagPositionZ = 10;

    private static float winRadius = 5;

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
        camera.position.set(-3.5f, 3f, -3.5f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 400f;

        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();

        // Creation of a blue flat field that corresponds to the water
        flatField = modelBuilder.createBox(5000, 1.0f, 5000,
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );

        int numberOfFields = 1;

        fieldInstance = new ModelInstance[numberOfFields];
        slopeInstance = new ModelInstance[numberOfFields];

        for (int i = 0; i < numberOfFields; i++) {

            // Adding all the mesh (green triangle) to the method that build the field
            modelBuilder.begin();
            buildField();
            slopeModel = modelBuilder.end();

            fieldInstance[i] = new ModelInstance(flatField, 0f, -0.5f, (-i) * 50);
            slopeInstance[i] = new ModelInstance(slopeModel, 0f, 0f, (-i) * 50);
        }

        // Creation of the ball
        ball = modelBuilder.createSphere(ballSize, ballSize, ballSize, 10, 10,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ballInstance = new ModelInstance(ball, ballPositionX,(defineFunction(ballPositionX, ballPositionZ))+(ballSize/2), ballPositionZ);

        shapeRenderer = new ShapeRenderer();

        // Creation of the flag
        flag1 = modelBuilder.createBox(0.1f, 4, 0.1f,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
        flag1Instance = new ModelInstance(flag1, flagPositionX, defineFunction(flagPositionX, flagPositionZ), flagPositionZ);

        flag2 = modelBuilder.createBox(1.5f, 1f, 0.2f,
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );
        flag2Instance = new ModelInstance(flag2, flagPositionX-0.75f, defineFunction(flagPositionX, flagPositionZ)+2.5f, flagPositionZ);

        // Adding an environment which is used for the luminosity of the frame
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1.f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0f, 1f));

    }

    // Method that defines the function we use to create the slope of the field
    public static float defineFunction(double i, double j) {

        float field0 = (float) (0.5);
        float field1 = (float) (((Math.sin(i) + Math.sin(j))/3)+0.5);
        float field2 = (float) (Math.sin(i))/3;
        float field3 = (float) (((Math.atan(i) + Math.atan(j))/2)+0.5);
        float ripple1 = (float) ((0.4)+Math.sin((0.4)*(Math.pow(i,2)+Math.pow(j,2))/10));
        float ripple2 = (float) ((0.1)+(Math.sin((0.4)*(Math.pow(i,2)+Math.pow(j,2)))/10)*4);

        return field1;
    }

    // Method used to get the friction at a certain location
    public static float getFriction(double x, double z) {

        if (defineFunction(x,z) < 0) {
            return -1f;
        }
        return 0.13f;
    }

    // Method that build the 3D field from a mesh
    // Need to be improved to give another function as input to have other kinds of fields
    public static void buildField(){

        int gridWidth = 50;
        int gridDepth = 50;
        Vector3 pos1,pos2,pos3,pos4;
        Vector3 nor1,nor2,nor3,nor4;
        MeshPartBuilder.VertexInfo v1,v2,v3,v4;
        for(int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridDepth; j++) {

                pos1 = new Vector3(i, defineFunction(i, j), j);
                pos2 = new Vector3(i, defineFunction(i, j + 1), j+1);
                pos3 = new Vector3(i+1, defineFunction(i + 1, j + 1), j+1);
                pos4 = new Vector3(i+1, defineFunction(i + 1, j), j);

                nor1 = new Vector3(-defineFunction(i, 0), 1, -defineFunction(0, j));
                nor2 = new Vector3(-defineFunction(i, 0), 1, -defineFunction(0, j + 1));
                nor3 = new Vector3(-defineFunction(i + 1, 0), 1, -defineFunction(0, j + 1));
                nor4 = new Vector3(-defineFunction(i + 1, 0), 1, -defineFunction(0, j));

                v1 = new MeshPartBuilder.VertexInfo().setPos(pos1).setNor(nor1).setCol(null).setUV(0.5f, 0.0f);
                v2 = new MeshPartBuilder.VertexInfo().setPos(pos2).setNor(nor2).setCol(null).setUV(0.0f, 0.0f);
                v3 = new MeshPartBuilder.VertexInfo().setPos(pos3).setNor(nor3).setCol(null).setUV(0.0f, 0.5f);
                v4 = new MeshPartBuilder.VertexInfo().setPos(pos4).setNor(nor4).setCol(null).setUV(0.5f, 0.5f);

                float meanX = (pos1.x + pos2.x + pos3.x + pos4.x)/4;
                float meanZ = (pos1.z + pos2.z + pos3.z + pos4.z)/4;

                System.out.println(Math.sqrt(Math.pow((i-flagPositionX), 2) + Math.pow((j-flagPositionZ), 2)));
                if (euclideanDist(meanX, meanZ) < winRadius) {
                    fieldColor = Color.PURPLE;
                }
                else {
                    fieldColor = Color.GREEN;
                }

                MeshPartBuilder b = modelBuilder.part("field", GL_TRIANGLES,
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                        new Material(ColorAttribute.createDiffuse(fieldColor)));

                b.rect(v1, v2, v3, v4);
            }
        }
    }

    public static float euclideanDist(float positionX, float positionZ) {

        return (float) Math.sqrt(Math.pow((positionX-flagPositionX), 2) + Math.pow((positionZ-flagPositionZ), 2));
    }

    public boolean isWin(float positionX, float positionZ) {

        if (euclideanDist(positionX, positionZ) < winRadius) {
            return true;
        }
        else {
            return false;
        }
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

        modelBatch.render(ballInstance, environment);
        /*for (int i = 0; i < 2; i++) {
            modelBatch.render(fieldInstance[i], environment);
            modelBatch.render(slopeInstance[i], environment);
        }*/
        modelBatch.render(fieldInstance[0], environment);
        modelBatch.render(slopeInstance[0], environment);

        // Creation of the arrow
        arrow = modelBuilder.createArrow(ballPositionX, 2f, ballPositionZ,
                ((camera.direction.x)*5)+(ballPositionX), 3f, ((camera.direction.z)*5)+(ballPositionZ), 0.1f, 0.1f, 10,
                GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        arrowInstance = new ModelInstance(arrow);

        modelBatch.render(arrowInstance, environment);

        modelBatch.render(flag1Instance, environment);

        modelBatch.render(flag2Instance, environment);

        modelBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        int scale = 40;

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(20, 20, 13, (float) (getMAX_SPEED()*(scale))+4);
        shapeRenderer.setColor(Color.RED);
        if ((getShot_Power()*(scale)) < getMAX_SPEED()*(scale)) {
            shapeRenderer.rect(22, 22, 8, (float) ((getShot_Power()) * (scale)));
        }

        shapeRenderer.end();

        //key input to take shot
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            double x_direction = getCamera().direction.x;
            double z_direction = getCamera().direction.z;
            double power = getShot_Power();

            System.out.println("first place of the ball 0, 0");

            Rungekuttasolver solver = new Rungekuttasolver();
            solver.setValues(ballPositionX, ballPositionZ, (x_direction*power)*300, (z_direction*power)*300);
            solver.RK4();

            ballPositionX = (float) solver.getX();
            ballPositionZ = (float) solver.getY();

            ballInstance = new ModelInstance(getBall(), ballPositionX,
                    (PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ)+ getBallSize()/2), ballPositionZ);

            getModelBatch().render(ballInstance);

            System.out.println("shot taken with power: " + power + " and x_direction: " + x_direction + " and y_direction: " + z_direction);
            //reset the power
            setShot_Power(getSTARTING_SHOT_POWER());

            camera.position.x = ballPositionX-5;
            camera.position.z = ballPositionZ-5;
            camera.position.y = 5;

            camera.lookAt(ballPositionX,0,ballPositionZ);

        }

        InputHandler.checkForInput(this);
        //camera.lookAt(ballPositionX,0,ballPositionZ);
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

    public float getBallSize() {
        return ballSize;
    }

    public ModelInstance getBallInstance() {
        return ballInstance;
    }

    public Model getBall() {
        return ball;
    }

    public ModelBatch getModelBatch() {
        return modelBatch;
    }

    public float getBallPositionX() {
        return ballPositionX;
    }

    public float getBallPositionZ() {
        return ballPositionZ;
    }
}