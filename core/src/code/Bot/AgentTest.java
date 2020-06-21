//package code.Bot;
//
//import code.Physics.Rungekuttasolver;
//import code.Screens.PuttingGameScreen;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.PerspectiveCamera;
//import com.badlogic.gdx.graphics.VertexAttributes;
//import com.badlogic.gdx.graphics.g3d.Material;
//import com.badlogic.gdx.graphics.g3d.Model;
//import com.badlogic.gdx.graphics.g3d.ModelInstance;
//import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
//import com.badlogic.gdx.math.Vector3;
//import com.badlogic.gdx.utils.Array;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import static code.Screens.PuttingGameScreen.countIndex;
//import static code.Screens.PuttingGameScreen.euclideanDistFlag;
//import static com.badlogic.gdx.graphics.GL20.GL_TRIANGLES;
//
//public class AgentTest {
//
//    //private PuttingGameScreen game;
//
//    private PerspectiveCamera camera;
//
//    private float ballPositionX;
//    private float ballPositionZ;
//    private float newBallPositionX;
//    private float newBallPositionZ;
//
//    private Vector3 vector1;
//    private Vector3 vector2;
//
//    private float[] sensorsAngleX = new float[11];
//    private float[] sensorsAngleY = new float[11];
//    private float[] sensorsAngleZ = new float[11];
//    private float[] sensorUpX = new float[11];
//    private float[] sensorUpY = new float[11];
//    private float[] sensorUpZ = new float[11];
//    private float[] sensorPositionX = new float[11];
//    private float[] sensorPositionY = new float[11];
//    private float[] sensorPositionZ = new float[11];
//    private float[] newPositions;
//    private boolean[] checkForSensorsStep = new boolean[11];
//
//    private float camDirectionFlagX;
//    private float camDirectionFlagY;
//    private float camDirectionFlagZ;
//    private float camUpFlagX;
//    private float camUpFlagY;
//    private float camUpFlagZ;
//    private float camPositionFlagX;
//    private float camPositionFlagY;
//    private float camPositionFlagZ;
//
//    private float finalPositionArrowX;
//    private float finalPositionArrowZ;
//
//    private float minDistanceArrowFlag;
//
//    public static boolean checkForSensors = false;
//    public static boolean sensorsReady = false;
//    public static boolean botReady = false;
//    public static boolean isBotReady = false;
//    private boolean findFlag = false;
//    private boolean check = false;
//
//    public static float botTimer1 = 0;
//    public static float botTimer2 = 0;
//    private int countForFlag = 0;
//    private int countForBot = 0;
//    private int bestSensor = 0;
//    private int count = 0;
//    private int counter = 0;
//    private int countForSensors = 0;
//    private int countForSensorsReady = 0;
//
//    private Array<ModelInstance> instancesTest = new Array<>();
//
//    private boolean checkCamera = false;
//
//    private ArrayList<float[]> sensorsOutput = new ArrayList<>();
//
//    public AgentTest() {
//
//        this.camera = PuttingGameScreen.getCamera();
//        this.ballPositionX = PuttingGameScreen.getBallPositionX();
//        this.ballPositionZ = PuttingGameScreen.getFlagPositionZ();
//        this.newBallPositionX = PuttingGameScreen.getNewBallPositionX();
//        this.newBallPositionZ = PuttingGameScreen.getNewBallPositionZ();
//
//    }
//
//    public void startAgent() {
//
//        finalPositionArrowX = ((camera.direction.x) * 5) + (ballPositionX);
//        finalPositionArrowZ = ((camera.direction.z) * 5) + (ballPositionZ);
//
//        // Creation of the directive arrow
//        Model arrow = PuttingGameScreen.getModelBuilder().createArrow(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ) + 1f, ballPositionZ,
//                ((camera.direction.x) * 5) + (ballPositionX), PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ) + 2f,
//                ((camera.direction.z) * 5) + (ballPositionZ), 0.1f, 0.1f, 10,
//                GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.RED)),
//                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
//        ModelInstance arrowInstance = new ModelInstance(arrow);
//        PuttingGameScreen.getModelBatch().render(arrowInstance, PuttingGameScreen.getEnvironment());
//
//
//        if (!findFlag && PuttingGameScreen.getBallStop()) {
//
//            PuttingGameScreen.canTranslateCam = false;
//
//            if (bestSensor == 5) {
//                findFlag = true;
//            }
//
//            if (countForFlag < 1) {
//                minDistanceArrowFlag = euclideanDistFlag(ballPositionX, ballPositionZ);
//            }
//
//            countForFlag++;
//
//            //System.out.println("final arrow x = " + euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ));
//            //System.out.println("minDistanceArrowFlag = " + minDistanceArrowFlag);
//
//            if (euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ) < minDistanceArrowFlag) {
//
//                //System.out.println("ok");
//
//                camera.rotateAround(vector1 = new Vector3(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                        vector2 = new Vector3(0f, 1f, 0f), 0.5f);
//                camera.lookAt(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//
//                minDistanceArrowFlag = euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ);
//
//                camDirectionFlagX = camera.direction.x;
//                camDirectionFlagY = camera.direction.y;
//                camDirectionFlagZ = camera.direction.z;
//
//                camUpFlagX = camera.up.x;
//                camUpFlagY = camera.up.y;
//                camUpFlagZ = camera.up.z;
//
//                camPositionFlagX = camera.position.x;
//                camPositionFlagY = camera.position.y;
//                camPositionFlagZ = camera.position.z;
//
//                sensorsAngleX[5] = camDirectionFlagX;
//                sensorsAngleY[5] = camDirectionFlagY;
//                sensorsAngleZ[5] = camDirectionFlagZ;
//
//                sensorUpX[5] = camUpFlagX;
//                sensorUpY[5] = camUpFlagY;
//                sensorUpZ[5] = camUpFlagZ;
//
//                sensorPositionX[5] = camDirectionFlagX;
//                sensorPositionY[5] = camDirectionFlagY;
//                sensorPositionZ[5] = camDirectionFlagZ;
//
//
//                if (count > 1) {
//                    check = true;
//                }
//
//                count++;
//            }
//            else {
//
//                camera.rotateAround(vector1 = new Vector3(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                        vector2 = new Vector3(0f, 1f, 0f), 4f);
//                camera.lookAt(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//
//                if (check) {
//                    findFlag = true;
//                }
//            }
//
//            countForBot = 0;
//        }
//        else {
//
//            if (countForBot < 1) {
//
//                /*
//                camera.direction.x = camDirectionFlagX;
//                camera.direction.y = camDirectionFlagY;
//                camera.direction.z = camDirectionFlagZ;
//
//                camera.up.x = camUpFlagX;
//                camera.up.y = camUpFlagY;
//                camera.up.z = camUpFlagZ;
//
//                camera.position.x = camPositionFlagX;
//                camera.position.y = camPositionFlagY;
//                camera.position.z = camPositionFlagZ;
//                */
//
//                checkForSensors = true;
//                countForBot++;
//            }
//            else {
//                checkForSensors = false;
//            }
//        }
//
//        if (sensorsReady) {
//
//            if (countForSensors < 1) {
//                for (int j = 0; j < PuttingGameScreen.getSensorsSize().length; j++) {
//                    PuttingGameScreen.getMinEuclideanDist()[j] = 100;
//                }
//            }
//
//            countForSensors++;
//
//            PuttingGameScreen.getModelBatch().render(PuttingGameScreen.getSensors(), PuttingGameScreen.getEnvironment());
//
//            float timePeriod1 = 2f;
//            float camVelocity = 7f;
//            float error = 0.1f;
//
//            if (botTimer1 < timePeriod1) {
//
//                for (int i = 0; i < PuttingGameScreen.getSensorsSize().length; i++) {
//                    if (PuttingGameScreen.euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i) < PuttingGameScreen.getMinEuclideanDist()[i]) {
//
//                        PuttingGameScreen.getMinEuclideanDist()[i] = PuttingGameScreen.euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i)+error;
//                    }
//                }
//
//                camera.rotateAround(vector1 = new Vector3(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                        vector2 = new Vector3(0f, 1f, 0f), camVelocity);
//                camera.lookAt(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//
//                botTimer1+= Gdx.graphics.getDeltaTime();
//            }
//
//
//            float camVelocity1 = 3f;
//            float camVelocity2 = 0.5f;
//            if (botTimer1 > timePeriod1 && !botReady) {
//
//                if (counter < 1) {
//
//                    camera.direction.x = camDirectionFlagX;
//                    camera.direction.y = camDirectionFlagY;
//                    camera.direction.z = camDirectionFlagZ;
//
//                    camera.up.x = camUpFlagX;
//                    camera.up.y = camUpFlagY;
//                    camera.up.z = camUpFlagZ;
//
//                    camera.position.x = camPositionFlagX;
//                    camera.position.y = camPositionFlagY;
//                    camera.position.z = camPositionFlagZ;
//
//                    checkCamera = true;
//                }
//                if (checkCamera) {
//                    counter++;
//                }
//
//                //for (int i = 0; i < sensorsSize.length; i++) {
//
//                if (checkCamera) {
//                    if (PuttingGameScreen.euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, countForSensorsReady) < PuttingGameScreen.getMinEuclideanDist()[countForSensorsReady]) {
//
//                        PuttingGameScreen.getMinEuclideanDist()[countForSensorsReady] = PuttingGameScreen.euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, countForSensorsReady);
//
//                        if (countForSensorsReady == 0) {
//                            camera.rotateAround(vector1 = new Vector3(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                                    vector2 = new Vector3(0f, 1f, 0f), camVelocity2);
//                            camera.lookAt(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//                        } else {
//                            camera.rotateAround(vector1 = new Vector3(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                                    vector2 = new Vector3(0f, 1f, 0f), -camVelocity2);
//                            camera.lookAt(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//                        }
//
//                        sensorsAngleX[countForSensorsReady] = camera.direction.x;
//                        sensorsAngleY[countForSensorsReady] = camera.direction.y;
//                        sensorsAngleZ[countForSensorsReady] = camera.direction.z;
//
//                        sensorUpX[countForSensorsReady] = camera.up.x;
//                        sensorUpY[countForSensorsReady] = camera.up.y;
//                        sensorUpZ[countForSensorsReady] = camera.up.z;
//
//                        sensorPositionX[countForSensorsReady] = camera.position.x;
//                        sensorPositionY[countForSensorsReady] = camera.position.y;
//                        sensorPositionZ[countForSensorsReady] = camera.position.z;
//
//                        checkForSensorsStep[countForSensorsReady] = true;
//                    } else {
//                        if (checkForSensorsStep[countForSensorsReady]) {
//                            countForSensorsReady++;
//                        }
//                        if (countForSensorsReady == 0) {
//                            camera.rotateAround(vector1 = new Vector3(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                                    vector2 = new Vector3(0f, 1f, 0f), camVelocity1);
//                            camera.lookAt(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//                        } else {
//                            camera.rotateAround(vector1 = new Vector3(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                                    vector2 = new Vector3(0f, 1f, 0f), -camVelocity1);
//                            camera.lookAt(ballPositionX, PuttingGameScreen.defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//                        }
//                    }
//                    if (countForSensorsReady == 11) {
//                        botReady = true;
//                    }
//                }
//
//                //System.out.println("countForSensorsReady = " + countForSensorsReady);
//
//                //}
//
//            }
//        }
//
//        /*
//        if (!findFlag && ballStop) {
//
//            canTranslateCam = false;
//
//            if (bestSensor == 5) {
//                findFlag = true;
//            }
//
//            if (countForFlag < 1) {
//                minDistanceArrowFlag = euclideanDistFlag(ballPositionX, ballPositionZ);
//            }
//
//            countForFlag++;
//
//            float timePeriod = 2f;
//            float camVelocity = 5f;
//
//            if (botTimer1 < timePeriod) {
//
//                if (euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ) < minDistanceArrowFlag) {
//
//                    minDistanceArrowFlag = euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ);
//
//                    camDirectionFlagX = camera.direction.x;
//                    camDirectionFlagY = camera.direction.y;
//                    camDirectionFlagZ = camera.direction.z;
//
//                    camUpFlagX = camera.up.x;
//                    camUpFlagY = camera.up.y;
//                    camUpFlagZ = camera.up.z;
//
//                    camPositionFlagX = camera.position.x;
//                    camPositionFlagY = camera.position.y;
//                    camPositionFlagZ = camera.position.z;
//
//                    sensorsAngleX[5] = camDirectionFlagX;
//                    sensorsAngleY[5] = camDirectionFlagY;
//                    sensorsAngleZ[5] = camDirectionFlagZ;
//
//                    sensorUpX[5] = camUpFlagX;
//                    sensorUpY[5] = camUpFlagY;
//                    sensorUpZ[5] = camUpFlagZ;
//
//                    sensorPositionX[5] = camDirectionFlagX;
//                    sensorPositionY[5] = camDirectionFlagY;
//                    sensorPositionZ[5] = camDirectionFlagZ;
//                }
//
//                camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                        vector2 = new Vector3(0f, 1f, 0f), camVelocity);
//                camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//
//                botTimer1 += Gdx.graphics.getDeltaTime();
//            }
//
//            if (botTimer1 > timePeriod && botTimer2 < timePeriod) {
//
//                if (euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ) < minDistanceArrowFlag) {
//
//                    minDistanceArrowFlag = euclideanDistFlag(finalPositionArrowX, finalPositionArrowZ);
//
//                    camDirectionFlagX = camera.direction.x;
//                    camDirectionFlagY = camera.direction.y;
//                    camDirectionFlagZ = camera.direction.z;
//
//                    camUpFlagX = camera.up.x;
//                    camUpFlagY = camera.up.y;
//                    camUpFlagZ = camera.up.z;
//
//                    camPositionFlagX = camera.position.x;
//                    camPositionFlagY = camera.position.y;
//                    camPositionFlagZ = camera.position.z;
//
//                    sensorsAngleX[5] = camDirectionFlagX;
//                    sensorsAngleY[5] = camDirectionFlagY;
//                    sensorsAngleZ[5] = camDirectionFlagZ;
//
//                    sensorUpX[5] = camUpFlagX;
//                    sensorUpY[5] = camUpFlagY;
//                    sensorUpZ[5] = camUpFlagZ;
//
//                    sensorPositionX[5] = camDirectionFlagX;
//                    sensorPositionY[5] = camDirectionFlagY;
//                    sensorPositionZ[5] = camDirectionFlagZ;
//                }
//
//                camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                        vector2 = new Vector3(0f, 1f, 0f), -camVelocity);
//                camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//
//                botTimer2 += Gdx.graphics.getDeltaTime();
//            }
//
//            if (botTimer1 > timePeriod && botTimer2 > timePeriod) {
//
//                camera.direction.x = camDirectionFlagX;
//                camera.direction.y = camDirectionFlagY;
//                camera.direction.z = camDirectionFlagZ;
//
//                camera.up.x = camUpFlagX;
//                camera.up.y = camUpFlagY;
//                camera.up.z = camUpFlagZ;
//
//                camera.position.x = camPositionFlagX;
//                camera.position.y = camPositionFlagY;
//                camera.position.z = camPositionFlagZ;
//
//                checkForSensors = true;
//                findFlag = true;
//            }
//        }
//        */
//
//        /*
//
//        float timePeriod = 2f;
//        float camVelocity = 7f;
//
//        if (sensorsReady) {
//
//            if (countForSensors < 1) {
//                for (int j = 0; j < sensorsSize.length; j++) {
//                    minEuclideanDist[j] = 100;
//                }
//            }
//
//            countForSensors++;
//
//            modelBatch.render(sensors, environment);
//
//            if (botTimer1 < timePeriod) {
//
//                for (int i = 0; i < sensorsSize.length; i++) {
//
//
//                    if (i==0 || i==10) {
//                    System.out.println("i = " + i);
//                    System.out.println("real dist = " + euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i));
//                    System.out.println("theoric dist  = " + minEuclideanDist[i]);
//                    System.out.println();
//                    }
//
//
//                    if (euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i) < minEuclideanDist[i]) {
//
//
//                        if (i==0 || i==10) {
//                            System.out.println("Is ok");
//                        }
//
//
//                        minEuclideanDist[i] = euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i);
//
//                        sensorsAngleX[i] = camera.direction.x;
//                        sensorsAngleY[i] = camera.direction.y;
//                        sensorsAngleZ[i] = camera.direction.z;
//
//                        sensorUpX[i] = camera.up.x;
//                        sensorUpY[i] = camera.up.y;
//                        sensorUpZ[i] = camera.up.z;
//
//                        sensorPositionX[i] = camera.position.x;
//                        sensorPositionY[i] = camera.position.y;
//                        sensorPositionZ[i] = camera.position.z;
//                    }
//                }
//
//                camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                        vector2 = new Vector3(0f, 1f, 0f), camVelocity);
//                camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//
//                botTimer1 += Gdx.graphics.getDeltaTime();
//            }
//
//            if (botTimer1 > timePeriod && botTimer2 < timePeriod+0.5f) {
//
//                for (int i = 0; i < sensorsSize.length; i++) {
//
//                    if (euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i) < minEuclideanDist[i]) {
//
//                        minEuclideanDist[i] = euclideanDistSensors(finalPositionArrowX, finalPositionArrowZ, i);
//
//                        sensorsAngleX[i] = camera.direction.x;
//                        sensorsAngleY[i] = camera.direction.y;
//                        sensorsAngleZ[i] = camera.direction.z;
//
//                        sensorUpX[i] = camera.up.x;
//                        sensorUpY[i] = camera.up.y;
//                        sensorUpZ[i] = camera.up.z;
//
//                        sensorPositionX[i] = camera.position.x;
//                        sensorPositionY[i] = camera.position.y;
//                        sensorPositionZ[i] = camera.position.z;
//                    }
//                }
//
//                camera.rotateAround(vector1 = new Vector3(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ),
//                        vector2 = new Vector3(0f, 1f, 0f), -camVelocity);
//                camera.lookAt(ballPositionX, defineFunction(ballPositionX, ballPositionZ), ballPositionZ);
//
//                botTimer2 += Gdx.graphics.getDeltaTime();
//            }
//
//            if (botTimer1 > timePeriod && botTimer2 > timePeriod+0.5f) {
//
//                // To comment
//                camera.direction.x = camDirectionFlagX;
//                camera.direction.y = camDirectionFlagY;
//                camera.direction.z = camDirectionFlagZ;
//
//                camera.up.x = camUpFlagX;
//                camera.up.y = camUpFlagY;
//                camera.up.z = camUpFlagZ;
//
//                camera.position.x = camPositionFlagX;
//                camera.position.y = camPositionFlagY;
//                camera.position.z = camPositionFlagZ;
//
//
//                botReady = true;
//                sensorsReady = false;
//            }
//        }
//
//        */
//
//        if (botReady) {
//
//            for (int i = 0; i < PuttingGameScreen.getSensorsSize().length; i++) {
//                for (int j = 0; j < 10; j++) {
//                    int stepIndex = (i*10)+j;
//                    float posX = 0;
//                    float posZ = 0;
//                    float[] stepOutput = new float[7];
//                    boolean[] stepCollision = new boolean[10];
//                    for (int k = 0; k < stepOutput.length; k++) {
//                        if (k==0) {
//                            stepOutput[k] = sensorsAngleX[i];
//                        }
//                        if (k==1) {
//                            stepOutput[k] = sensorsAngleZ[i];
//                        }
//                        if (k==2) {
//                            /*
//                            System.out.println("not ok");
//                            System.out.println("sensorsAngleX = " + Arrays.toString(sensorsAngleX));
//                            System.out.println("sensorsAngleZ = " + Arrays.toString(sensorsAngleZ));
//                            System.out.println("sensorsAngleX[i] = " + sensorsAngleX[i]);
//                            System.out.println("sensorsAngleZ[i] = " + sensorsAngleZ[i]);
//                            System.out.println("stepPositionX = " + stepPositionX[stepIndex]);
//                            System.out.println("stepPositionZ = " + stepPositionZ[stepIndex]);
//                            */
//
//
//                            /*
//                            System.out.println("stepPositionX = " + stepPositionX[stepIndex]);
//                            System.out.println("stepPositionZ = " + stepPositionZ[stepIndex]);
//                            System.out.println("sensorsAngleX = " + sensorsAngleX[i]);
//                            System.out.println("sensorsAngleZ = " + sensorsAngleZ[i]);
//                            */
//
//                            //System.out.println("stuck");
//
//                            /*
//                            System.out.println("ballPositionX = " + ballPositionX);
//                            System.out.println("ballPositionZ = " + ballPositionZ);
//                            */
//
//                            /*
//                            System.out.println("stepPositionX = " + Arrays.toString(stepPositionX));
//                            System.out.println("stepPositionZ = " + Arrays.toString(stepPositionZ));
//                            System.out.println("stepPositionX current = " + stepPositionX[stepIndex]);
//                            System.out.println("stepPositionZ current = " + stepPositionZ[stepIndex]);
//                            System.out.println("sensorsAngleX = " + Arrays.toString(sensorsAngleX));
//                            System.out.println("sensorsAngleZ = " + Arrays.toString(sensorsAngleZ));
//                            System.out.println("sensorsAngleX current = " + sensorsAngleX[i]);
//                            System.out.println("sensorsAngleZ current = " + sensorsAngleZ[i]);
//                            System.out.println();
//                            */
//
//                            System.out.println("step x = " + PuttingGameScreen.getStepPositionX()[stepIndex]);
//                            System.out.println("step z = " + PuttingGameScreen.getStepPositionZ()[stepIndex]);
//
//
//                            float velocity = PuttingGameScreen.evaluatePowerRK4(ballPositionX,ballPositionZ,PuttingGameScreen.getStepPositionX()[stepIndex],PuttingGameScreen.getStepPositionZ()[stepIndex],sensorsAngleX[i],sensorsAngleZ[i]);
//                            stepOutput[k] = velocity;
//
//                            //System.out.println("ok");
//                        }
//                        if (k==3) {
//
//                            /*
//                            Rungekuttasolver RK4 = new Rungekuttasolver();
//
//                            RK4.setValues(ballPositionX, ballPositionZ, sensorsAngleX[i]*stepOutput[k-1], sensorPositionZ[i]*stepOutput[k-1]);
//
//                            RK4.RK4();
//
//                            posX = (float) RK4.getX();
//                            posZ = (float) RK4.getY();
//
//                            System.out.println("posX = " + posX);
//                            System.out.println("posZ = " + posZ);
//                            System.out.println();
//                            */
//
//                            posX = PuttingGameScreen.getStepPositionX()[stepIndex];
//                            posZ = PuttingGameScreen.getStepPositionZ()[stepIndex];
//
//                            for (int l = 0; l < PuttingGameScreen.getNumberOfTree(); l++) {
//                                if (PuttingGameScreen.euclideanDistObstacles(posX, posZ, l) < 0.5f) {
//                                    stepOutput[k] = 1;
//                                    stepCollision[j] = true;
//                                }
//                                else {
//                                    stepOutput[k] = 0;
//                                    stepCollision[j] = false;
//                                }
//                            }
//
//                            if (PuttingGameScreen.defineFunction(posX, posZ) < 0.05f) {
//                                stepOutput[k] = 1;
//                                stepCollision[j] = true;
//                            }
//                            else {
//                                stepOutput[k] = 0;
//                                stepCollision[j] = false;
//                            }
//
//                            if (PuttingGameScreen.outOfField(posX, posZ)) {
//                                stepOutput[k] = 1;
//                                stepCollision[j] = true;
//                            }
//                            else {
//                                stepOutput[k] = 0;
//                                stepCollision[j] = false;
//                            }
//                        }
//                        if (k==4) {
//                            if (stepCollision[j]) {
//                                stepOutput[k] = 0;
//                            }
//                            else {
//                                if (PuttingGameScreen.isWin(posX, posZ)) {
//                                    stepOutput[k] = 1;
//                                }
//                            }
//                        }
//                        if (k==5) {
//                            stepOutput[k] = posX;
//                        }
//                        if (k==6) {
//                            stepOutput[k] = posZ;
//                        }
//                    }
//                    //System.out.println("stepData = " + Arrays.toString(stepData));
//                    //System.out.println("stepOutput = " + Arrays.toString(stepOutput));
//
//                    sensorsOutput.add(stepOutput);
//                    //sensorsData.add(stepData);
//                }
//            }
//            isBotReady = true;
//
//            /*
//            for (int i = 0; i < sensorsOutput.size(); i++) {
//                System.out.println("sensorsOutput = " + Arrays.toString(sensorsOutput.get(i)));
//            }
//            */
//
//        }
//    }
//
//    public void resetValues() {
//
//        PuttingGameScreen.sumX = 0;
//        PuttingGameScreen.sumZ = 0;
//        PuttingGameScreen.countIterationBot = 0;
//
//        count = 0;
//        counter = 0;
//        botTimer1 = 0;
//        botTimer2 = 0;
//        countForSensors = 0;
//        countForSensorsReady = 0;
//
//        check = false;
//        botReady = false;
//        isBotReady = false;
//        findFlag = false;
//        checkCamera = false;
//
//        countForFlag = 0;
//
//        PuttingGameScreen.isSensorOnSand = new boolean[11];
//        checkForSensorsStep = new boolean[11];
//    }
//
//    public void startBot() {
//
//        countIndex++;
//        PuttingGameScreen.ballStop = false;
//
//        //System.out.println("power x = " + Arrays.toString(game.getMaxPositionAgentX()));
//        //System.out.println("power z = " + Arrays.toString(game.getMaxPositionAgentZ()));
//        AgentBot bot = new AgentBot(PuttingGameScreen.getSensorsSize(), sensorsAngleX, sensorsAngleZ, PuttingGameScreen.getMaxPositionAgentX(), PuttingGameScreen.getMaxPositionAgentZ(), PuttingGameScreen.getCanHitFlag(), PuttingGameScreen.getIsSensorOnSand(), ballPositionX, ballPositionZ);
//
//        newPositions = bot.startBot();
//
//        PuttingGameScreen.agentPower = bot.getPowerScalar();
//        //System.out.println("agentPower = " + agentPower);
//
//        bestSensor = bot.getBestSensor();
//
//        camera.direction.x = sensorsAngleX[bestSensor];
//        camera.direction.y = sensorsAngleY[bestSensor];
//        camera.direction.z = sensorsAngleZ[bestSensor];
//
//        camera.up.x = sensorUpX[bestSensor];
//        camera.up.y = sensorUpY[bestSensor];
//        camera.up.z = sensorUpZ[bestSensor];
//
//        camera.position.x = sensorPositionX[bestSensor];
//        camera.position.y = sensorPositionY[bestSensor];
//        camera.position.z = sensorPositionZ[bestSensor];
//
//        //sensorsAngleX = new float[sensorsSize.length];
//        //sensorsAngleZ = new float[sensorsSize.length];
//
//        newBallPositionX = newPositions[0];
//        newBallPositionZ = newPositions[1];
//
//        PuttingGameScreen.canTranslateCam = true;
//
//        PuttingGameScreen.positionArrayX[countIndex] = newBallPositionX;
//        PuttingGameScreen.positionArrayZ[countIndex] = newBallPositionZ;
//
//        PuttingGameScreen.ballPositionX = PuttingGameScreen.positionArrayX[countIndex - 1];
//        PuttingGameScreen.ballPositionZ = PuttingGameScreen.positionArrayZ[countIndex - 1];
//
//        int ballStep = 100;
//
//        PuttingGameScreen.ballStepXmean = (PuttingGameScreen.getPositionArrayX()[countIndex] - PuttingGameScreen.getPositionArrayX()[countIndex - 1]) / ballStep;
//        PuttingGameScreen.ballStepZmean = (PuttingGameScreen.getPositionArrayZ()[countIndex] - PuttingGameScreen.getPositionArrayZ()[countIndex - 1]) / ballStep;
//    }
//
//    public ArrayList<float[]> getSensorsOutput() {
//        return sensorsOutput;
//    }
//}
