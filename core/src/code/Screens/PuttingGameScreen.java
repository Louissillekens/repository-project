package code.Screens;

import code.Bot.AgentBot;
import code.Lets_Go_Champ.Alex_Clem;
import code.Lets_Go_Champ.Experience;
import code.Lets_Go_Champ.Qvalues;
import code.NN.ExportNeuralNets;
import code.NN.MathWork;
import code.Physics.Rungekuttasolver;
import code.astar.AStar;
import code.astar.Node;
import com.badlogic.gdx.Gdx;
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

import java.util.*;

import static com.badlogic.gdx.graphics.GL20.GL_TRIANGLES;

/**
 * A screen class to render a putting game
 * @author Clément Detry
 */
public class PuttingGameScreen implements Screen {

    private Game game;

    private InputHandler handler;

    private static PerspectiveCamera camera;
    private static ModelBatch modelBatch;
    private MeshPartBuilder meshPartBuilder;
    private static ModelBuilder modelBuilder;
    private static Environment environment;

    private static float startingPositionX = 2;
    private static float startingPositionZ = 2;
    private static float ballSize = 0.2f;
    private static float ballPositionX = startingPositionX;
    private static float ballPositionZ = startingPositionZ;
    public static float newBallPositionX;
    public static float newBallPositionZ;
    private static float ballStepXmean;
    private static float ballStepZmean;

    //QSHIT
    public static boolean freeToGo = false;

    private Model flatField;
    private ModelInstance[] fieldInstance;
    private Model slopeModel;
    private ModelInstance[] slopeInstance;
    private static Model arrow;
    private static ModelInstance arrowInstance;
    private static Color fieldColor;
    private static int numberOfFields = 1;
    private static float gridWidth = 50;
    private static float gridDepth = 50;

    public static Vector3 vector1;
    public static Vector3 vector2;

    private GameMode gameMode;

    private final double POWER_INCREMENT = 0.025;
    private static final double MAX_SPEED = 3;
    private static double STARTING_SHOT_POWER = 0.000001;
    public static double shot_Power = STARTING_SHOT_POWER;
    private ShapeRenderer shapeRenderer;

    private Model flag1;
    private ModelInstance flag1Instance;
    private Model flag2;
    private ModelInstance flag2Instance;
    private static float flagPositionX;
    private static float flagPositionZ;
    private static float winRadius = 3;

    public static int countIndex = 0;
    private static float[] positionArrayX = new float[100];
    private static float[] positionArrayZ = new float[100];

    public static float[] translateX = new float[100];
    public static float[] translateZ = new float[100];
    private static float sumX = 0;
    private static float sumZ = 0;
    public static boolean trackShot = false;
    public static boolean canTranslateCam = false;
    public static boolean canReset = false;

    private SpriteBatch batch;
    private BitmapFont font;
    private float fontX, fontY;
    private GlyphLayout layout = new GlyphLayout();
    private float timer = 0;
    private int period = 2;

    private drawObject drawObject = new drawObject();

    private Array<ModelInstance> instances;
    private Model ballModel;

    private ModelInstance ball;

    public static boolean ballStop = true;

    private boolean[] detectorCollision = new boolean[11];
    private int numberOfLinesSensors = 50;

    private static int numberOfTree = 50;

    private static float[] treePositionX;
    private static float[] treePositionZ;

    private static boolean[] canHitFlag = new boolean[11];
    private static boolean[] isSensorOnSand = new boolean[11];
    private static float[] sensorsSize = new float[11];
    private static float[] maxPositionX = new float[11];
    private static float[] maxPositionZ = new float[11];
    private static float[] maxPositionAgentX = new float[11];
    private static float[] maxPositionAgentZ = new float[11];
    private static float[] minEuclideanDist = new float[11];
    private static float[] sensorsAngleX = new float[11];
    private static float[] sensorsAngleY = new float[11];
    private static float[] sensorsAngleZ = new float[11];
    private static float[] sensorUpX = new float[11];
    private static float[] sensorUpY = new float[11];
    private static float[] sensorUpZ = new float[11];
    private static float[] sensorPositionX = new float[11];
    private static float[] sensorPositionY = new float[11];
    private static float[] sensorPositionZ = new float[11];
    private static boolean[] checkForSensorsStep = new boolean[11];

    private static Array<ModelInstance> sensors = new Array<>();

    private static float camDirectionFlagX;
    private static float camDirectionFlagY;
    private static float camDirectionFlagZ;
    private static float camUpFlagX;
    private static float camUpFlagY;
    private static float camUpFlagZ;
    private static float camPositionFlagX;
    private static float camPositionFlagY;
    private static float camPositionFlagZ;

    private static float finalPositionArrowX;
    private static float finalPositionArrowZ;

    private static float minDistanceArrowFlag;

    private static boolean checkForSensors = false;
    private static boolean sensorsReady = false;
    private static boolean botReady = false;
    private static boolean findFlag = false;
    private static boolean check = false;
    private boolean checkCollisionMessage = false;
    private boolean readyToTrain = false;
    private boolean ballMoved = false;
    public static boolean finishAgent = false;
    public static boolean isReadyToTrain = false;
    private static boolean checkCamera = false;
    private boolean checkAStar = false;
    private boolean aStarReady = true;

    private static float agentPower;
    private static float botTimer1 = 0;
    private static int countForFlag = 0;
    private static int countForBot = 0;
    private static int bestSensor = 0;
    private static int count = 0;
    private static int counter = 0;
    private static int countForSensors = 0;
    private static int countForSensorsReady = 0;
    private static int countForSensor0Ready = 0;
    private int nodeIndex = 0;
    private int countAStarIterations = 0;
    public static int countTries = 0;

    private static float[] stepPositionX = new float[sensorsSize.length*10];
    private static float[] stepPositionZ = new float[sensorsSize.length*10];

    private static ArrayList<float[]> sensorsOutput = new ArrayList<>();
    private float[] stepData;
    public static int action;

    private static int sandPitSize = 5;
    private static int amountOfSandPit = 30;

    private static float[] sandPitX1 = new float[amountOfSandPit];
    private static float[] sandPitZ1 = new float[amountOfSandPit];
    private static float[] sandPitX2 = new float[amountOfSandPit];
    private static float[] sandPitZ2 = new float[amountOfSandPit];

    private float[] saveStepX = new float[numberOfLinesSensors];
    private float[] saveStepZ = new float[numberOfLinesSensors];

    private String name = "";

    private AStar bot;
    private List<Node> nodes;
    private float[] newPosX;
    private float[] newPosZ;

    int secondPassed = 0;

    Timer time = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            secondPassed++;
            System.out.println("secondPassed = " + secondPassed);
        }
    };
    private int counterFirstIter;

    public void start() {
        time.schedule(timerTask, 1000, 1000);
    }

    /**
     * Constructor that creates a new instance of the putting game screen
     * @param game current instance of the game
     * @param gameMode preciously chosen game mode (not working yet)
     */
    public PuttingGameScreen(final Game game, GameMode gameMode) {

        this.game = game;
        this.gameMode = new GameMode(gameMode.gameName);
        this.createField();
        handler = new InputHandler(this);

        start();
    }

    public PuttingGameScreen(float startingPositionX, float startingPositionZ) {

        this.ballPositionX = startingPositionX;
        this.ballPositionZ = startingPositionZ;
        this.name = "Q_agent";
        finishAgent = false;
        this.gameMode = new GameMode("Bot");
        this.createField();
        handler = new InputHandler(this);
    }

    /**
     * Method used to create the 3D field
     */
    public void createField() {

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

            while (((Math.abs(randomX - flagPositionX) < 5) && (Math.abs(randomZ - flagPositionZ) < 5)) || (defineFunction(randomX, randomZ) < 0)) {
                randomX = 5 + rand.nextFloat() * (45 - 5);
                randomZ = 5 + rand.nextFloat() * (45 - 5);
            }

            treePositionX[i] = randomX;
            treePositionZ[i] = randomZ;

            ModelInstance[] treeInstances = drawObject.drawTree(randomX, randomZ);

            instances.add(treeInstances[0], treeInstances[1]);
        }

        // Adding an environment which is used for the luminosity
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1.f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0f, 1f));
    }

    /**
     * Method that defines the function used to create the form of the field
     * @param x first variable of the function
     * @param y second variable of the function
     * @return the estimation of the function at a given x and y point
     */
    public static float defineFunction(double x, double y) {

        float field0 = (float) (0.5);
        float field1 = (float) (((Math.sin(x) + Math.sin(y))/4)+0.3);
        float field2 = (float) ((Math.sin(x))/3)+0.2f;
        float field3 = (float) (((Math.atan(x) + Math.atan(y))/2)+0.5);
        float field4 = (float) (((Math.sin(x) + Math.sin(y))/4)+(Math.sin(2*x)/4)+0.3);
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

    public void takeRK4shot(float power, float scalar) {

        float directionX = camera.direction.x;
        float directionZ = camera.direction.z;

        Rungekuttasolver RK4 = new Rungekuttasolver();

        RK4.setValues(ballPositionX, ballPositionZ, (directionX*power)*scalar, (directionZ*power)*scalar);

        RK4.RK4();

        newBallPositionX = (float) RK4.getX();
        newBallPositionZ = (float) RK4.getY();

        positionArrayX[countIndex] = newBallPositionX;
        positionArrayZ[countIndex] = newBallPositionZ;

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

    /**
     * Method that makes the ball move to its new position
     */
    public void ballMovement() {

        double ballPositionXround = (double)(Math.round(ballPositionX*100))/10;
        double newBallPositionXround = (double)(Math.round(newBallPositionX*100))/10;
        double ballPositionZround = (double)(Math.round(ballPositionZ*100))/10;
        double newBallPositionZround = (double)(Math.round(newBallPositionZ*100))/10;

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

            positionArrayX[countIndex] = newBallPositionX;
            positionArrayZ[countIndex] = newBallPositionZ;

            translateX[countIndex - 1] = sumX;
            translateZ[countIndex - 1] = sumZ;

            ballStop = false;
            sensorsReady = false;
            checkAStar = false;
        }
        else {
            ballStop = true;
            checkAStar = true;
            if (countIndex > 0) {
                ballPositionX = positionArrayX[countIndex];
                ballPositionZ = positionArrayZ[countIndex];
            }
        }
    }

    /**
     * Method used to draw a string on the screen
     * @param message to draw on the screen
     */
    public void displayMessage(String message){

        batch = new SpriteBatch();
        font = new BitmapFont();

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

    public boolean checkCollision(int index) {

        return euclideanDistObstacles(ballPositionX, ballPositionZ, index) < 0.4f;
    }

    public void drawSensors(double angle, float x1, float x2, float z1, float z2, int index, int num) {

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

        for (int i = 0; i < detectorCollision.length; i++) {

            if (i!=5) {
                if (num == i) {
                    if (collision) {
                        detectorCollision[num] = true;
                        maxPositionAgentX[num] = ballPositionX+rotationX1;
                        maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
                    } else {
                        maxPositionAgentX[num] = ballPositionX+rotationX1;
                        maxPositionAgentZ[num] = ballPositionZ+rotationZ1;
                    }
                }
            }
        }

        if (!collision) {
            sensors.add(lineInstance);
        }
        sensorsSize[num] = (index + 1)/(numberOfLinesSensors/10f);
    }

    public static void cameraRotation(float angle) {

        camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                vector2 = new Vector3(0f, 1f, 0f), angle);
        camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
    }

    public static void drawArrow() {

        arrow = getModelBuilder().createArrow(ballPositionX, defineFunction(ballPositionX, ballPositionZ) + 1f, ballPositionZ,
                ((camera.direction.x) * 5) + (ballPositionX), defineFunction(ballPositionX, ballPositionZ) + 2f,
                ((camera.direction.z) * 5) + (ballPositionZ), 0.1f, 0.1f, 10,
                GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        arrowInstance = new ModelInstance(arrow);
        modelBatch.render(arrowInstance, environment);
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

    public static void resetValues() {

        sumX = 0;
        sumZ = 0;

        count = 0;
        counter = 0;
        botTimer1 = 0;
        countForSensors = 0;
        countForSensorsReady = 0;
        countForFlag = 0;
        countForSensor0Ready = 0;

        check = false;
        botReady = false;
        findFlag = false;
        checkCamera = false;

        isSensorOnSand = new boolean[11];
        checkForSensorsStep = new boolean[11];

        sensorsAngleX = new float[sensorsSize.length];
        sensorsAngleY = new float[sensorsSize.length];
        sensorsAngleZ = new float[sensorsSize.length];

        sensorUpX = new float[sensorsSize.length];
        sensorUpY = new float[sensorsSize.length];
        sensorUpZ = new float[sensorsSize.length];

        sensorPositionX = new float[sensorsSize.length];
        sensorPositionY = new float[sensorsSize.length];
        sensorPositionZ = new float[sensorsSize.length];
    }

    public void renderSensors() {

        if (checkForSensors) {
            sensors.clear();

            float maxVx = (flagPositionX - ballPositionX);
            float maxVz = (flagPositionZ - ballPositionZ);

            float stepX = 0;
            float stepZ = 0;
            float stepX1 = maxVx / numberOfLinesSensors;
            float stepZ1 = maxVz / numberOfLinesSensors;

            int i = 0;
            while (i < numberOfLinesSensors) {
                if (!detectorCollision[5]) {

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
                            detectorCollision[5] = true;
                        }
                    }

                    if (defineFunction(ballPositionX + stepX + stepX1, ballPositionZ + stepZ + stepZ1) < 0.05f) {
                        sensorsSize[5] = (i + 1) / (numberOfLinesSensors / 10f);
                        detectorCollision[5] = true;
                    }

                    if (outOfField(ballPositionX + stepX + stepX1, ballPositionZ + stepZ + stepZ1)) {
                        sensorsSize[5] = (i + 1) / (numberOfLinesSensors / 10f);
                        detectorCollision[5] = true;
                    }

                    if (euclideanDistFlag(ballPositionX + stepX + stepX1, ballPositionZ + stepZ + stepZ1) < 0.001f) {
                        canHitFlag[5] = true;
                        detectorCollision[5] = true;
                    } else {
                        canHitFlag[5] = false;
                    }

                    if (isOnSand(ballPositionX + stepX + stepX1, ballPositionZ + stepZ + stepZ1)) {
                        isSensorOnSand[5] = true;
                    }

                    if (!detectorCollision[5]) {
                        sensors.add(lineInstance);
                    }

                    sensorsSize[5] = (i + 1) / (numberOfLinesSensors / 10f);

                    maxPositionAgentX[5] = ballPositionX + stepX;
                    maxPositionAgentZ[5] = ballPositionZ + stepZ;
                }

                for (int j = 0; j < detectorCollision.length; j++) {

                    float angle = -135+((j+1)*22.5f);

                    if (j==0) {
                        angle-=22.5;
                    }
                    if (j==10) {
                        angle+=22.5;
                    }
                    if (j!=5) {
                        if (!detectorCollision[j]) {
                            drawSensors(angle, stepX, stepX1, stepZ, stepZ1, i,j);
                        }
                    }
                }

                saveStepX[i] = stepX+stepX1;
                saveStepZ[i] = stepZ+stepZ1;

                stepX += stepX1;
                stepZ += stepZ1;

                i++;
            }

            for (int j = 0; j < sensorsSize.length; j++) {
                for (int k = 0; k < 10; k++) {

                    int stepIndex1 = ((j*10)+(k));
                    int stepIndex2 = ((k+1)*5)-1;

                    float angle = -135+((j+1)*22.5f);

                    if (j==0) {
                        angle-=22.5;
                    }
                    if (j==10) {
                        angle+=22.5;
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

            detectorCollision = new boolean[11];

            for (int j = 0; j < sensorsSize.length; j++) {
                minEuclideanDist[j] = euclideanDistSensors(0, 0, j);
            }

            botTimer1 = 0;
            sensorsReady = true;
            checkForSensors = false;
        }
    }

    public static void startAgent() {

        finalPositionArrowX = ((camera.direction.x) * 5) + (ballPositionX);
        finalPositionArrowZ = ((camera.direction.z) * 5) + (ballPositionZ);

        drawArrow();

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

                cameraRotation(0.5f);

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

            } else {
                cameraRotation(4);

                if (check) {
                    findFlag = true;
                }
            }

            countForBot = 0;
        } else {
            if (countForBot < 1) {
                checkForSensors = true;
                countForBot++;
            } else {
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
                        minEuclideanDist[i] = euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i) + error;
                    }
                }

                cameraRotation(-camVelocity);

                botTimer1 += Gdx.graphics.getDeltaTime();
            }

            float camVelocity1 = 3f;
            float camVelocity2 = 0.5f;
            if (botTimer1 > timePeriod1 && !botReady) {
                if (counter < 1) {

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

                if (checkCamera) {
                    if (euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, countForSensorsReady) < minEuclideanDist[countForSensorsReady]) {
                        minEuclideanDist[countForSensorsReady] = euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, countForSensorsReady);
                        if (countForSensorsReady == 0) {
                            if (countForSensor0Ready < 5) {
                                cameraRotation(camVelocity2);
                            } else {
                                checkForSensorsStep[countForSensorsReady] = true;
                            }
                            countForSensor0Ready++;
                        } else {
                            cameraRotation(-camVelocity2);
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
                            cameraRotation(camVelocity1);
                        } else {
                            cameraRotation(-camVelocity1);
                        }
                    }
                    if (countForSensorsReady == 11) {
                        botReady = true;
                    }
                }
            }
        }

        if (botReady) {

            for (int i = 0; i < sensorsOutput.size(); i++) {
                System.out.println("sensorsOutput = " + Arrays.toString(sensorsOutput.get(i)));
            }

            finishAgent = true;

            if (BotScreen.getBotName().equals("agent")) {

                countIndex++;
                ballStop = false;

                AgentBot bot = new AgentBot(sensorsSize, sensorsAngleX, sensorsAngleZ, maxPositionAgentX, maxPositionAgentZ, canHitFlag, isSensorOnSand, ballPositionX, ballPositionZ);

                float[] newPositions = bot.startBot();

                agentPower = bot.getPowerScalar();

                bestSensor = bot.getBestSensor();

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

                camera.direction.x = sensorsAngleX[bestSensor];
                camera.direction.y = sensorsAngleY[bestSensor];
                camera.direction.z = sensorsAngleZ[bestSensor];

                camera.up.x = sensorUpX[bestSensor];
                camera.up.y = sensorUpY[bestSensor];
                camera.up.z = sensorUpZ[bestSensor];

                camera.position.x = sensorPositionX[bestSensor];
                camera.position.y = sensorPositionY[bestSensor];
                camera.position.z = sensorPositionZ[bestSensor];

                resetValues();
            }

            /*
            if (BotScreen.getBotName().equals("Q_agent")) {


            }
            */
        }
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

        ball = new ModelInstance(ballModel, ballPositionX, (defineFunction(ballPositionX, ballPositionZ))+(ballSize/2), ballPositionZ);

        if (ballStop) {
            if (!gameMode.gameName.equals("Single_Player")) {
                renderSensors();
            }
        }

        if (gameMode.gameName.equals("Single_Player")) {
            if (!ballStop) {
                checkForSensors = true;
            }
            else {
                modelBatch.render(sensors, environment);
            }
        }


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

        if (!checkCollisionMessage && !readyToTrain) {

            ballMovement();

            //System.out.println("ballPositionX = " + ballPositionX);
            //System.out.println("ballPositionZ = " + ballPositionZ);
            //System.out.println("newBallPositionX = " + newBallPositionX);
            //System.out.println("newBallPositionZ = " + newBallPositionZ);
            //System.out.println();
        }

        //if (!checkAStar) {
            //ballMovement();
        //}

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
                aStarReady = true;
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
                aStarReady = true;
            }
        }
        // Condition used to check if the ball is closed enough to the flag
        if (isWin(ballPositionX, ballPositionZ) && ballStop) {

            if (timer < period) {
                displayMessage("Win in " + countTries + " tries!");
                checkCollisionMessage = true;
                timer+=Gdx.graphics.getDeltaTime();
            }
            else {
                System.out.println("amount of tries = " + countTries);
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
                    aStarReady = true;
                }
            }
        }

        // Condition that only let the user controls when the Single Player mode is selected
        if (gameMode.gameName.equals("Single_Player")) {

            finalPositionArrowX = ((camera.direction.x)*5)+(ballPositionX);
            finalPositionArrowZ = ((camera.direction.z)*5)+(ballPositionZ);

            // Creation of the directive arrow
            drawArrow();

            if (ballStop) {
                handler.checkForInput();
                handler.checkForSpaceInput();
            }
        }
        else {
            if (BotScreen.getBotName().equals("agent")) {
                if (!checkCollisionMessage) {
                    startAgent();
                }
            }
            if (BotScreen.getBotName().equals("aStar")) {

                //System.out.println("aStarReady = " + aStarReady);

                if (aStarReady) {

                    //System.out.println("ok");

                    if (countAStarIterations > 0) {

                        ballPositionX = startingPositionX;
                        ballPositionZ = startingPositionZ;

                        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                        camera.position.set(-3.5f + startingPositionX, 3f, -3.5f + startingPositionZ);
                        camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
                        camera.near = 0.1f;
                        camera.far = 400f;

                        positionArrayX = new float[100];
                        positionArrayZ = new float[100];

                        translateX = new float[100];
                        translateZ = new float[100];

                        sumX = 0;
                        sumZ = 0;

                        countIndex = 0;
                        nodeIndex = 0;
                        countTries = 0;
                    }

                    bot = new AStar(this);
                    int samplesize = 1;
                    int nano = 1000000;
                    long startTime = System.nanoTime();
                    for (int i=0;i<samplesize;i++){
                        nodes = bot.findRoute(); //TODO find the time it took for A* to run 
                    }
                    long endTime = System.nanoTime();
                    System.out.println("average time per shot was " + (((endTime-startTime)/nano)/samplesize)+ " ms");

                    System.out.println("nodes = " + nodes.size());

                    newPosX = new float[nodes.size()];
                    newPosZ = new float[nodes.size()];

                    for (int i = 0; i < nodes.size(); i++) {

                        newPosX[i] = (float) nodes.get(i).getX();
                        newPosZ[i] = (float) nodes.get(i).getZ();
                    }

                    countAStarIterations++;

                    aStarReady = false;
                    checkAStar = true;
                }

                //System.out.println("checkAStar = " + checkAStar);
                //System.out.println("nodeIndex = " + nodeIndex);
                //System.out.println("countIndex = " + countIndex);

                if (checkAStar && nodeIndex < nodes.size()) {

                    countIndex++;

                    if (nodeIndex==0) {
                        newBallPositionX = newPosX[nodeIndex]+startingPositionX;
                        newBallPositionZ = newPosZ[nodeIndex]+startingPositionZ;
                    }
                    else {
                        newBallPositionX = newPosX[nodeIndex];
                        newBallPositionZ = newPosZ[nodeIndex];
                    }

                    if (!isWin(newBallPositionX, newBallPositionZ)) {
                        countTries++;
                    }

                    System.out.println("newPosX = " + newBallPositionX);
                    System.out.println("newPosZ = " + newBallPositionZ);
                    System.out.println();

                    positionArrayX[countIndex] = newBallPositionX;
                    positionArrayZ[countIndex] = newBallPositionZ;

                    //ballPositionX = positionArrayX[countIndex-1];
                    //ballPositionZ = positionArrayZ[countIndex-1];

                    //System.out.println("positionArrayX = " + Arrays.toString(positionArrayX));
                    //System.out.println("positionArrayZ = " + Arrays.toString(positionArrayZ));

                    int ballStep = 100;
                    ballStepXmean = (positionArrayX[countIndex]-positionArrayX[countIndex -1])/ ballStep;
                    ballStepZmean = (positionArrayZ[countIndex]-positionArrayZ[countIndex -1])/ ballStep;

                    sumX = 0;
                    sumZ = 0;

                    trackShot = true;
                    canTranslateCam = true;
                    canReset = true;

                    checkAStar = false;
                    nodeIndex++;
                }
                //System.out.println("checkAStar = " + checkAStar);
            }
        }

        if (name.equals("Q_agent")) {
            Alex_Clem lgbabe = BotScreen.getLets_Go_Baby();

            //After the first refresh
            if (freeToGo) {
                if (lgbabe.getE() < lgbabe.getNum_episodes()) {
                    int e = lgbabe.getE();

                    //We start from the starting position
                    if (lgbabe.gm.isDone()) {
                        lgbabe.gm.reset();
                        lgbabe.setDestination(false); //TODO IF ARRIVED AT DESTINATION
                    }

                    int action = lgbabe.agent.selectAction(lgbabe.state, lgbabe.policy_net); // Decide an action [0;109] from Q(s,a) = policy net (The agent didnt move yet)
                    int reward = lgbabe.gm.takeAction(action); //Get the reward from that action
                    lgbabe.setState(lgbabe.gm.getState());
                    float[] next_state = lgbabe.gm.getState(); // Get the new state (has been updated in takeAction)
                     System.out.println("experiences = " + e + " state " + Arrays.toString(lgbabe.state));

                    lgbabe.memory.push(new Experience(lgbabe.state, action, next_state, reward)); // Add a new experience of this step
                    lgbabe.setState(next_state); //Update the new position numerically
                    System.out.println("lgbabe.memory.push_count = " + lgbabe.memory.push_count);
                    // If the number of experiences in the memory is >= batch size we need
                    if (lgbabe.memory.canProvideSample(lgbabe.batch_size)) {
                        List<Experience> experiences = lgbabe.memory.getSample(lgbabe.batch_size);

                        //First get arrays of Q(s'a) and Q'(s',a)
                        float[] current_q_values = Qvalues.getCurrent(lgbabe.policy_net, experiences); // Compute the current Q values
                        float[] next_q_values = Qvalues.getNext(lgbabe.target_net, experiences); // Compute the next state action pair Q max value

                        float[] target_q_values = Qvalues.getTarget(next_q_values, experiences, lgbabe.gamma); // Apply the formula to it
                        float[] loss = MathWork.squaredError(current_q_values, target_q_values); // Compute the loss for every current-target pair

                        lgbabe.policy_net.backprop(loss, Qvalues.getActionCache()); // Backprop the loss to update the weights and bias
                    }

                    lgbabe.total_rewards_episodes[e] = lgbabe.gm.getTotalRewards(); // Store the total rewards of an episode

                    // Update the target_net check
                    if (e % lgbabe.target_network_update == 0) {
                        lgbabe.target_net.copyLayers(lgbabe.policy_net);
                    }

                    if (lgbabe.gm.isDone()) {
                        ExportNeuralNets.exportNetworks(lgbabe.policy_net, lgbabe.target_net);
                        lgbabe.setE(e++);
                    }

                }
            }
            if (counterFirstIter == 0)
                freeToGo = true;
            counterFirstIter++;


            /*if (LinkAgentNN.newAgent) {
                if (ballMoved) {
                    finishAgent = false;
                }
                if (!finishAgent) {
                    startAgent();
                }
                else {

                    int sensorNum = (int) Math.ceil(action/10f);

                    stepData = sensorsOutput.get(action);

                    if (stepData[0]==1) {
                        ballMoved = false;
                    }
                    else {
                        float power = evaluatePowerRK4(ballPositionX, ballPositionZ, stepData[3], stepData[4], sensorsAngleX[sensorNum], sensorsAngleZ[sensorNum]);

                        Rungekuttasolver RK4 = new Rungekuttasolver();

                        RK4.setValues(ballPositionX, ballPositionZ, sensorsAngleX[sensorNum]*power, sensorsAngleZ[sensorNum]*power);
                        RK4.RK4();

                        newBallPositionX = (float) RK4.getX();
                        newBallPositionZ = (float) RK4.getY();

                        canTranslateCam = true;

                        positionArrayX[countIndex] = newBallPositionX;
                        positionArrayZ[countIndex] = newBallPositionZ;

                        ballPositionX = positionArrayX[countIndex - 1];
                        ballPositionZ = positionArrayZ[countIndex - 1];

                        int ballStep = 100;
                        ballStepXmean = (positionArrayX[countIndex] - positionArrayX[countIndex - 1]) / ballStep;
                        ballStepZmean = (positionArrayZ[countIndex] - positionArrayZ[countIndex - 1]) / ballStep;

                        ballMoved = true;
                        ballStop = false;
                    }
                }
                if (finishAgent && ballStop) {
                    isReadyToTrain = true;
                }
            }*/
        }
    }

    public static ArrayList<float[]> sensorCalc(){

        for (int i = 0; i < sensorsSize.length; i++) {
            for (int j = 0; j < 10; j++) {
                int stepIndex = (i * 10) + j;
                int stepIndexBis = 0;
                float posX = 0;
                float posZ = 0;
                float[] stepOutput = new float[15];
                boolean[] stepCollision = new boolean[10];
                for (int k = 0; k < stepOutput.length; k++) {

                    if (k == 0) {
                        posX = stepPositionX[stepIndex];
                        posZ = stepPositionZ[stepIndex];

                        for (int l = 0; l < numberOfTree; l++) {
                            if (euclideanDistObstacles(posX, posZ, l) < 0.5f || stepCollision[j]) {
                                stepOutput[k] = 1;
                                stepCollision[j] = true;
                            } else {
                                stepOutput[k] = 0;
                                stepCollision[j] = false;
                            }
                        }

                        if (defineFunction(posX, posZ) < 0.05f || stepCollision[j]) {
                            stepOutput[k] = 1;
                            stepCollision[j] = true;
                        } else {
                            stepOutput[k] = 0;
                            stepCollision[j] = false;
                        }

                        if (outOfField(posX, posZ) || stepCollision[j]) {
                            stepOutput[k] = 1;
                            stepCollision[j] = true;
                        } else {
                            stepOutput[k] = 0;
                            stepCollision[j] = false;
                        }
                    }
                    if (k == 1) {
                        if (stepCollision[j]) {
                            stepOutput[k] = 0;
                        } else {
                            if (isWin(posX, posZ)) {
                                stepOutput[k] = 1;
                            }
                        }
                    }
                    if (k == 2) {
                        stepOutput[k] = posX;
                    }
                    if (k == 3) {
                        stepOutput[k] = posZ;
                    }
                    if (k > 4) {
                        stepOutput[k] = sensorsSize[stepIndexBis] / 10f;
                        stepIndexBis++;
                    }
                }
                sensorsOutput.add(stepOutput);
            }
        }
        return sensorsOutput;
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

    public static float getStartingPositionX() {
        return startingPositionX;
    }

    public static float getStartingPositionZ() {
        return startingPositionZ;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public float getGridWidth() {
        return gridWidth;
    }

    public float getGridDepth() {
        return gridDepth;
    }

}
