package code.Screens;

import code.Bot.AgentBot;
import code.Bot.PuttingBotDeployement;
import code.Physics.Rungekuttasolver;
import code.Physics.VerletSolver;
import code.astar.AStar;
import code.util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.game.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_TRIANGLES;

/**
 * A screen class to render a putting game
 * @author Clément Detry
 */
public class PuttingGameScreen implements Screen {

    // Instance variable for the current game
    private Game game;

    //this handler takes care of all input from the player
    InputHandler handler = new InputHandler();

    // Instances variables for the perspective scene
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private MeshPartBuilder meshPartBuilder;
    private static ModelBuilder modelBuilder;
    private Environment environment;
    private float directionX;
    private float directionZ;

    // Instances variables for the ball in the game
    //private Model ball;
    private ModelInstance ballInstance;
    private float startingPositionX = 2;
    private float startingPositionZ = 2;
    private static float ballSize = 0.2f;
    private float ballPositionX = startingPositionX;
    private float ballPositionZ = startingPositionZ;
    private float newBallPositionX;
    private float newBallPositionZ;
    private float ballStepXmean;
    private float ballStepZmean;

    // Instances variables for the 3D field
    private Model flatField;
    private ModelInstance[] fieldInstance;
    private Model slopeModel;
    private ModelInstance[] slopeInstance;
    private Model arrow;
    private ModelInstance arrowInstance;
    private static Color fieldColor;
    private static int numberOfFields = 1;
    private static float gridWidth = 50;
    private static float gridDepth = 50;

    // Instances vectors for the camera rotations
    public static Vector3 vector1;
    public static Vector3 vector2;

    // Instance variable for the chosen game mode
    private GameMode gameMode;

    // Instances variable used for the shot power
    private final double POWER_INCREMENT = 0.025;//m/s
    private static final double MAX_SPEED = 3;//for now the max speed is 3 (should be possible to change per course)
    private final double STARTING_SHOT_POWER = 0.000001;
    private double shot_Power = STARTING_SHOT_POWER;//m/s
    private ShapeRenderer shapeRenderer;

    // Instances variables for the flag
    private Model flag1;
    private ModelInstance flag1Instance;
    private Model flag2;
    private ModelInstance flag2Instance;
    private static float flagPositionX;
    private static float flagPositionZ;
    private static float winRadius = 3;

    // Instances variables used to store the different positions of the ball
    private static int countIndex = 0;
    private float[] positionArrayX = new float[100];
    private float[] positionArrayZ = new float[100];

    // Instances variables used to store the different translations of the camera
    private float sumX = 0;
    private float[] translateX = new float[100];
    private float sumZ = 0;
    private float[] translateZ = new float[100];
    private static boolean trackShot = false;
    private static boolean canTranslateCam = false;
    private static boolean canReset = false;

    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont font = new BitmapFont();
    private float fontX, fontY;
    private GlyphLayout layout = new GlyphLayout();
    private float timer = 0;
    private int period = 2;

    private drawObject drawObject = new drawObject();

    private Array<ModelInstance> instances;
    private Model ballModel;

    private ModelInstance ball;

    /*
    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;

    private btCollisionShape ballShape;
    private btCollisionShape trunkShape;
    private btCollisionShape branchesShape;

    private btCollisionObject ballObject;
    private btCollisionObject trunkObject;
    private btCollisionObject branchesObject;

    private btCollisionShape lineShape;
    private btCollisionObject lineObject;

    private Array<btCollisionObject> obstacleObjects = new Array<>();
    */

    private boolean ballStop = true;

    private float vx;
    private float vy;

    private boolean isDetectorCollision1 = false;
    private boolean isDetectorCollision2 = false;
    private boolean isDetectorCollision3 = false;
    private boolean isDetectorCollision4 = false;
    private boolean isDetectorCollision5 = false;
    private boolean isDetectorCollision6 = false;
    private boolean isDetectorCollision7 = false;
    private boolean isDetectorCollision8 = false;
    private boolean isDetectorCollision9 = false;
    private boolean isDetectorCollision10 = false;
    private boolean isDetectorCollision11 = false;
    private int numberOfLinesSensors = 50;

    private int numberOfTree = 30;
    private static float[] treePositionX;
    private static float[] treePositionZ;

    private boolean[] canHitFlag = new boolean[11];
    private boolean[] isSensorOnSand = new boolean[11];
    private float[] sensorsSize = new float[11];
    private static float[] maxPositionX = new float[11];
    private static float[] maxPositionZ = new float[11];
    private static float[] maxPositionAgentX = new float[11];
    private static float[] maxPositionAgentZ = new float[11];
    private float[] minEuclideanDist = new float[11];
    private float[] sensorsAngleX = new float[11];
    private float[] sensorsAngleY = new float[11];
    private float[] sensorsAngleZ = new float[11];
    private float[] sensorUpX = new float[11];
    private float[] sensorUpY = new float[11];
    private float[] sensorUpZ = new float[11];
    private float[] sensorPositionX = new float[11];
    private float[] sensorPositionY = new float[11];
    private float[] sensorPositionZ = new float[11];
    private boolean[] checkForSensorsStep = new boolean[11];

    private Array<ModelInstance> sensors = new Array<>();

    private float camDirectionFlagX;
    private float camDirectionFlagY;
    private float camDirectionFlagZ;
    private float camUpFlagX;
    private float camUpFlagY;
    private float camUpFlagZ;
    private float camPositionFlagX;
    private float camPositionFlagY;
    private float camPositionFlagZ;

    private float finalPositionArrowX;
    private float finalPositionArrowZ;

    private float minDistanceArrowFlag;

    private boolean checkForSensors = false;
    private boolean sensorsReady = false;
    private boolean botReady = false;
    private boolean isBotReady = false;
    private boolean findFlag = false;
    private boolean check = false;
    private boolean checkCollisionMessage = false;

    private float botTimer1 = 0;
    private float botTimer2 = 0;
    private int countForFlag = 0;
    private int countForBot = 0;
    private int bestSensor = 0;
    private int count = 0;
    private int counter = 0;
    private int countForSensors = 0;
    private int countForSensorsReady = 0;

    private float[] stepPositionX = new float[sensorsSize.length*10];
    private float[] stepPositionZ = new float[sensorsSize.length*10];

    private static ArrayList<float[]> sensorsData = new ArrayList<>();
    private static ArrayList<float[]> sensorsOutput = new ArrayList<float[]>();

    private static int sandPitSize = 5;
    private static int amountOfSandPit = 10;
    private static float[] sandPitX1 = new float[amountOfSandPit];
    private static float[] sandPitZ1 = new float[amountOfSandPit];
    private static float[] sandPitX2 = new float[amountOfSandPit];
    private static float[] sandPitZ2 = new float[amountOfSandPit];

    private boolean checkSolver = false;

    private float agentPower;

    private float stepForMaxX;
    private float stepForMaxZ;
    private float[] saveStepX = new float[numberOfLinesSensors];
    private float[] saveStepZ = new float[numberOfLinesSensors];

    private Array<ModelInstance> instancesTest = new Array<>();

    private boolean checkCamera = false;

    private int countForSensor0Ready = 0;

    public static boolean finishAgent;

    private String name;

    /**
     * Constructor that creates a new instance of the putting game screen
     * @param game current instance of the game
     * @param gameMode preciously chosen game mode (not working yet)
     */
    public PuttingGameScreen(final Game game, GameMode gameMode) {

        this.game = game;
        this.gameMode = new GameMode(gameMode.gameName);
        this.createField();
    }

    public PuttingGameScreen(float startingPositionX, float startingPositionZ) {

        this.ballPositionX = startingPositionX;
        this.ballPositionZ = startingPositionZ;
        this.name = "NN";
        finishAgent = false;
        this.gameMode = new GameMode("Bot");
        BotScreen.setBotName("agent");
        this.createField();
    }

    /**
     * Method used to create the 3D field
     */
    public void createField() {

        //Bullet.init();

        // Creation of the 3D perspective camera
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(-3.5f+startingPositionX, 3f, -3.5f+startingPositionZ);
        camera.lookAt(ballPositionX, defineFunction(ballPositionX,ballPositionZ), ballPositionZ);
        camera.near = 0.1f;
        camera.far = 400f;

        finalPositionArrowX = ((camera.direction.x)*5)+(ballPositionX);
        finalPositionArrowZ = ((camera.direction.z)*5)+(ballPositionZ);

        generateRandomFlagPosition(20,40,20,40);

        minDistanceArrowFlag = euclideanDistFlag(ballPositionX, ballPositionZ);

        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();

        // Creation of a blue flat field corresponding to the water
        flatField = modelBuilder.createBox(5000, 1.0f, 5000,
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );

        // Models that hold information about the field
        fieldInstance = new ModelInstance[numberOfFields];
        slopeInstance = new ModelInstance[numberOfFields];

        for (int i = 0; i < numberOfFields; i++) {

            modelBuilder.begin();
            buildField();
            slopeModel = modelBuilder.end();

            fieldInstance[i] = new ModelInstance(flatField, 0f, -0.5f, (-i) * 50);
            slopeInstance[i] = new ModelInstance(slopeModel, 0f, 0f, (-i) * 50);
        }

        // Initial values of the arrays that store the position of the ball is the first position of the ball
        positionArrayX[0] = ballPositionX;
        positionArrayZ[0] = ballPositionZ;

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

        instances = new Array<>();

        modelBuilder.begin();
        modelBuilder.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)))
                .sphere(ballSize, ballSize, ballSize, 10, 10);
        ballModel = modelBuilder.end();

        ball = new ModelInstance(ballModel, ballPositionX, (defineFunction(ballPositionX, ballPositionZ))+(ballSize/2), ballPositionZ);

        treePositionX = new float[numberOfTree];
        treePositionZ = new float[numberOfTree];

        for (int i = 0; i < numberOfTree; i++) {

            Random rand = new Random();
            float randomX = 5 + rand.nextFloat() * (45 - 5);
            float randomZ = 5 + rand.nextFloat() * (45 - 5);

            while (((Math.abs(randomX-flagPositionX) < 5) && (Math.abs(randomZ-flagPositionZ) < 5)) || (defineFunction(randomX, randomZ) < 0)) {
                randomX = 5 + rand.nextFloat() * (45 - 5);
                randomZ = 5 + rand.nextFloat() * (45 - 5);
            }

            treePositionX[i] = randomX;
            treePositionZ[i] = randomZ;

            ModelInstance[] treeInstances = drawObject.drawTree(randomX,randomZ);

            instances.add(treeInstances[0], treeInstances[1]);

            /*
            trunkShape = new btCylinderShape(new Vector3(0.25f,3,0.25f));

            trunkObject = new btCollisionObject();
            trunkObject.setCollisionShape(trunkShape);
            trunkObject.setWorldTransform(treeInstances[0].transform);
            obstacleObjects.add(trunkObject);

            branchesShape = new btConeShape(2,3);

            branchesObject = new btCollisionObject();
            branchesObject.setCollisionShape(branchesShape);
            branchesObject.setWorldTransform(treeInstances[1].transform);
            obstacleObjects.add(branchesObject);*/

        }

        /*
        ballShape = new btSphereShape(ballSize);

        ballObject = new btCollisionObject();
        ballObject.setCollisionShape(ballShape);
        ballObject.setWorldTransform(ball.transform);

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        */

        // Adding an environment which is used for the luminosity
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1.f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0f, 1f));
    }

    // Method that defines the function we use to create the slope of the field

    /**
     * Method that defines the function used to create the form of the field
     * @param x first variable of the function
     * @param y second variable of the function
     * @return the estimation of the function at a given x and y point
     */
    public static float defineFunction(double x, double y) {

        //float field0 = (float) (0.5);
        float field1 = (float) (((Math.sin(x) + Math.sin(y))/4)+0.3);
        //float field2 = (float) ((Math.sin(x))/3)+0.2f;
        //float field3 = (float) (((Math.atan(x) + Math.atan(y))/2)+0.5);
        float ripple1 = (float) ((0.4)+Math.sin((0.4)*(Math.pow(x,2)+Math.pow(y,2))/10)+1);

        return field1;
    }

    /**
     * Method used to get the friction at a certain location (not fully working yet)
     * @param x the first coordinate on the field
     * @param z the second coordinate on the field
     * @return the estimation of the friction at a given point on the field
     */
    public static float getFriction(double x, double z) {

        /*
        if (isOnSand((float) x, (float) z)) {
            return 100f;
        }
        */
        return 0.13f;
    }

    /**
     * Method that build the 3D field using 3D vectors
     */
    public static void buildField(){

        gridWidth = 50;
        gridDepth = 50;
        Vector3 pos1,pos2,pos3,pos4;
        Vector3 nor1,nor2,nor3,nor4;
        MeshPartBuilder.VertexInfo v1,v2,v3,v4;

        int count = 0;

        for (int i = 0; i < amountOfSandPit-1; i++) {

            float[] sandPitPosition = generateSandPitPosition();
            sandPitX1[count] = sandPitPosition[0];
            sandPitZ1[count] = sandPitPosition[1];
            sandPitX2[count] = sandPitPosition[2];
            sandPitZ2[count] = sandPitPosition[3];
            count++;
        }

        /*
        System.out.println("sandPitX1 = " + Arrays.toString(sandPitX1));
        System.out.println("sandPitX1 = " + Arrays.toString(sandPitX2));
        System.out.println("sandPitX1 = " + Arrays.toString(sandPitZ1));
        System.out.println("sandPitX1 = " + Arrays.toString(sandPitZ2));
        */


        /*
        System.out.println("sandPitX1 = " + Arrays.toString(sandPitX1));
        System.out.println("sandPitX2 = " + Arrays.toString(sandPitX2));
        System.out.println("sandPitZ1 = " + Arrays.toString(sandPitZ1));
        System.out.println("sandPitZ2 = " + Arrays.toString(sandPitZ2));
        */

        float step = 1f;
        for(float i = 0; i < ((step*(1/step))*gridDepth); i+=step) {
            for (float j = 0; j < ((step*(1/step))*gridWidth); j+=step) {

                pos1 = new Vector3(i, defineFunction(i, j), j);
                pos2 = new Vector3(i, defineFunction(i, j+step), j+step);
                pos3 = new Vector3(i+step, defineFunction(i+step, j+step), j+step);
                pos4 = new Vector3(i+step, defineFunction(i+step, j), j);

                nor1 = new Vector3(-defineFunction(i, 0), step, -defineFunction(0, j));
                nor2 = new Vector3(-defineFunction(i, 0), step, -defineFunction(0, j+step));
                nor3 = new Vector3(-defineFunction(i+step, 0), step, -defineFunction(0, j+step));
                nor4 = new Vector3(-defineFunction(i+step, 0), step, -defineFunction(0, j));

                v1 = new MeshPartBuilder.VertexInfo().setPos(pos1).setNor(nor1).setCol(null).setUV(step/2, 0.0f);
                v2 = new MeshPartBuilder.VertexInfo().setPos(pos2).setNor(nor2).setCol(null).setUV(0.0f, 0.0f);
                v3 = new MeshPartBuilder.VertexInfo().setPos(pos3).setNor(nor3).setCol(null).setUV(0.0f, step/2);
                v4 = new MeshPartBuilder.VertexInfo().setPos(pos4).setNor(nor4).setCol(null).setUV(step/2, step/2);

                float meanX = (pos1.x + pos2.x + pos3.x + pos4.x)/4;
                float meanZ = (pos1.z + pos2.z + pos3.z + pos4.z)/4;

                if (euclideanDistFlag(meanX, meanZ) < winRadius) {
                    fieldColor = Color.PURPLE;
                }
                else {
                    fieldColor = Color.GREEN;
                }

                for (int k = 0; k < amountOfSandPit; k++) {
                    if (pos1.x >= sandPitX1[k] && pos4.x <= sandPitX2[k] && pos1.z >= sandPitZ1[k] && pos2.z <= sandPitZ2[k]) {
                        fieldColor = Color.YELLOW;
                    }
                }

                MeshPartBuilder b = modelBuilder.part("field", GL_TRIANGLES,
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                        new Material(ColorAttribute.createDiffuse(fieldColor)));

                b.rect(v1, v2, v3, v4);
            }
        }
    }

    /**
     * Method that return the euclidean distance between the ball and the flag
     * @param positionX the first coordinate on the field
     * @param positionZ the second coordinate on the field
     * @return the euclidean distance between that given point and the flag
     */
    public static float euclideanDistFlag(float positionX, float positionZ) {

        return (float) Math.sqrt(Math.pow((positionX-flagPositionX), 2) + Math.pow((positionZ-flagPositionZ), 2));
    }

    public static float euclideanDistObstacles(float positionX, float positionZ, int index) {

        float treeX = treePositionX[index];
        float treeZ = treePositionZ[index];

        return (float) Math.sqrt(Math.pow((positionX-treeX), 2) + Math.pow((positionZ-treeZ), 2));
    }

    public static float euclideanDistSensors(float x1, float z1, int index) {

        return (float) Math.sqrt(Math.pow((x1-maxPositionX[index]), 2) + Math.pow((z1-maxPositionZ[index]), 2));
    }

    public static float euclideanDist(float x1, float z1, float x2, float z2) {

        return (float) Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((z1-z2), 2));
    }

    /**
     * Method that checks if the ball is close enough to the flag
     * @param positionX the first coordinate on the field
     * @param positionZ the second coordinate on the field
     * @return true if the ball is close enough to the field, false otherwise
     */
    public static boolean isWin(float positionX, float positionZ) {

        if(euclideanDistFlag(positionX, positionZ) < winRadius) {
            return true;
        }
        return false;
    }

    /**
     * Method that checks if the ball falls into the water
     * @param positionX the first coordinate of the ball
     * @param positionZ the second coordinate of the ball
     * @return true if the ball falls into water, false otherwise
     */
    public static boolean isInWater(float positionX, float positionZ) {

        if(defineFunction(positionX, positionZ) < 0) return true;
        return false;
    }

    /**
     * Method that checks if the ball is still in the field
     * @param positionX the first coordinate of the ball
     * @param positionZ the second coordinate of the ball
     * @return true if the ball go out of the field, false otherwise
     */
    public static boolean outOfField(float positionX, float positionZ) {

        if(positionX > gridDepth- 2*ballSize || positionZ > gridWidth-2*ballSize || positionX < 2*ballSize || positionZ < 2*ballSize) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean isOnSand(float positionX, float positionZ) {

        boolean check = false;
        for (int k = 0; k < amountOfSandPit; k++) {
            for (int l = 0; l < sandPitSize; l++) {
                if (positionX >= sandPitX1[k] && positionX <= sandPitX2[k] && positionZ >= sandPitZ1[k] && positionZ <= sandPitZ2[k]) {
                    check = true;
                }
            }
        }
        return check;
    }

    /**
     * Method used to reset the shot to the previous one
     */
    public void resetBallShot() {

        if (countIndex > 0) {

            positionArrayX[countIndex] = positionArrayX[countIndex - 1];
            positionArrayZ[countIndex] = positionArrayZ[countIndex - 1];

            ballPositionX = positionArrayX[countIndex - 1];
            ballPositionZ = positionArrayZ[countIndex - 1];
            newBallPositionX = 0;
            newBallPositionZ = 0;
        }
    }

    /**
     * Method that makes the ball move to its new position
     */
    public void ballMovement() {

        double ballPositionXround = (double)(Math.round(ballPositionX*100))/10;
        double newBallPositionXround = (double)(Math.round(newBallPositionX*100))/10;
        double ballPositionZround = (double)(Math.round(ballPositionZ*100))/10;
        double newBallPositionZround = (double)(Math.round(newBallPositionZ*100))/10;

        float allowableRange = 0.1f;

        // Condition that checks if the new position of the ball is different from the current one
        // If it is the case, move the ball to that new position
        if (((((ballPositionX < newBallPositionX) && (ballPositionZ < newBallPositionZ)) ||
                ((ballPositionX > newBallPositionX) && (ballPositionZ > newBallPositionZ)) ||
                ((ballPositionX < newBallPositionX) && (ballPositionZ > newBallPositionZ)) ||
                ((ballPositionX > newBallPositionX) && (ballPositionZ < newBallPositionZ))) &&
                ((ballPositionXround != newBallPositionXround) && (ballPositionZround != newBallPositionZround))) &&
                (canTranslateCam)) {

            float scaleFactor = 50;

            if (isOnSand(ballPositionX, ballPositionZ)) {

                newBallPositionX-=ballStepXmean/2;
                newBallPositionZ-=ballStepZmean/2;

                ballPositionX += ballStepXmean/2;
                ballPositionZ += ballStepZmean/2;

                if (gameMode.gameName.equals("Single_Player")) {
                    camera.translate((newBallPositionX - ballPositionX) / (scaleFactor * 2), 0.001f / (scaleFactor * 2), (newBallPositionZ - ballPositionZ) / (scaleFactor * 2));
                }
                else {
                    if (agentPower > 1500) {
                        camera.translate((newBallPositionX - ballPositionX) / (scaleFactor * 3), 0.001f / (scaleFactor * 3), (newBallPositionZ - ballPositionZ) / (scaleFactor * 3));
                    }
                    else {
                        camera.translate((newBallPositionX - ballPositionX) / (scaleFactor * 2), 0.001f / (scaleFactor * 2), (newBallPositionZ - ballPositionZ) / (scaleFactor * 2));
                    }
                }

                sumX += (newBallPositionX - ballPositionX) / (scaleFactor*2);
                sumZ += (newBallPositionZ - ballPositionZ) / (scaleFactor*2);
            }
            else {

                ballPositionX += ballStepXmean;
                ballPositionZ += ballStepZmean;

                camera.translate((newBallPositionX - ballPositionX) / scaleFactor, 0.001f / scaleFactor, (newBallPositionZ - ballPositionZ) / scaleFactor);

                sumX += (newBallPositionX - ballPositionX) / (scaleFactor);
                sumZ += (newBallPositionZ - ballPositionZ) / (scaleFactor);
            }
            camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);

            /*
            if (gameMode.gameName.equals("Single_Player")) {

                camera.translate((newBallPositionX - ballPositionX) / scaleFactor, 0.001f / scaleFactor, (newBallPositionZ - ballPositionZ) / scaleFactor);
                sumX += (newBallPositionX - ballPositionX) / (scaleFactor);
                sumZ += (newBallPositionZ - ballPositionZ) / (scaleFactor);
            }*/

            /*
            else if (BotScreen.getBotName().equals("agent")) {

                float scaleFactor;
                if (agentPower >= 2000) {
                    scaleFactor = 53;
                }
                else {
                    scaleFactor = 50;
                }
                camera.translate((newBallPositionX - ballPositionX) / scaleFactor, 0.001f / scaleFactor, (newBallPositionZ - ballPositionZ) / scaleFactor);
                sumX += (newBallPositionX - ballPositionX) / (scaleFactor);
                sumZ += (newBallPositionZ - ballPositionZ) / (scaleFactor);
            }
            */

            positionArrayX[countIndex] = newBallPositionX;
            positionArrayZ[countIndex] = newBallPositionZ;

            translateX[countIndex - 1] = sumX;
            translateZ[countIndex - 1] = sumZ;

            ballStop = false;
            sensorsReady = false;

        }
        else {
            ballStop = true;
            if (countIndex > 0) {
                ballPositionX = positionArrayX[countIndex];
                ballPositionZ = positionArrayZ[countIndex];
                /*
                System.out.println("ballPositionX = " + ballPositionX);
                System.out.println("ballPositionZ = " + ballPositionZ);
                System.out.println("newBallPositionX = " + newBallPositionX);
                System.out.println("newBallPositionZ = " + newBallPositionZ);
                */
            }
        }
    }

    /**
     * Method used to draw a string on the screen
     * @param message to draw on the screen
     */
    public void displayMessage(String message){

        font.getData().setScale(3);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        layout.setText(font, message);
        fontX = (Game.WIDTH/2) - (layout.width/2);
        fontY = (Game.HEIGHT/2) - (layout.height/2);

        batch.begin();
        font.draw(batch, message, fontX, fontY);
        batch.end();
    }

    /**
     * Method used to draw the flag at a random position in a certain range on the field
     * @param boundX1 smaller bound location X coordinate
     * @param boundX2 higher bound location X coordinate
     * @param boundZ1 smaller bound location Z coordinate
     * @param boundZ2 higher bound location Z coordinate
     */
    public void generateRandomFlagPosition(int boundX1, int boundX2, int boundZ1, int boundZ2) {

        Random rand = new Random();
        int randomX = rand.nextInt((boundX2-boundX1)+1) + boundX1;
        int randomZ = rand.nextInt((boundZ2-boundZ1)+1) + boundZ1;

        boolean checkPosition = false;

        while (!checkPosition) {
            randomX = rand.nextInt((boundX2-boundX1)+1) + boundX1;
            randomZ = rand.nextInt((boundZ2-boundZ1)+1) + boundZ1;
            if (defineFunction(randomX, randomZ) > 0.4f && (Math.abs(randomX-randomZ) > 3)) {
                checkPosition = true;
            }
        }

        flagPositionX = randomX;
        flagPositionZ = randomZ;

        System.out.println("flag x: " + randomX);
        System.out.println("flag z: " + randomZ);
    }

    /*
    public boolean checkCollision(btCollisionObject ballObject, btCollisionObject obstacleObject) {

        CollisionObjectWrapper co0 = new CollisionObjectWrapper(ballObject);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(obstacleObject);

        btCollisionAlgorithmConstructionInfo ci = new btCollisionAlgorithmConstructionInfo();
        ci.setDispatcher1(dispatcher);
        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null, ci, co0.wrapper, co1.wrapper, false);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

        algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);

        return result.getPersistentManifold().getNumContacts() > 0;
    }*/

    public boolean checkCollision(int index) {

        if (euclideanDistObstacles(ballPositionX, ballPositionZ, index) < 0.4f) {
            return true;
        }
        else {
            return false;
        }
    }

    public void drawSensor(double angle, float x1, float x2, float z1, float z2, int index, int num) {

        boolean collision = false;

        float radian = (float) Math.toRadians(angle);

        float rotationX1 = (float) ((x1) * Math.cos(radian) - (z1) * Math.sin(radian));
        float rotationZ1 = (float) ((z1) * Math.cos(radian) + (x1) * Math.sin(radian));

        float rotationX2 = (float) ((x1 + x2) * Math.cos(radian) - (z1 + z2) * Math.sin(radian));
        float rotationZ2 = (float) ((z1 + z2) * Math.cos(radian) + (x1 + x2) * Math.sin(radian));

        modelBuilder.begin();
        meshPartBuilder = modelBuilder.part("line", 1, 3, new Material());
        meshPartBuilder.setColor(Color.WHITE);
        meshPartBuilder.line(ballPositionX + rotationX1, (defineFunction(ballPositionX + rotationX1, ballPositionZ + rotationZ1) + 0.1f), ballPositionZ + rotationZ1,
                ballPositionX + rotationX2, (defineFunction(ballPositionX + rotationX2, ballPositionZ + rotationZ2) + 0.1f), ballPositionZ + rotationZ2);
        Model lineModel = modelBuilder.end();
        ModelInstance lineInstance = new ModelInstance(lineModel);

        for (int j = 0; j < numberOfTree; j++) {
            if (euclideanDistObstacles(ballPositionX + rotationX2, ballPositionZ + rotationZ2, j) < 0.5f) {
                sensorsSize[num] = (index + 1)/(numberOfLinesSensors/10f);
                collision = true;
            }
        }

        if (defineFunction(ballPositionX + rotationX2, ballPositionZ + rotationZ2) < 0.05f) {
            sensorsSize[num] = (index + 1)/(numberOfLinesSensors/10f);
            collision = true;
        }

        if (outOfField(ballPositionX + rotationX2, ballPositionZ + rotationZ2)) {
            sensorsSize[num] = (index + 1)/(numberOfLinesSensors/10f);
            collision = true;
        }

        if (euclideanDistFlag(ballPositionX + rotationX2, ballPositionZ + rotationZ2) < 0.001f) {
            canHitFlag[num] = true;
            collision = true;
        }
        else {
            canHitFlag[num] = false;
        }

        if (isOnSand(ballPositionX + rotationX2, ballPositionZ + rotationZ2)) {
            isSensorOnSand[num] = true;
        }

        if (num == 0) {
            if (collision) {
                isDetectorCollision1 = true;
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            } else {
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            }
        }
        if (num == 1) {
            if (collision) {
                isDetectorCollision2 = true;
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            } else {
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            }
        }
        if (num == 2) {
            if (collision) {
                isDetectorCollision3 = true;
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            } else {
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            }
        }
        if (num == 3) {
            if (collision) {
                isDetectorCollision4 = true;
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            } else {
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            }
        }
        if (num == 4) {
            if (collision) {
                isDetectorCollision5 = true;
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            } else {
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            }
        }
        if (num == 6) {
            if (collision) {
                isDetectorCollision7 = true;
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            } else {
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            }
        }
        if (num == 7) {
            if (collision) {
                isDetectorCollision8 = true;
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            } else {
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            }
        }
        if (num == 8) {
            if (collision) {
                isDetectorCollision9 = true;
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            } else {
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            }
        }
        if (num == 9) {
            if (collision) {
                isDetectorCollision10 = true;
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            } else {
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            }
        }
        if (num == 10) {
            if (collision) {
                isDetectorCollision11 = true;
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            } else {
                maxPositionAgentX[num] = ballPositionX+rotationX1;
                maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
            }
        }

        if (!collision) {
            sensors.add(lineInstance);
        }

        /*
        if (collision) {
            modelBuilder.begin();
            meshPartBuilder = modelBuilder.part("line", 1, 3, new Material());
            meshPartBuilder.setColor(Color.WHITE);
            meshPartBuilder.line(ballPositionX + rotationX1, (defineFunction(ballPositionX + rotationX1, ballPositionZ + rotationZ1) + 0.1f), ballPositionZ + rotationZ1,
                    ballPositionX + rotationX2, (defineFunction(ballPositionX + rotationX2, ballPositionZ + rotationZ2) + 0.1f), ballPositionZ + rotationZ2);
            Model lineModelExtra = modelBuilder.end();
            ModelInstance lineInstanceExtra = new ModelInstance(lineModelExtra);

            sensors.add(lineInstanceExtra);
        }
        */

        sensorsSize[num] = (index + 1)/(numberOfLinesSensors/10f);


    }

    public static float evaluatePowerRK4(float x, float z, float x1, float z1, float vx, float vz) {

        Rungekuttasolver RK4 = new Rungekuttasolver();

        boolean check = false;
        float allowableRange = 0.3f;
        float increment = 2f;
        float i = increment/2;
        float power = 1;
        float newX = 0;
        float newZ = 0;
        int count = 0;
        int type = 0;

        while (!check) {

            RK4.setValues(x, z, vx*i, vz*i);
            RK4.RK4();

            newX = (float) RK4.getX();
            newZ = (float) RK4.getY();


            /*
            System.out.println("x1 = " + x1);
            System.out.println("z1 = " + z1);
            System.out.println("newX = " + newX);
            System.out.println("newZ = " + newZ);
            System.out.println("type = " + type);
            System.out.println();
            */



            if (count < 1) {
                if (x1 > newX && z1 > newZ) {
                    type = 1;
                }
                if (x1 > newX && z1 < newZ) {
                    type = 2;
                }
                if (x1 < newX && z1 > newZ) {
                    type = 3;
                }
                if (x1 < newX && z1 < newZ) {
                    type = 4;
                }
            }
            count++;


            /*
            if ((newX < x1+allowableRange && newX > x1-allowableRange) && (newZ < z1+allowableRange && newZ > z1-allowableRange)) {
                check = true;
                power = i;
            }
            i+=increment;
            */



            if (type==1) {
                if (newX > x1-(allowableRange) && newZ > z1-(allowableRange)) {
                    power = i-increment;
                    check = true;
                }
            }
            if (type==2) {
                if (newX > x1-(allowableRange) && newZ < z1+(allowableRange)) {
                    power = i-increment;
                    check = true;
                }
            }
            if (type==3) {
                if (newX < x1+(allowableRange) && newZ > z-(allowableRange)) {
                    power = i-increment;
                    check = true;
                }
            }
            if (type==4) {
                if (newX < x1+(allowableRange) && newZ < z1+(allowableRange)) {
                    power = i-increment;
                    check = true;
                }
            }

            i+=increment;

            //System.out.println("stuck");
        }

        if (outOfField(newX, newZ) || isInWater(newX, newZ)) {
            power-=((30/100f)*power);
        }

        return power;
    }

    public static float[] generateSandPitPosition() {

        Random rand = new Random();
        int randomX1 = rand.nextInt((45-5) + 1)+5;
        int randomZ1 = rand.nextInt((45-5) + 1)+5;
        int randomX2 = randomX1+sandPitSize;
        int randomZ2 = randomZ1+sandPitSize;

        boolean checkPosition = false;

        if ((euclideanDistFlag(randomX1, randomZ1) <= sandPitSize) || (euclideanDistFlag(randomX1, randomZ2) <= sandPitSize) ||
                (euclideanDistFlag(randomX2, randomZ1) <= sandPitSize) || (euclideanDistFlag(randomX2, randomZ2) <= sandPitSize)) {
            while (!checkPosition) {
                randomX1 = rand.nextInt((45 - 5) + 1) + 5;
                randomZ1 = rand.nextInt((45 - 5) + 1) + 5;
                randomX2 = randomX1+sandPitSize;
                randomZ2 = randomZ1+sandPitSize;

                if ((euclideanDistFlag(randomX1, randomZ1) > sandPitSize) || (euclideanDistFlag(randomX1, randomZ2) > sandPitSize) ||
                        (euclideanDistFlag(randomX2, randomZ1) > sandPitSize) || (euclideanDistFlag(randomX2, randomZ2) > sandPitSize)) {
                    checkPosition = true;
                }
            }
        }

        return new float[]{randomX1, randomZ1, randomX2, randomZ2};
    }

    /**
     * Render all the elements of the field
     * @param delta time between the last and the current frame
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();

        modelBatch.begin(camera);

        // Render the instance of the field with the given environment
        for (int i = 0; i < numberOfFields; i++) {
            modelBatch.render(fieldInstance[i], environment);
            modelBatch.render(slopeInstance[i], environment);
        }

        modelBatch.render(ball, environment);
        modelBatch.render(instances, environment);

        //ballObject.setWorldTransform(ball.transform);

        ball = new ModelInstance(ballModel, ballPositionX, (defineFunction(ballPositionX, ballPositionZ))+(ballSize/2), ballPositionZ);

        if (ballStop) {

            if (checkForSensors) {
                sensors.clear();

                float maxVx = (flagPositionX - ballPositionX);
                float maxVz = (flagPositionZ - ballPositionZ);

                float stepX = 0;
                float stepZ = 0;
                float stepX1 = maxVx / numberOfLinesSensors;
                float stepZ1 = maxVz / numberOfLinesSensors;

                stepForMaxX = 0;
                stepForMaxZ = 0;

                int i = 0;
                while (i < numberOfLinesSensors) {
                    if (!isDetectorCollision6) {

                        modelBuilder.begin();
                        meshPartBuilder = modelBuilder.part("line", 1, 3, new Material());
                        meshPartBuilder.setColor(Color.RED);
                        meshPartBuilder.line((ballPositionX + (stepX)), defineFunction((ballPositionX + (stepX)), (ballPositionZ + (stepZ))) + 0.1f, (ballPositionZ + (stepZ)),
                                ballPositionX + stepX + stepX1, defineFunction(ballPositionX + stepX + (stepX1), ballPositionZ + stepZ + (stepZ1)) + 0.1f, ballPositionZ + stepZ + (stepZ1));
                        Model lineModel = modelBuilder.end();
                        ModelInstance lineInstance = new ModelInstance(lineModel);

                        for (int j = 0; j < numberOfTree; j++) {
                            if (euclideanDistObstacles(ballPositionX + stepX + stepX1, ballPositionZ + stepZ + stepZ1, j) < 0.5f) {
                                sensorsSize[5] = (i + 1) / (numberOfLinesSensors / 10f);
                                isDetectorCollision6 = true;
                            }
                        }

                        if (defineFunction(ballPositionX + stepX + stepX1, ballPositionZ + stepZ + stepZ1) < 0.05f) {
                            sensorsSize[5] = (i + 1) / (numberOfLinesSensors / 10f);
                            isDetectorCollision6 = true;
                        }

                        if (outOfField(ballPositionX + stepX + stepX1, ballPositionZ + stepZ + stepZ1)) {
                            sensorsSize[5] = (i + 1) / (numberOfLinesSensors / 10f);
                            isDetectorCollision6 = true;
                        }

                        if (euclideanDistFlag(ballPositionX + stepX + stepX1, ballPositionZ + stepZ + stepZ1) < 0.001f) {
                            canHitFlag[5] = true;
                            isDetectorCollision6 = true;
                        } else {
                            canHitFlag[5] = false;
                        }

                        if (isOnSand(ballPositionX + stepX + stepX1, ballPositionZ + stepZ + stepZ1)) {
                            isSensorOnSand[5] = true;
                        }

                        if (!isDetectorCollision6) {
                            sensors.add(lineInstance);
                        }
                        /*
                        if (isDetectorCollision6) {

                            modelBuilder.begin();
                            meshPartBuilder = modelBuilder.part("line", 1, 3, new Material());
                            meshPartBuilder.setColor(Color.RED);
                            meshPartBuilder.line((ballPositionX + (stepX)), defineFunction((ballPositionX + (stepX)), (ballPositionZ + (stepZ))) + 0.1f, (ballPositionZ + (stepZ)),
                                    ballPositionX + stepX + stepX1, defineFunction(ballPositionX + stepX + (stepX1), ballPositionZ + stepZ + (stepZ1)) + 0.1f, ballPositionZ + stepZ + (stepZ1));
                            Model lineModelExtra = modelBuilder.end();
                            ModelInstance lineInstanceExtra = new ModelInstance(lineModelExtra);

                            sensors.add(lineInstanceExtra);
                        }
                        */

                        sensorsSize[5] = (i + 1) / (numberOfLinesSensors / 10f);

                        maxPositionAgentX[5] = ballPositionX + stepX;
                        maxPositionAgentZ[5] = ballPositionZ + stepZ;
                    }

                    if (!isDetectorCollision1) {
                        drawSensor(-135, stepX, stepX1, stepZ, stepZ1, i, 0);
                    }
                    if (!isDetectorCollision2) {
                        drawSensor(-90, stepX, stepX1, stepZ, stepZ1, i, 1);
                    }
                    if (!isDetectorCollision3) {
                        drawSensor(-67.5, stepX, stepX1, stepZ, stepZ1, i, 2);
                    }
                    if (!isDetectorCollision4) {
                        drawSensor(-45, stepX, stepX1, stepZ, stepZ1, i, 3);
                    }
                    if (!isDetectorCollision5) {
                        drawSensor(-22.5, stepX, stepX1, stepZ, stepZ1, i, 4);
                    }
                    if (!isDetectorCollision7) {
                        drawSensor(22.5, stepX, stepX1, stepZ, stepZ1, i, 6);
                    }
                    if (!isDetectorCollision8) {
                        drawSensor(45, stepX, stepX1, stepZ, stepZ1, i, 7);
                    }
                    if (!isDetectorCollision9) {
                        drawSensor(67.5, stepX, stepX1, stepZ, stepZ1, i, 8);
                    }
                    if (!isDetectorCollision10) {
                        drawSensor(90, stepX, stepX1, stepZ, stepZ1, i, 9);
                    }
                    if (!isDetectorCollision11) {
                        drawSensor(135, stepX, stepX1, stepZ, stepZ1, i, 10);
                    }

                    // Print the resulting size of each sensors
                    // System.out.println("sensorsSize = " + Arrays.toString(sensorsSize));


                    saveStepX[i] = stepX+stepX1;
                    saveStepZ[i] = stepZ+stepZ1;

                    stepX += stepX1;
                    stepZ += stepZ1;

                    i++;
                }


                //System.out.println("maxPositionX = " + maxPositionX[5]);

                /*
                for (int j = 0; j < sensorsSize.length; j++) {

                    float angle = 0;

                    if (j==0) {
                        angle = -135;
                    }
                    if (j==1) {
                        angle = -90;
                    }
                    if (j==2) {
                        angle = -67.5f;
                    }
                    if (j==3) {
                        angle = -45;
                    }
                    if (j==4) {
                        angle = -22.5f;
                    }
                    if (j==5) {
                        angle = 0;
                    }
                    if (j==6) {
                        angle = 22.5f;
                    }
                    if (j==7) {
                        angle = 45;
                    }
                    if (j==8) {
                        angle = 67.5f;
                    }
                    if (j==9) {
                        angle = 90;
                    }
                    if (j==10) {
                        angle = 135;
                    }

                    float radian = (float) Math.toRadians(angle);

                    float x = (float) ((maxPositionX[5]) * Math.cos(radian) - (maxPositionZ[5]) * Math.sin(radian));
                    float z = (float) ((maxPositionZ[5]) * Math.cos(radian) + (maxPositionX[5]) * Math.sin(radian));

                    maxPositionX[j] = ballPositionX+x;
                    maxPositionZ[j] = ballPositionZ+z;
                }
                */

                //System.out.println("maxPositionX = " + Arrays.toString(maxPositionX));


                //stepForMaxX = stepX + stepX1;
                //stepForMaxZ = stepZ + stepZ1;

                //for (int j = 0; j < 11; j++) {

                /*
                int step = 10;
                float stepSensorX = (stepForMaxX - ballPositionX)/step;
                float stepSensorZ = (stepForMaxZ - ballPositionZ)/step;
                for (int k = 0; k < step; k++) {
                   //int stepIndex = ((j+1)*10)-(k+1);
                    int stepIndex = 50+k;
                    //int stepIndex = 59-k;
                    //stepPositionX[stepIndex] = (stepForMaxX) - ((k)*stepSensorX);
                    //stepPositionZ[stepIndex] = (stepForMaxZ) - ((k)*stepSensorZ);
                    stepPositionX[stepIndex] = ballPositionX + ((k+1)*stepSensorX);
                    stepPositionZ[stepIndex] = ballPositionZ + ((k+1)*stepSensorZ);
                }
                */

                /*
                System.out.println("stepPositionX = " + Arrays.toString(stepPositionX));
                System.out.println("stepPositionZ = " + Arrays.toString(stepPositionZ));
                System.out.println();
                */

                for (int j = 0; j < sensorsSize.length; j++) {
                    for (int k = 0; k < 10; k++) {

                        int stepIndex1 = ((j*10)+(k));
                        int stepIndex2 = ((k+1)*5)-1;

                        float angle = 0;

                        if (j==0) {
                            angle = -135;
                        }
                        if (j==1) {
                            angle = -90;
                        }
                        if (j==2) {
                            angle = -67.5f;
                        }
                        if (j==3) {
                            angle = -45;
                        }
                        if (j==4) {
                            angle = -22.5f;
                        }
                        if (j==5) {
                            angle = 0;
                        }
                        if (j==6) {
                            angle = 22.5f;
                        }
                        if (j==7) {
                            angle = 45;
                        }
                        if (j==8) {
                            angle = 67.5f;
                        }
                        if (j==9) {
                            angle = 90;
                        }
                        if (j==10) {
                            angle = 135;
                        }

                        float radian = (float) Math.toRadians(angle);

                        float x = (float) ((saveStepX[stepIndex2]) * Math.cos(radian) - (saveStepZ[stepIndex2]) * Math.sin(radian));
                        float z = (float) ((saveStepZ[stepIndex2]) * Math.cos(radian) + (saveStepX[stepIndex2]) * Math.sin(radian));

                        stepPositionX[stepIndex1] = ballPositionX+x;
                        stepPositionZ[stepIndex1] = ballPositionZ+z;
                    }
                }

                for (int j = 0; j < sensorsSize.length; j++) {

                    int stepIndex = (((j+1)*10)-1);

                    maxPositionX[j] = stepPositionX[stepIndex];
                    maxPositionZ[j] = stepPositionZ[stepIndex];
                }



                //}
                //System.out.println("stepPositionX = " + Arrays.toString(stepPositionX));
                //System.out.println("stepPositionZ = " + Arrays.toString(stepPositionZ));

                //System.out.println("isSensorOnSand = " + Arrays.toString(isSensorOnSand));

                isDetectorCollision1 = false;
                isDetectorCollision2 = false;
                isDetectorCollision3 = false;
                isDetectorCollision4 = false;
                isDetectorCollision5 = false;
                isDetectorCollision6 = false;
                isDetectorCollision7 = false;
                isDetectorCollision8 = false;
                isDetectorCollision9 = false;
                isDetectorCollision10 = false;
                isDetectorCollision11 = false;

                for (int j = 0; j < sensorsSize.length; j++) {
                    minEuclideanDist[j] = euclideanDistSensors(0, 0, j);
                }

                botTimer1 = 0;
                botTimer2  = 0;
                sensorsReady = true;
                checkForSensors = false;
            }
        }

        if (gameMode.gameName.equals("Single_Player")) {
            if (!ballStop) {
                checkForSensors = true;
            }
            else {
                modelBatch.render(sensors, environment);
                checkSolver = false;
            }
        }




/*
        for (int i = 0; i < maxPositionZ.length; i++) {
            Model testII = modelBuilder.createBox(0.2f, 3, 0.2f,
                    new Material(ColorAttribute.createDiffuse(Color.YELLOW)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
            );
            ModelInstance testIII = new ModelInstance(testII, maxPositionX[i], defineFunction(maxPositionX[i], maxPositionZ[i]), maxPositionZ[i]);

            instancesTest.add(testIII);
        }

        if (ballStop) {
            modelBatch.render(instancesTest, environment);
        }
        else {
            instancesTest.clear();
        }



        Array<ModelInstance> testArray = new Array<>();

        for (int i = 0; i < stepPositionX.length; i++) {
            Model test = modelBuilder.createBox(0.05f, 1f, 0.05f,
                    new Material(ColorAttribute.createDiffuse(Color.RED)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
            );
            ModelInstance testI = new ModelInstance(test, stepPositionX[i], defineFunction(stepPositionX[i],  stepPositionZ[i]), stepPositionZ[i]);

            testArray.add(testI);
        }

        if (ballStop) {
            modelBatch.render(testArray, environment);
        }
        else {
            testArray.clear();
        }
        */




        modelBatch.render(flag1Instance, environment);
        modelBatch.render(flag2Instance, environment);

        modelBatch.end();

        // Creation of the power shot indicator
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        int scale = 40;

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(20, 20, 13, (float) (getMAX_SPEED()*(scale))+4);
        shapeRenderer.setColor(Color.RED);
        if ((getShot_Power()*(scale)) < getMAX_SPEED()*(scale)) {
            shapeRenderer.rect(22, 22, 8, (float) ((getShot_Power()) * (scale)));
        }

        shapeRenderer.end();

        if (!checkCollisionMessage) {
            ballMovement();
        }

        // Condition used when the ball is out of the field
        if (outOfField(ballPositionX, ballPositionZ)) {

            if (timer < period) {
                displayMessage("Ball went out of the field");
                checkCollisionMessage = true;
                timer += Gdx.graphics.getDeltaTime();
            }
            else {
                checkCollisionMessage = false;
                timer = 0;
                camera.translate(-(sumX), (float) (-0.001 / 3), -(sumZ));
                canTranslateCam = false;
                canReset = false;
                resetBallShot();
                camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
            }
        }

        // Condition used to reset the ball position when the ball falls into water
        if (isInWater(ballPositionX, ballPositionZ)) {

            if (timer < period) {
                displayMessage("Ball fell in water");
                checkCollisionMessage = true;
                timer += Gdx.graphics.getDeltaTime();
            }
            else {
                checkCollisionMessage = false;
                timer = 0;
                camera.translate(-(sumX), (float) (-0.001 / 3), -(sumZ));
                canTranslateCam = false;
                canReset = false;
                // Call of the method that reset the ball to the previous place
                resetBallShot();
                camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
            }
        }
        // Condition used to check if the ball is closed enough to the flag
        if (isWin(ballPositionX, ballPositionZ) && ballStop) {

            if (timer < period) {
                displayMessage("Win");
                checkCollisionMessage = true;
                timer+=Gdx.graphics.getDeltaTime();
            }
            else {
                Gdx.app.exit();
            }
        }

        // Loop that checks the collision between each obstacle and the ball
        for (int i = 0; i < numberOfTree; i++) {
            if (checkCollision(i)) {
                if (timer < period) {
                    displayMessage("Ball collide with a tree");
                    checkCollisionMessage = true;
                    timer += Gdx.graphics.getDeltaTime();
                }
                else {
                    checkCollisionMessage = false;
                    timer = 0;
                    camera.translate(-(sumX), (float) (-0.001 / 3), -(sumZ));
                    canTranslateCam = false;
                    canReset = false;
                    // Call of the method that reset the ball to the previous place
                    resetBallShot();
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
                }
            }
        }

        // Condition that only let the user controls when the Single Player mode is selected
        if (gameMode.gameName.equals("Single_Player")) {

            finalPositionArrowX = ((camera.direction.x)*5)+(ballPositionX);
            finalPositionArrowZ = ((camera.direction.z)*5)+(ballPositionZ);

            // Creation of the directive arrow
            arrow = modelBuilder.createArrow(ballPositionX, defineFunction(ballPositionX, ballPositionZ)+1f, ballPositionZ,
                    ((camera.direction.x)*5)+(ballPositionX), defineFunction(ballPositionX, ballPositionZ)+2f,
                    ((camera.direction.z)*5)+(ballPositionZ), 0.1f, 0.1f, 10,
                    GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.RED)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            arrowInstance = new ModelInstance(arrow);
            modelBatch.render(arrowInstance, environment);


            // Call of the class input handler that contains the majority of the user controls (only for single player)
            if (ballStop) {
                handler.checkForInput();
                handler.checkForSpaceInput();
            }
        }
        else {
            if (BotScreen.getBotName().equals("agent")) {
                if (!checkCollisionMessage) {
                    handler.checkForAgentBot();
                }
                /*
                if (ballStop) {
                    if (countIterationBot < 1) {
                        agent = new AgentTest();
                    }
                    countIterationBot++;
                    //System.out.println("agent = " + agent);
                    agent.startAgent();
                    if (AgentTest.isBotReady) {
                        agent.startBot();
                    }
                } else {
                    agent.resetValues();
                }
                */
            }
            if (BotScreen.getBotName().equals("aStar")) {
                AStar bot = new AStar(this);
                bot.findRoute();
            }
        }

        if (name.equals("NN")) {
            if (finishAgent) {
                Gdx.app.exit();
            }
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

        /*
        trunkObject.dispose();
        trunkShape.dispose();

        branchesObject.dispose();
        branchesShape.dispose();

        ballObject.dispose();
        ballShape.dispose();

        dispatcher.dispose();
        collisionConfig.dispose();
        */

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

    public static double getMAX_SPEED(){
        return MAX_SPEED;
    }

    public double getSTARTING_SHOT_POWER(){
        return STARTING_SHOT_POWER;
    }

    public ModelInstance getBall() {
        return ball;
    }

    public float getBallPositionX() {
        return ballPositionX;
    }

    public float getBallPositionZ() {
        return ballPositionZ;
    }

    public static float getFlagPositionX() {
        return flagPositionX;
    }

    public static float getFlagPositionZ() {
        return flagPositionZ;
    }

    public static float getWinRadius() {
        return winRadius;
    }

    public boolean getCanReset(){
        return canReset;
    }

    public boolean getTrackShot(){
        return trackShot;
    }

    public float[] getTranslateX(){
        return translateX;
    }

    public float[] getTranslateZ(){
        return translateZ;
    }

    public void setTrackShot(boolean value){
        trackShot = value;
    }

    public void setCanTranslateCam(boolean value){
        canTranslateCam = value;
    }

    public int getCountIndex(){
        return countIndex;
    }

    public static ModelBuilder getModelBuilder() {
        return modelBuilder;
    }

    public static ArrayList<float[]> getSensorsOutput() {
        return sensorsOutput;
    }

    public static ArrayList<float[]> getSensorsData() {
        return sensorsData;
    }

    public class InputHandler {

        public void checkForAgentBot() {

            finalPositionArrowX = ((camera.direction.x) * 5) + (ballPositionX);
            finalPositionArrowZ = ((camera.direction.z) * 5) + (ballPositionZ);

            // Creation of the directive arrow
            arrow = getModelBuilder().createArrow(ballPositionX, defineFunction(ballPositionX, ballPositionZ) + 1f, ballPositionZ,
                    ((camera.direction.x) * 5) + (ballPositionX), defineFunction(ballPositionX, ballPositionZ) + 2f,
                    ((camera.direction.z) * 5) + (ballPositionZ), 0.1f, 0.1f, 10,
                    GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.RED)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            arrowInstance = new ModelInstance(arrow);
            modelBatch.render(arrowInstance, environment);


            if (!findFlag && ballStop) {

                canTranslateCam = false;

                if (bestSensor == 5) {
                    findFlag = true;
                }

                if (countForFlag < 1) {
                    minDistanceArrowFlag = euclideanDistFlag(ballPositionX, ballPositionZ);
                }

                countForFlag++;

                //System.out.println("final arrow x = " + euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ));
                //System.out.println("minDistanceArrowFlag = " + minDistanceArrowFlag);

                if (euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ) < minDistanceArrowFlag) {

                    //System.out.println("ok");

                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                            vector2 = new Vector3(0f, 1f, 0f), 0.5f);
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);

                    minDistanceArrowFlag = euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ);

                    camDirectionFlagX = camera.direction.x;
                    camDirectionFlagY = camera.direction.y;
                    camDirectionFlagZ = camera.direction.z;

                    camUpFlagX = camera.up.x;
                    camUpFlagY = camera.up.y;
                    camUpFlagZ = camera.up.z;

                    camPositionFlagX = camera.position.x;
                    camPositionFlagY = camera.position.y;
                    camPositionFlagZ = camera.position.z;

                    sensorsAngleX[5] = camDirectionFlagX;
                    sensorsAngleY[5] = camDirectionFlagY;
                    sensorsAngleZ[5] = camDirectionFlagZ;

                    sensorUpX[5] = camUpFlagX;
                    sensorUpY[5] = camUpFlagY;
                    sensorUpZ[5] = camUpFlagZ;

                    sensorPositionX[5] = camPositionFlagX;
                    sensorPositionY[5] = camPositionFlagY;
                    sensorPositionZ[5] = camPositionFlagZ;


                    if (count > 1) {
                        check = true;
                    }

                    count++;
                }
                else {

                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                            vector2 = new Vector3(0f, 1f, 0f), 4f);
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);

                    if (check) {
                        findFlag = true;
                    }
                }

                countForBot = 0;
            }
            else {

                if (countForBot < 1) {

                    /*
                    camera.direction.x = camDirectionFlagX;
                    camera.direction.y = camDirectionFlagY;
                    camera.direction.z = camDirectionFlagZ;

                    camera.up.x = camUpFlagX;
                    camera.up.y = camUpFlagY;
                    camera.up.z = camUpFlagZ;

                    camera.position.x = camPositionFlagX;
                    camera.position.y = camPositionFlagY;
                    camera.position.z = camPositionFlagZ;
                    */

                    checkForSensors = true;
                    countForBot++;
                }
                else {
                    checkForSensors = false;
                }
            }

            if (sensorsReady) {

                if (countForSensors < 1) {
                    for (int j = 0; j < sensorsSize.length; j++) {
                        minEuclideanDist[j] = 100;
                    }
                }

                countForSensors++;

                modelBatch.render(sensors, environment);

                float timePeriod1 = 2f;
                float camVelocity = 8f;
                float error = 0.1f;

                if (botTimer1 < timePeriod1) {

                    for (int i = 0; i < sensorsSize.length; i++) {
                        if (euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i) < minEuclideanDist[i]) {

                            minEuclideanDist[i] = euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i)+error;
                        }
                    }

                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                            vector2 = new Vector3(0f, 1f, 0f), -camVelocity);
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);

                    botTimer1+= Gdx.graphics.getDeltaTime();
                }


                float camVelocity1 = 3f;
                float camVelocity2 = 0.5f;
                if (botTimer1 > timePeriod1 && !botReady) {

                    if (counter < 1) {

                        //System.out.println("camera ok");

                        camera.direction.x = sensorsAngleX[5];
                        camera.direction.y = sensorsAngleY[5];
                        camera.direction.z = sensorsAngleZ[5];

                        camera.up.x = sensorUpX[5];
                        camera.up.y = sensorUpY[5];
                        camera.up.z = sensorUpZ[5];

                        camera.position.x = sensorPositionX[5];
                        camera.position.y = sensorPositionY[5];
                        camera.position.z = sensorPositionZ[5];

                        checkCamera = true;
                    }
                    if (checkCamera) {
                        counter++;
                    }

                    //for (int i = 0; i < sensorsSize.length; i++) {

                    //System.out.println("checkCamera = " + checkCamera);

                    if (checkCamera) {

                        if (euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, countForSensorsReady) < minEuclideanDist[countForSensorsReady]) {

                            minEuclideanDist[countForSensorsReady] = euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, countForSensorsReady);

                            if (countForSensorsReady == 0) {
                                if (countForSensor0Ready < 5) {
                                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                                            vector2 = new Vector3(0f, 1f, 0f), camVelocity2);
                                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
                                }
                                else {
                                    checkForSensorsStep[countForSensorsReady] = true;
                                }
                                countForSensor0Ready++;
                            } else {
                                camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                                        vector2 = new Vector3(0f, 1f, 0f), -camVelocity2);
                                camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
                                checkForSensorsStep[countForSensorsReady] = true;
                            }

                            sensorsAngleX[countForSensorsReady] = camera.direction.x;
                            sensorsAngleY[countForSensorsReady] = camera.direction.y;
                            sensorsAngleZ[countForSensorsReady] = camera.direction.z;

                            sensorUpX[countForSensorsReady] = camera.up.x;
                            sensorUpY[countForSensorsReady] = camera.up.y;
                            sensorUpZ[countForSensorsReady] = camera.up.z;

                            sensorPositionX[countForSensorsReady] = camera.position.x;
                            sensorPositionY[countForSensorsReady] = camera.position.y;
                            sensorPositionZ[countForSensorsReady] = camera.position.z;
                        } else {
                            if (checkForSensorsStep[countForSensorsReady]) {
                                countForSensorsReady++;
                            }
                            if (countForSensorsReady == 0) {
                                camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                                        vector2 = new Vector3(0f, 1f, 0f), camVelocity1);
                                camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
                            } else {
                                camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                                        vector2 = new Vector3(0f, 1f, 0f), -camVelocity1);
                                camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
                            }
                        }
                        if (countForSensorsReady == 11) {
                            botReady = true;
                        }
                    }

                    //System.out.println("countForSensorsReady = " + countForSensorsReady);

                    //}

                }
            }

            /*
            if (!findFlag && ballStop) {

                canTranslateCam = false;

                if (bestSensor == 5) {
                    findFlag = true;
                }

                if (countForFlag < 1) {
                    minDistanceArrowFlag = euclideanDistFlag(ballPositionX, ballPositionZ);
                }

                countForFlag++;

                float timePeriod = 2f;
                float camVelocity = 5f;

                if (botTimer1 < timePeriod) {

                    if (euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ) < minDistanceArrowFlag) {

                        minDistanceArrowFlag = euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ);

                        camDirectionFlagX = camera.direction.x;
                        camDirectionFlagY = camera.direction.y;
                        camDirectionFlagZ = camera.direction.z;

                        camUpFlagX = camera.up.x;
                        camUpFlagY = camera.up.y;
                        camUpFlagZ = camera.up.z;

                        camPositionFlagX = camera.position.x;
                        camPositionFlagY = camera.position.y;
                        camPositionFlagZ = camera.position.z;

                        sensorsAngleX[5] = camDirectionFlagX;
                        sensorsAngleY[5] = camDirectionFlagY;
                        sensorsAngleZ[5] = camDirectionFlagZ;

                        sensorUpX[5] = camUpFlagX;
                        sensorUpY[5] = camUpFlagY;
                        sensorUpZ[5] = camUpFlagZ;

                        sensorPositionX[5] = camDirectionFlagX;
                        sensorPositionY[5] = camDirectionFlagY;
                        sensorPositionZ[5] = camDirectionFlagZ;
                    }

                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                            vector2 = new Vector3(0f, 1f, 0f), camVelocity);
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);

                    botTimer1 += Gdx.graphics.getDeltaTime();
                }

                if (botTimer1 > timePeriod && botTimer2 < timePeriod) {

                    if (euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ) < minDistanceArrowFlag) {

                        minDistanceArrowFlag = euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ);

                        camDirectionFlagX = camera.direction.x;
                        camDirectionFlagY = camera.direction.y;
                        camDirectionFlagZ = camera.direction.z;

                        camUpFlagX = camera.up.x;
                        camUpFlagY = camera.up.y;
                        camUpFlagZ = camera.up.z;

                        camPositionFlagX = camera.position.x;
                        camPositionFlagY = camera.position.y;
                        camPositionFlagZ = camera.position.z;

                        sensorsAngleX[5] = camDirectionFlagX;
                        sensorsAngleY[5] = camDirectionFlagY;
                        sensorsAngleZ[5] = camDirectionFlagZ;

                        sensorUpX[5] = camUpFlagX;
                        sensorUpY[5] = camUpFlagY;
                        sensorUpZ[5] = camUpFlagZ;

                        sensorPositionX[5] = camDirectionFlagX;
                        sensorPositionY[5] = camDirectionFlagY;
                        sensorPositionZ[5] = camDirectionFlagZ;
                    }

                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                            vector2 = new Vector3(0f, 1f, 0f), -camVelocity);
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);

                    botTimer2 += Gdx.graphics.getDeltaTime();
                }

                if (botTimer1 > timePeriod && botTimer2 > timePeriod) {

                    camera.direction.x = camDirectionFlagX;
                    camera.direction.y = camDirectionFlagY;
                    camera.direction.z = camDirectionFlagZ;

                    camera.up.x = camUpFlagX;
                    camera.up.y = camUpFlagY;
                    camera.up.z = camUpFlagZ;

                    camera.position.x = camPositionFlagX;
                    camera.position.y = camPositionFlagY;
                    camera.position.z = camPositionFlagZ;

                    checkForSensors = true;
                    findFlag = true;
                }
            }
            */

            /*

            float timePeriod = 2f;
            float camVelocity = 7f;

            if (sensorsReady) {

                if (countForSensors < 1) {
                    for (int j = 0; j < sensorsSize.length; j++) {
                        minEuclideanDist[j] = 100;
                    }
                }

                countForSensors++;

                modelBatch.render(sensors, environment);

                if (botTimer1 < timePeriod) {

                    for (int i = 0; i < sensorsSize.length; i++) {


                        if (i==0 || i==10) {
                        System.out.println("i = " + i);
                        System.out.println("real dist = " + euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i));
                        System.out.println("theoric dist  = " + minEuclideanDist[i]);
                        System.out.println();
                        }


                        if (euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i) < minEuclideanDist[i]) {


                            if (i==0 || i==10) {
                                System.out.println("Is ok");
                            }


                            minEuclideanDist[i] = euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i);

                            sensorsAngleX[i] = camera.direction.x;
                            sensorsAngleY[i] = camera.direction.y;
                            sensorsAngleZ[i] = camera.direction.z;

                            sensorUpX[i] = camera.up.x;
                            sensorUpY[i] = camera.up.y;
                            sensorUpZ[i] = camera.up.z;

                            sensorPositionX[i] = camera.position.x;
                            sensorPositionY[i] = camera.position.y;
                            sensorPositionZ[i] = camera.position.z;
                        }
                    }

                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                            vector2 = new Vector3(0f, 1f, 0f), camVelocity);
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);

                    botTimer1 += Gdx.graphics.getDeltaTime();
                }

                if (botTimer1 > timePeriod && botTimer2 < timePeriod+0.5f) {

                    for (int i = 0; i < sensorsSize.length; i++) {

                        if (euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i) < minEuclideanDist[i]) {

                            minEuclideanDist[i] = euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i);

                            sensorsAngleX[i] = camera.direction.x;
                            sensorsAngleY[i] = camera.direction.y;
                            sensorsAngleZ[i] = camera.direction.z;

                            sensorUpX[i] = camera.up.x;
                            sensorUpY[i] = camera.up.y;
                            sensorUpZ[i] = camera.up.z;

                            sensorPositionX[i] = camera.position.x;
                            sensorPositionY[i] = camera.position.y;
                            sensorPositionZ[i] = camera.position.z;
                        }
                    }

                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                            vector2 = new Vector3(0f, 1f, 0f), -camVelocity);
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);

                    botTimer2 += Gdx.graphics.getDeltaTime();
                }

                if (botTimer1 > timePeriod && botTimer2 > timePeriod+0.5f) {

                    // To comment
                    camera.direction.x = camDirectionFlagX;
                    camera.direction.y = camDirectionFlagY;
                    camera.direction.z = camDirectionFlagZ;

                    camera.up.x = camUpFlagX;
                    camera.up.y = camUpFlagY;
                    camera.up.z = camUpFlagZ;

                    camera.position.x = camPositionFlagX;
                    camera.position.y = camPositionFlagY;
                    camera.position.z = camPositionFlagZ;


                    botReady = true;
                    sensorsReady = false;
                }
            }

            */

            if (botReady) {

                for (int i = 0; i < sensorsSize.length; i++) {
                    for (int j = 0; j < 10; j++) {
                        int stepIndex = (i*10)+j;
                        int stepIndexBis = 0;
                        float posX = 0;
                        float posZ = 0;
                        float[] stepOutput = new float[18];
                        boolean[] stepCollision = new boolean[10];
                        for (int k = 0; k < stepOutput.length; k++) {
                            if (k==0) {
                                stepOutput[k] = sensorsAngleX[i];
                            }
                            if (k==1) {
                                stepOutput[k] = sensorsAngleZ[i];
                            }
                            if (k==2) {
                                /*
                                System.out.println("not ok");
                                System.out.println("sensorsAngleX = " + Arrays.toString(sensorsAngleX));
                                System.out.println("sensorsAngleZ = " + Arrays.toString(sensorsAngleZ));
                                System.out.println("sensorsAngleX[i] = " + sensorsAngleX[i]);
                                System.out.println("sensorsAngleZ[i] = " + sensorsAngleZ[i]);
                                System.out.println("stepPositionX = " + stepPositionX[stepIndex]);
                                System.out.println("stepPositionZ = " + stepPositionZ[stepIndex]);
                                */


                                /*
                                System.out.println("stepPositionX = " + stepPositionX[stepIndex]);
                                System.out.println("stepPositionZ = " + stepPositionZ[stepIndex]);
                                System.out.println("sensorsAngleX = " + sensorsAngleX[i]);
                                System.out.println("sensorsAngleZ = " + sensorsAngleZ[i]);
                                */

                                //System.out.println("stuck");

                                /*
                                System.out.println("ballPositionX = " + ballPositionX);
                                System.out.println("ballPositionZ = " + ballPositionZ);
                                */

                                /*
                                System.out.println("stepPositionX = " + Arrays.toString(stepPositionX));
                                System.out.println("stepPositionZ = " + Arrays.toString(stepPositionZ));
                                System.out.println("stepPositionX current = " + stepPositionX[stepIndex]);
                                System.out.println("stepPositionZ current = " + stepPositionZ[stepIndex]);
                                System.out.println("sensorsAngleX = " + Arrays.toString(sensorsAngleX));
                                System.out.println("sensorsAngleZ = " + Arrays.toString(sensorsAngleZ));
                                System.out.println("sensorsAngleX current = " + sensorsAngleX[i]);
                                System.out.println("sensorsAngleZ current = " + sensorsAngleZ[i]);
                                System.out.println();
                                */

                                /*
                                System.out.println("step x = " + stepPositionX[stepIndex]);
                                System.out.println("step z = " + stepPositionZ[stepIndex]);

                                System.out.println("stuck");

                                float velocity = evaluatePowerRK4(ballPositionX,ballPositionZ,stepPositionX[stepIndex],stepPositionZ[stepIndex],sensorsAngleX[i],sensorsAngleZ[i]);
                                stepOutput[k] = velocity;

                                System.out.println("ok");
                                */
                            }
                            if (k==3) {

                                /*
                                Rungekuttasolver RK4 = new Rungekuttasolver();

                                RK4.setValues(ballPositionX, ballPositionZ, sensorsAngleX[i]*stepOutput[k-1], sensorPositionZ[i]*stepOutput[k-1]);

                                RK4.RK4();

                                posX = (float) RK4.getX();
                                posZ = (float) RK4.getY();

                                System.out.println("posX = " + posX);
                                System.out.println("posZ = " + posZ);
                                System.out.println();
                                */

                                posX = stepPositionX[stepIndex];
                                posZ = stepPositionZ[stepIndex];

                                for (int l = 0; l < numberOfTree; l++) {
                                    if (euclideanDistObstacles(posX, posZ, l) < 0.5f) {
                                        stepOutput[k] = 1;
                                        stepCollision[j] = true;
                                    }
                                    else {
                                        stepOutput[k] = 0;
                                        stepCollision[j] = false;
                                    }
                                }

                                if (defineFunction(posX, posZ) < 0.05f) {
                                    stepOutput[k] = 1;
                                    stepCollision[j] = true;
                                }
                                else {
                                    stepOutput[k] = 0;
                                    stepCollision[j] = false;
                                }

                                if (outOfField(posX, posZ)) {
                                    stepOutput[k] = 1;
                                    stepCollision[j] = true;
                                }
                                else {
                                    stepOutput[k] = 0;
                                    stepCollision[j] = false;
                                }
                            }
                            if (k==4) {
                                if (stepCollision[j]) {
                                    stepOutput[k] = 0;
                                }
                                else {
                                    if (isWin(posX, posZ)) {
                                        stepOutput[k] = 1;
                                    }
                                }
                            }
                            if (k==5) {
                                stepOutput[k] = posX;
                            }
                            if (k==6) {
                                stepOutput[k] = posZ;
                            }
                            if (k > 6) {
                                stepOutput[k] = sensorsSize[stepIndexBis]/10f;
                                stepIndexBis++;
                            }
                        }
                        //System.out.println("stepData = " + Arrays.toString(stepData));
                        //System.out.println("stepOutput = " + Arrays.toString(stepOutput));

                        sensorsOutput.add(stepOutput);
                        //sensorsData.add(stepData);
                    }
                }
                //isBotReady = true;


                for (int i = 0; i < sensorsOutput.size(); i++) {
                    System.out.println("sensorsOutput = " + Arrays.toString(sensorsOutput.get(i)));
                }


                countIndex++;
                ballStop = false;

                AgentBot bot = new AgentBot(sensorsSize, sensorsAngleX, sensorsAngleZ, maxPositionAgentX, maxPositionAgentZ, canHitFlag, isSensorOnSand, ballPositionX, ballPositionZ);

                float[] newPositions = bot.startBot();

                agentPower = bot.getPowerScalar();
                //System.out.println("agentPower = " + agentPower);

                bestSensor = bot.getBestSensor();

                camera.direction.x = sensorsAngleX[bestSensor];
                camera.direction.y = sensorsAngleY[bestSensor];
                camera.direction.z = sensorsAngleZ[bestSensor];

                camera.up.x = sensorUpX[bestSensor];
                camera.up.y = sensorUpY[bestSensor];
                camera.up.z = sensorUpZ[bestSensor];

                camera.position.x = sensorPositionX[bestSensor];
                camera.position.y = sensorPositionY[bestSensor];
                camera.position.z = sensorPositionZ[bestSensor];

                sensorsAngleX = new float[sensorsSize.length];
                sensorsAngleY = new float[sensorsSize.length];
                sensorsAngleZ = new float[sensorsSize.length];

                sensorUpX = new float[sensorsSize.length];
                sensorUpY = new float[sensorsSize.length];
                sensorUpZ = new float[sensorsSize.length];

                sensorPositionX = new float[sensorsSize.length];
                sensorPositionY = new float[sensorsSize.length];
                sensorPositionZ = new float[sensorsSize.length];

                newBallPositionX = newPositions[0];
                newBallPositionZ = newPositions[1];

                canTranslateCam = true;

                positionArrayX[countIndex] = newBallPositionX;
                positionArrayZ[countIndex] = newBallPositionZ;

                ballPositionX = positionArrayX[countIndex - 1];
                ballPositionZ = positionArrayZ[countIndex - 1];

                int ballStep = 100;
                ballStepXmean = (positionArrayX[countIndex] - positionArrayX[countIndex - 1]) / ballStep;
                ballStepZmean = (positionArrayZ[countIndex] - positionArrayZ[countIndex - 1]) / ballStep;

                sumX = 0;
                sumZ = 0;
                //countIterationBot = 0;

                count = 0;
                counter = 0;
                botTimer1 = 0;
                botTimer2 = 0;
                countForSensors = 0;
                countForSensorsReady = 0;
                countForFlag = 0;
                countForSensor0Ready = 0;

                check = false;
                botReady = false;
                //isBotReady = false;
                findFlag = false;
                checkCamera = false;

                isSensorOnSand = new boolean[11];
                checkForSensorsStep = new boolean[11];

                finishAgent = true;
            }


            /*
            for (int i = 0; i < sensorsData.size(); i++) {
                System.out.println("sensorsData = " + Arrays.toString(sensorsData.get(i)));
            }
            for (int i = 0; i < sensorsOutput.size(); i++) {
                System.out.println("sensorsOutput = " + Arrays.toString(sensorsOutput.get(i)));
            }
            */
        }


        public void checkForSpaceInput(){

            //key input to take a shot with the given power
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){

                double power = shot_Power;

                countIndex++;

                ballStop = false;

                // Condition that checks the game mode chosen by the user before taking the shot
                if (gameMode.gameName.equals("Single_Player")) {

                    // Instance of the RK4 solver
                    // Used to compute the next position of the ball based on the current position, the camera direction and the power
                    if (SolverScreen.getSolverName().equals("RK4")) {

                        directionX = camera.direction.x;
                        directionZ = camera.direction.z;

                        Rungekuttasolver RK4 = new Rungekuttasolver();

                        RK4.setValues(ballPositionX, ballPositionZ, (directionX*power)*500, (directionZ*power)*500);

                        //float allowableRange = 0.000001f;

                        RK4.RK4();

                        newBallPositionX = (float) RK4.getX();
                        newBallPositionZ = (float) RK4.getY();

                        /*
                        while (!checkSolver) {
                            if (vx > allowableRange || vy > allowableRange) {

                                RK4.RK4();
                                
                                newBallPositionX = (float) RK4.getX();
                                newBallPositionZ = (float) RK4.getY();

                                vx = (float) RK4.getVx();
                                vy = (float) RK4.getVy();
                                System.out.println("ok");
                            }
                            else {
                                System.out.println("ok1");
                                checkSolver = true;
                            }
                        }
                        */
                    }

                    // Instance of the Verlet solver
                    // Used to compute the next position of the ball based on the current position, the camera direction and the power
                    if (SolverScreen.getSolverName().equals("Verlet")) {

                        int scalar = 500;
                        directionX = camera.direction.x;
                        directionZ = camera.direction.z;

                        VerletSolver Verlet = new VerletSolver();

                        Verlet.setValues(ballPositionX, ballPositionZ, (directionX * power) * scalar, (directionZ * power) * scalar);

                        Verlet.Verlet();
                        newBallPositionX = (float) Verlet.getX();
                        newBallPositionZ = (float) Verlet.getY();

                        vx = (float) Verlet.getVx();
                        vy = (float) Verlet.getVx();
                    }

                }
                else if (gameMode.gameName.equals("Bot")) {

                    Rungekuttasolver Verlet = new Rungekuttasolver();

                    PuttingBotDeployement bot = new PuttingBotDeployement();
                    int scalar = 500;
                    bot.start();
                    power = bot.getVelo();
                    directionX = (float) bot.getXVector();
                    directionZ = (float) bot.getYVector();
                    System.out.println("Direction x: " + directionX + "Direction y: " + directionZ);

                    System.out.println("dx: " + directionX);
                    System.out.println("dz: " + directionZ);
                    Verlet.setValues(ballPositionX, ballPositionZ, (directionX * power) * scalar, (directionZ * power) * scalar);

                }

                positionArrayX[countIndex] = newBallPositionX;
                positionArrayZ[countIndex] = newBallPositionZ;

                //System.out.println("distance x = " + (positionArrayX[countIndex] - positionArrayX[countIndex-1]));
                //System.out.println("distance z = " + (positionArrayZ[countIndex] - positionArrayZ[countIndex-1]));

                //System.out.println("euclidean distance = " + Math.sqrt(Math.pow((positionArrayX[countIndex] - positionArrayX[countIndex-1]),2) + Math.pow((positionArrayZ[countIndex] - positionArrayZ[countIndex-1]),2)));

                ballPositionX = positionArrayX[countIndex -1];
                ballPositionZ = positionArrayZ[countIndex -1];

                int ballStep = 100;
                ballStepXmean = (positionArrayX[countIndex]-positionArrayX[countIndex -1])/ ballStep;
                ballStepZmean = (positionArrayZ[countIndex]-positionArrayZ[countIndex -1])/ ballStep;


                // Reset the power after the shot
                setShot_Power(getSTARTING_SHOT_POWER());

                sumX = 0;
                sumZ = 0;

                trackShot = true;
                canTranslateCam = true;
                canReset = true;
            }
        }

        /**
         * checks for input and updates the given PuttingGameScreen accordingly
         */
        public void checkForInput(){

            if (gameMode.gameName.equals("Single_Player")) {

                // Some key pressed input to rotate the camera and also zoom in zoom out
                if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                            vector2 = new Vector3(0f, -1f, 0f), 1f);
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
                    /*
                    System.out.println("Direction  X = " + camera.direction.x);
                    System.out.println("Direction  Z = " + camera.direction.z);
                    System.out.println();
                    */

                }
                if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                            vector2 = new Vector3(0f, 1f, 0f), 1f);
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);

                    /*
                    System.out.println("Direction  X = " + camera.direction.x);
                    System.out.println("Direction  Y = " + camera.direction.y);
                    System.out.println("Direction  Z = " + camera.direction.z);
                    System.out.println("Up X = " + camera.up.x);
                    System.out.println("Up Y = " + camera.up.y);
                    System.out.println("Up Z = " + camera.up.z);
                    System.out.println("Position  X = " + camera.position.x);
                    System.out.println("Position  Y = " + camera.position.y);
                    System.out.println("Position  Z = " + camera.position.z);
                    System.out.println();

                    */

                    /*
                    if (camera.direction.x < minAngleX) {
                        minAngleX = camera.direction.x;
                        System.out.println("minAngleX = " + minAngleX);
                    }
                    if (camera.direction.x > maxAngleX) {
                        maxAngleX = camera.direction.x;
                        System.out.println("maxAngleX = " + maxAngleX);
                    }
                    */

                }
                if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    //round the shot power to two decimal places to avoid errors where the power would get above max power
                    shot_Power = Util.round(shot_Power, 2);
                    if(shot_Power < MAX_SPEED - POWER_INCREMENT){
                        IncrementShotPower(1);
                    }
                    //System.out.println("shot power now at: " + shot_Power);
                }
                if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    //round the shot power to two decimal places to avoid errors where the power would get below 0
                    shot_Power = Util.round(shot_Power, 2);
                    if(shot_Power > 0 + POWER_INCREMENT){
                        IncrementShotPower(-1);
                    }
                    //System.out.println("shot power now at: " + shot_Power);
                }
                /*
                // Key pressed input to be back on the game mode screen
                if(Gdx.input.isKeyPressed(Input.Keys.B)) {
                    game.setScreen(new GameModeScreen(game));
                }
                */
                // Key pressed input to quit
                if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    Gdx.app.exit();
                }

                // Key press input R that return to the place of the previous shot
                if (Gdx.input.isKeyPressed(Input.Keys.R)) {

                    // Condition that checks if the ball shot can be reset to the previous one
                    if (canReset) {
                        // Condition that checks if the camera can be moved after pushing keyboard command R
                        if (trackShot) {
                            camera.translate(-(translateX[countIndex - 1]), -0.001f, -(translateZ[countIndex - 1]));
                        }
                        trackShot = false;
                        canTranslateCam = false;
                        // Call of the method that reset the ball to the previous place
                        resetBallShot();
                        camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
                    }
                }
            }
        }
    }
}