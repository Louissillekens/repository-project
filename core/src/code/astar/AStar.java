package code.astar;

import code.Screens.PuttingGameScreen;

import java.util.ArrayList;
import java.util.List;

public class AStar {

    private List<Node> nodes;
    //still need acces to the data of the field (sizes, objects, location of ball and goal)
    private final float ballPositionX;
    private final float ballPositionZ;
    private final float flagPositionX;
    private final float flagPositionZ;

    //temp set to 0.5, these final doubles are here to work as a factor in computing the score of a node
    private final double distanceHeuristic = 0.5;
    private final double stepHeuristic = 0.5;

    public AStar(PuttingGameScreen game){
        //set the course
        ballPositionX = game.getBallPositionX();
        ballPositionZ = game.getBallPositionZ();
        flagPositionX = game.getFlagPositionX();
        flagPositionZ = game.getFlagPositionZ();
        nodes = new ArrayList<Node>();
    }

    /**
     * creates the first node as a node with no parent and the position as the ball is
     */
    public void makeInitialNode(){
        nodes.add(new Node(null, ballPositionX, ballPositionZ));
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public void addNodes(List<Node> more_nodes){
        for(Node node : more_nodes){
            nodes.add(node);
        }
    }

    /**
     * from the given node make ten new random nodes and add to list
     * @param node the node from which to start
     */
    public void generateNodes(Node node){
        //TODO
    }

    /**
     * compute and set the score of the given node according to the heuristics
     * @param node given Node on the field
     */
    public void computeScore(Node node){
        //TODO
    }

    public List<Node> findRoadTo(Node node){
        //TODO go back through the parents of this node to the root and put them in a list to find the road we take
        return new ArrayList<Node>();
    }

    public void doIteration(Node node){
        //TODO do one iteration of the algorithm
    }
}
