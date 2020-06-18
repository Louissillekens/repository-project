package code.astar;

import code.Screens.PuttingGameScreen;

import java.util.ArrayList;
import java.util.List;

public class AStar {

    private List<Node> nodes;
    private PuttingGameScreen game;


    private final int amOfNodes = 10; //the amount of nodes we generate from a node each time

    //temp set to 0.5, these final doubles are here to work as a factor in computing the score of a node
    private final double distanceHeuristic = 0.5;
    private final double stepHeuristic = 0.5;

    public AStar(PuttingGameScreen game){

        this.game = game;
        nodes = new ArrayList<Node>();
    }

    /**
     * creates the first node as a node with no parent and the position as the ball is
     */
    public void makeInitialNode(){
        nodes.add(new Node(null, game.getBallPositionX(), game.getBallPositionZ()));
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

        //make sure that we didn't generate nodes already
        if(!node.isChecked()){
            int count = 0;
            while(count < amOfNodes){

                Node cloneNode = node.partialClone();
                //each iteration create a random shot and make a node of where the ball arrives
                cloneNode.generateShot();
                double[] locData = node.executeShot();
                Node nextNode = new Node(cloneNode, locData[0], locData[1]);
                this.computeScore(nextNode);
                this.addNode(nextNode);
                cloneNode.setChecked(true);
                count++;
            }

            //TODO make a node that shoots directly towards the goal as well instead of removing the original
            nodes.remove(node);
        }
    }

    /**
     * compute and set the score of the given node according to the heuristics
     * @param node given Node on the field
     */
    public void computeScore(Node node){
        //TODO we must decide what our heuristics are
        //we compute the score and add set this in the node
    }

    /**
     *
     * @param node //TODO
     * @return
     */
    public List<Node> findRoadTo(Node node){

        ArrayList<Node> list = new ArrayList<Node>();
        list.add(node);

        while(node.hasParent()){
            //adds the node to the start of the list and shifts existing nodes to the right
            list.add(0,node.getParent());
            node = node.getParent();
        }

        return list;
    }

    /**
     * find the best node from the list that is not checked yet
     * @return the best scoring node from the list, must be unchecked
     */
    public Node chooseBestNode(){

        Node best = new Node(0);

        for(Node node : nodes){
            if(!node.isChecked()){
                if(node.getScore() > best.getScore()) best = node;
            }
        }

        return best;
    }

    public boolean withinRangeOfGoal(Node node){

        //TODO check if the node is within the range of the goal

        //temp return
        return false;
    }

    public boolean doIteration(){

        Node best = this.chooseBestNode();

        if(withinRangeOfGoal(best)){
            return false;
        }

        else{
            this.generateNodes(best);
            return true;
        }
    }

    public List<Node> findRoute(){

        makeInitialNode();
        List<Node> answer = new ArrayList<Node>();

        boolean inProgress = true;

        while(inProgress){

            if(!doIteration()){
                Node best = this.chooseBestNode();
                answer = this.findRoadTo(best);
                inProgress = false;
            }
        }
        return answer;
    }

    public int getAmOfNodes(){
        return amOfNodes;
    }
}
