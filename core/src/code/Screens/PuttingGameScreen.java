package code.Screens;

import code.Bot.PuttingBotDeployement;
import code.Physics.Rungekuttasolver;
import code.Physics.VerletSolver;
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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;
import com.game.game.Game;

import java.util.ArrayList;
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
    private static ModelBuilder modelBuilder;
    private Environment environment;
    private float directionX;
    private float directionZ;

    // Instances variables for the ball in the game
    //private Model ball;
    private ModelInstance ballInstance;
    private float ballSize = 0.2f;
    private float ballPositionX = 0;
    private float ballPositionZ = 0;
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
    private final double MAX_SPEED = 3;//for now the max speed is 3 (should be possible to change per course)
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
    private static float winRadius = 2;

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
    private int period = 5000;

    private drawObject drawObject = new drawObject();

    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;

    private Array<ModelInstance> instances;
    private Model ballModel;

    private ModelInstance ball;

    private btCollisionShape ballShape;
    private btCollisionShape trunkShape;
    private btCollisionShape branchesShape;

    private btCollisionObject ballObject;
    private btCollisionObject trunkObject;
    private btCollisionObject branchesObject;

    private Array<btCollisionObject> obstacleObjects = new Array<>();

    private boolean isCollision = false;

    private boolean ballStop = true;

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

    /**
     * Method used to create the 3D field
     */
    public void createField() {

        Bullet.init();

        // Creation of the 3D perspective camera
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(-3.5f, 3f, -3.5f);
        camera.lookAt(ballPositionX, defineFunction(ballPositionX,ballPositionZ), ballPositionZ);
        camera.near = 0.1f;
        camera.far = 400f;
        
        generateRandomFlagPosition(20,40,20,40);

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

        for (int i = 0; i < 15; i++) {

            Random rand = new Random();
            float randomX = 5 + rand.nextFloat() * (45 - 5);
            float randomZ = 5 + rand.nextFloat() * (45 - 5);

            while (((Math.abs(randomX-flagPositionX) < 5) && (Math.abs(randomZ-flagPositionZ) < 5)) || (defineFunction(randomX, randomZ) < 0)) {
                randomX = 5 + rand.nextFloat() * (45 - 5);
                randomZ = 5 + rand.nextFloat() * (45 - 5);
            }

            ModelInstance[] treeInstances = drawObject.drawTree(randomX,randomZ);

            instances.add(treeInstances[0], treeInstances[1]);

            trunkShape = new btCylinderShape(new Vector3(0.25f,3,0.25f));

            trunkObject = new btCollisionObject();
            trunkObject.setCollisionShape(trunkShape);
            trunkObject.setWorldTransform(treeInstances[0].transform);
            obstacleObjects.add(trunkObject);

            branchesShape = new btConeShape(2,3);

            branchesObject = new btCollisionObject();
            branchesObject.setCollisionShape(branchesShape);
            branchesObject.setWorldTransform(treeInstances[1].transform);
            obstacleObjects.add(branchesObject);
        }

        ballShape = new btSphereShape(ballSize);

        ballObject = new btCollisionObject();
        ballObject.setCollisionShape(ballShape);
        ballObject.setWorldTransform(ball.transform);

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);

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

        float field0 = (float) (0.5);
        float field1 = (float) (((Math.sin(x) + Math.sin(y))/4)+0.3);
        float field2 = (float) ((Math.sin(x))/3)+0.5f;
        float field3 = (float) (((Math.atan(x) + Math.atan(y))/2)+0.5);
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

        if (defineFunction(x,z) < 0) {
            return 1f;
        }
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

    /**
     * Method that return the euclidean distance between the ball and the flag
     * @param positionX the first coordinate on the field
     * @param positionZ the second coordinate on the field
     * @return the euclidean distance between that given point and the flag
     */
    public static float euclideanDist(float positionX, float positionZ) {

        return (float) Math.sqrt(Math.pow((positionX-flagPositionX), 2) + Math.pow((positionZ-flagPositionZ), 2));
    }

    /**
     * Method that checks if the ball is close enough to the flag
     * @param positionX the first coordinate on the field
     * @param positionZ the second coordinate on the field
     * @return true if the ball is close enough to the field, false otherwise
     */
    public boolean isWin(float positionX, float positionZ) {

        if(euclideanDist(positionX, positionZ) < winRadius) return true;
        return false;
    }

    /**
     * Method that checks if the ball falls into the water
     * @param positionX the first coordinate of the ball
     * @param positionZ the second coordinate of the ball
     * @return true if the ball falls into water, false otherwise
     */
    public boolean isInWater(float positionX, float positionZ) {

        if(defineFunction(positionX, positionZ) < 0) return true;
        return false;
    }

    /**
     * Method that checks if the ball is still in the field
     * @param positionX the first coordinate of the ball
     * @param positionZ the second coordinate of the ball
     * @return true if the ball go out of the field, false otherwise
     */
    public boolean outOfField(float positionX, float positionZ) {

        if(positionX > gridDepth || positionZ > gridWidth || positionX < 0 || positionZ < 0) return true;
        return false;
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

        // Condition that checks if the new position of the ball is different from the current one
        // If it is the case, move the ball to that new position
        if (((((ballPositionX < newBallPositionX) && (ballPositionZ < newBallPositionZ)) ||
                ((ballPositionX > newBallPositionX) && (ballPositionZ > newBallPositionZ)) ||
                ((ballPositionX < newBallPositionX) && (ballPositionZ > newBallPositionZ)) ||
                ((ballPositionX > newBallPositionX) && (ballPositionZ < newBallPositionZ))) &&
                ((ballPositionXround != newBallPositionXround) && (ballPositionZround != newBallPositionZround))) &&
                (canTranslateCam)) {

            // Scalar factor used for the camera translation
            float scaleFactor = 50;
            ballPositionX += ballStepXmean;
            ballPositionZ += ballStepZmean;
            camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
            camera.translate((newBallPositionX - ballPositionX) / scaleFactor, 0.001f / scaleFactor, (newBallPositionZ - ballPositionZ) / scaleFactor);

            sumX += (newBallPositionX - ballPositionX) / (scaleFactor);
            sumZ += (newBallPositionZ - ballPositionZ) / (scaleFactor);

            translateX[countIndex - 1] = sumX;
            translateZ[countIndex - 1] = sumZ;

            ballStop = false;
        }
        else {
            ballStop = true;
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
    public void generateRandomFlagPosition(float boundX1, float boundX2, float boundZ1, float boundZ2) {

        Random rand = new Random();
        float randomX = boundX1 + rand.nextFloat() * (boundX2 - boundX1);
        float randomZ = boundZ1 + rand.nextFloat() * (boundZ2 - boundZ1);

        while (defineFunction(randomX, randomZ) < 0) {
            randomX = boundX1 + rand.nextFloat() * (boundX2 - boundX1);
            randomZ = boundZ1 + rand.nextFloat() * (boundZ2 - boundZ1);
        }

        flagPositionX = randomX;
        flagPositionZ = randomZ;

        System.out.println("x: " + randomX);
        System.out.println("z: " + randomZ);
    }

    /**
     * Method that checks the collision between two 3D shapes
     * @param ballObject object that represents the ball on the field
     * @param obstacleObject object that represent one of the obstacle on the field
     * @return true if there is a collision
     */
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

        // Reset of the timer variable used to freeze the ball
        float timer = 0;

        modelBatch.begin(camera);

        // Render the instance of the field with the given environment
        for (int i = 0; i < numberOfFields; i++) {
            modelBatch.render(fieldInstance[i], environment);
            modelBatch.render(slopeInstance[i], environment);
        }

        modelBatch.render(ball, environment);
        modelBatch.render(instances, environment);

        ballObject.setWorldTransform(ball.transform);

        // Loop that checks the collision between each obstacle and the ball
        for (int i = 0; i < obstacleObjects.size; i++) {
            if (!isCollision) {
                isCollision = checkCollision(ballObject, obstacleObjects.get(i));
            }
            else {
                // TODO: compute new ball position with the impact
                isCollision = false;
                System.out.println("--> Obstacle");
            }
        }

        ball = new ModelInstance(ballModel, ballPositionX, (defineFunction(ballPositionX, ballPositionZ))+(ballSize/2), ballPositionZ);

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

        ballMovement();

        // Condition used when the ball is out of the field
        if (outOfField(ballPositionX, ballPositionZ)) {

            displayMessage("Ball went out of the field");

            camera.translate(-(sumX), (float) (-0.001/3), -(sumZ));
            canTranslateCam = false;
            canReset = false;
            resetBallShot();
            camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
        }

        // Condition used to reset the ball position when the ball falls into water
        if (isInWater(ballPositionX, ballPositionZ)) {

            while (timer < period) {
                displayMessage("Ball fell in water");
                timer += Gdx.graphics.getDeltaTime();
            }
            camera.translate(-(sumX), (float) (-0.001 / 3), -(sumZ));
            canTranslateCam = false;
            canReset = false;
            // Call of the method that reset the ball to the previous place
            resetBallShot();
            camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
        }
        // Condition used to check if the ball is closed enough to the flag
        if (isWin(ballPositionX, ballPositionZ) && ballStop) {

            while (timer < period) {
                displayMessage("Win");
                timer+=Gdx.graphics.getDeltaTime();
            }
            game.setScreen(new GameModeScreen(game));
        }

        // Condition that only let the user controls when the Single Player mode is selected
        if (gameMode.gameName.equals("Single_Player")) {

            // Creation of the directive arrow
            arrow = modelBuilder.createArrow(ballPositionX, defineFunction(ballPositionX, ballPositionZ)+1f, ballPositionZ,
                    ((camera.direction.x)*5)+(ballPositionX), defineFunction(ballPositionX, ballPositionZ)+2f,
                    ((camera.direction.z)*5)+(ballPositionZ), 0.1f, 0.1f, 10,
                    GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.RED)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            arrowInstance = new ModelInstance(arrow);
            modelBatch.render(arrowInstance, environment);
            // Call of the class input handler that contains the majority of the user controls (only for single player)
            handler.checkForInput();
            handler.checkForSpaceInput();
        }
        else {
            // Bot mode only needs to have space input to work
            handler.checkForSpaceInput();
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

        trunkObject.dispose();
        trunkShape.dispose();

        branchesObject.dispose();
        branchesShape.dispose();

        ballObject.dispose();
        ballShape.dispose();

        dispatcher.dispose();
        collisionConfig.dispose();

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

    public class InputHandler {

        public void checkForSpaceInput(){

            //key input to take a shot with the given power
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){

                double power = shot_Power;

                countIndex++;

                // Condition that checks the game mode chosen by the user before taking the shot
                if (gameMode.gameName.equals("Single_Player")) {

                    // Instance of the RK4 solver
                    // Used to compute the next position of the ball based on the current position, the camera direction and the power
                    if (SolverScreen.getSolverName().equals("RK4")) {

                        int scalar = 600;
                        directionX = camera.direction.x;
                        directionZ = camera.direction.z;

                        Rungekuttasolver solver = new Rungekuttasolver();
                        solver.setValues(ballPositionX, ballPositionZ, (directionX*power)*scalar, (directionZ*power)*scalar);
                        solver.RK4();
                        newBallPositionX = (float) solver.getX();
                        newBallPositionZ = (float) solver.getY();
                    }

                    // Instance of the Verlet solver
                    // Used to compute the next position of the ball based on the current position, the camera direction and the power
                    if (SolverScreen.getSolverName().equals("Verlet")) {

                        int scalar = 500;
                        directionX = camera.direction.x;
                        directionZ = camera.direction.z;

                        VerletSolver solver = new VerletSolver();
                        solver.setValues(ballPositionX, ballPositionZ, (directionX*power)*scalar, (directionZ*power)*scalar);
                        solver.Verlet();
                        newBallPositionX = (float) solver.getX();
                        newBallPositionZ = (float) solver.getY();
                    }

                }
                else if (gameMode.gameName.equals("Bot")) {

                    Rungekuttasolver solver = new Rungekuttasolver();

                    PuttingBotDeployement bot = new PuttingBotDeployement();
                    int scalar = 500;
                    bot.start();
                    power = bot.getVelo();
                    directionX = (float) bot.getXVector();
                    directionZ = (float) bot.getYVector();
                    System.out.println("Direction x: " + directionX + "Direction y: " + directionZ);

                    System.out.println("dx: " + directionX);
                    System.out.println("dz: " + directionZ);
                    solver.setValues(ballPositionX, ballPositionZ, (directionX*power)*scalar, (directionZ*power)*scalar);
                }

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
                }
                if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
                            vector2 = new Vector3(0f, 1f, 0f), 1f);
                    camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
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
                // Key pressed input to be back on the game mode screen
                if(Gdx.input.isKeyPressed(Input.Keys.B)) {
                    game.setScreen(new GameModeScreen(game));
                }
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