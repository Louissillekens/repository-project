package code.testing_stuff;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;


import java.util.HashMap;
import java.util.Map;

public class CrazyPutting implements ApplicationListener {
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Model box;
    private Model shadow;
    private Model ball;
    private Model rect;

    private float ballRadius=0.1f;
    private ModelInstance boxInstance;
    private ModelInstance ballInstance;
    private ModelInstance shadowInstance;
    private ModelInstance rectInstance[];
    private MeshPartBuilder meshPartBuilder;
    private Environment environment;
    private float speed = 5f;
    private float ballVelocity = 0f;

    @Override
    public void create() {

        FunctionParser.parse("sin(x)+sin(y)");

        Map< String,Double> hm = new HashMap<String,Double>();
        hm.put("x", 3.14);
        hm.put("y", 3.14);
        //System.out.println(a.apply(hm));
        // Create camera sized to screens width/height with Field of View of 75 degrees
        camera = new PerspectiveCamera(
                75,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());

        // Move the camera 3 units back along the z-axis and look at the origin
        camera.position.set(3f, 3f, 3f);
        camera.lookAt(0f, 0f, 0f);

        // Near and Far (plane) represent the minimum and maximum ranges of the camera in, um, units
        camera.near = 0.1f;
        camera.far = 300.0f;

        // A ModelBatch is like a SpriteBatch, just for models.  Use it to batch up geometry for OpenGL
        modelBatch = new ModelBatch();

        // A ModelBuilder can be used to build meshes by hand
        ModelBuilder modelBuilder = new ModelBuilder();

        // It also has the handy ability to make certain premade shapes, like a Cube
        // We pass in a ColorAttribute, making our cubes diffuse ( aka, color ) red.
        // And let openGL know we are interested in the Position and Normal channels
        box = modelBuilder.createBox(20f, 1f, 20f,
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                Usage.Position | Usage.Normal
        );

        ball = modelBuilder.createSphere(ballRadius*2,ballRadius*2,ballRadius*2,10,10,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                Usage.Position | Usage.Normal
        );

        shadow = modelBuilder.createBox(ballRadius*2, 0.01f, ballRadius*2,
                new Material(ColorAttribute.createDiffuse(Color.BLACK)),
                Usage.Position | Usage.Normal
        );

        modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)));

        buildTerrain(builder);
        rect = modelBuilder.end();
        rectInstance = new ModelInstance [100];

        // A model holds all of the information about an, um, model, such as vertex data and texture info
        // However, you need an instance to actually render it.  The instance contains all the
        // positioning information ( and more ).  Remember Model==heavy ModelInstance==Light
        boxInstance = new ModelInstance(box, 0, -0.5f, 0);
        ballInstance = new ModelInstance(ball, 0, 1, 0);
        shadowInstance = new ModelInstance(shadow, 0, 0.5f, 0);
        for(int i=0;i<10;i++){
            for(int k=0;k<10;k++) {
                rectInstance[i*10+k] = new ModelInstance(rect, -i*50, 0, -k*50);
            }
        }

        // Finally we want some light, or we wont see our color.  The environment gets passed in during
        // the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1.f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -1.0f, 1f));
    }

    public static void buildTerrain(MeshPartBuilder b){
        int gridWidth=50;
        int gridDepth=50;
        float scale = 1f;
        Vector3 pos1,pos2,pos3,pos4;
        Vector3 nor1,nor2,nor3,nor4;
        VertexInfo v1,v2,v3,v4;
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

                v1 = new VertexInfo().setPos(pos1).setNor(nor1).setCol(null).setUV(0.5f, 0.0f);
                v2 = new VertexInfo().setPos(pos2).setNor(nor2).setCol(null).setUV(0.0f, 0.0f);
                v3 = new VertexInfo().setPos(pos3).setNor(nor3).setCol(null).setUV(0.0f, 0.5f);
                v4 = new VertexInfo().setPos(pos4).setNor(nor4).setCol(null).setUV(0.5f, 0.5f);

                b.rect(v1, v2, v3, v4);
            }
        }

        //nor2.sub(pos1).crs(nor4.sub(pos1)).toString();
        //System.out.println(nor2.toString());

//		VertexInfo v5 = new VertexInfo().setPos(1, 0, 0).setNor(0, 1, 0).setCol(null).setUV(0.5f, 0.0f);
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        box.dispose();
    }

    @Override
    public void render() {
        // You've seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT when working in 3D
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // For some flavor, lets spin our camera around the Y axis by 1 degree each time render is called
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            camera.rotateAround(Vector3.Zero, new Vector3(0, 1, 0), 1f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            camera.rotateAround(Vector3.Zero, new Vector3(0, -1, 0), 1f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            ballVelocity = 0f;
            ballInstance.transform.trn(0, speed * Gdx.graphics.getDeltaTime(), 0);
        } else if(ballInstance.transform.getTranslation(new Vector3()).y>ballRadius){
            ballVelocity+=9.81f*Gdx.graphics.getDeltaTime();
            ballInstance.transform.trn(0, -ballVelocity * Gdx.graphics.getDeltaTime(), 0);
        } else {
            ballVelocity=-ballVelocity*0.5f;
            if(ballVelocity*ballVelocity<0.3f) ballVelocity=0;
            ballInstance.transform.trn(0, -2.0f*ballVelocity * Gdx.graphics.getDeltaTime(), 0);
        }

//		if(ballInstance.transform.getTranslation(new Vector3()).y<ballRadius){
//			ballInstance.transform.setTranslation(new Vector3(ballInstance.transform.getTranslation(new Vector3()).x,ballRadius,ballInstance.transform.getTranslation(new Vector3()).z));
//		}

        shadowInstance.transform.setTranslation(new Vector3(-ballInstance.transform.getTranslation(new Vector3()).y,0,ballInstance.transform.getTranslation(new Vector3()).y));

        // When you change the camera details, you need to call update();
        // Also note, you need to call update() at least once.
        camera.update();
        // Like spriteBatch, just with models!  pass in the box Instance and the environment

        modelBatch.begin(camera);
        //modelBatch.render(boxInstance, environment);
        modelBatch.render(ballInstance, environment);
        modelBatch.render(shadowInstance, environment);
        for(int i=0;i<100;i++)
            modelBatch.render(rectInstance[i], environment);

        modelBatch.end();
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
}
