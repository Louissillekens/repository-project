package code.astar;


/**
 * this class tests various functions of the Node class
 * an attempt was made to do unit tests for this but with the entire LibGDX setup
 * making sure the dependencies worked was quite confusing
 */
public class NodeTest {


    public static void main(String[] args) {

        //made a few nodes to test with
        Node parent = new Node(null, 1,1);
        Node node = new Node(parent, 2,2);
        Node child1 = new Node(node,1,2);
        Node child2 = new Node(node,1.5,3);

        child1.generateShot(0.25,3);
        System.out.println(child1.getPower() + " ------ " + child1.getAngle());
        double[] nextLoc = child1.executeShot();
        System.out.println("the next location is at : (" + nextLoc[0] + ", " + nextLoc[1] + ")");
    }
}
