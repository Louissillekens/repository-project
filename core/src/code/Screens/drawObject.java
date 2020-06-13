package code.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

public class drawObject {

    public ModelInstance drawRectangle(float xPosition, float zPosition, float width, float height, float depth, Color color) {

        Model model = PuttingGameScreen.getModelBuilder().createBox(width, height, depth,
                new Material(ColorAttribute.createDiffuse(color)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        return new ModelInstance(model, xPosition, PuttingGameScreen.defineFunction(xPosition, zPosition), zPosition);
    }

    public ModelInstance[] drawTree(float xPosition, float zPosition) {

        Model trunk = PuttingGameScreen.getModelBuilder().createCylinder(0.5f, 6, 0.5f, 20,
                new Material(ColorAttribute.createDiffuse(Color.BROWN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ModelInstance trunkInstance = new ModelInstance(trunk, xPosition, PuttingGameScreen.defineFunction(xPosition, zPosition), zPosition);

        Model branches = PuttingGameScreen.getModelBuilder().createCone(2,3,2,20,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ModelInstance branchesInstance = new ModelInstance(branches, xPosition, PuttingGameScreen.defineFunction(xPosition, zPosition)+3.5f, zPosition);

        return new ModelInstance[]{trunkInstance, branchesInstance};
    }
}
