package code.astar;

import code.Screens.PuttingGameScreen;

import java.util.ArrayList;
import java.util.List;

public class AStar {

    private List<Node> nodes;
    private PuttingGameScreen game;


    private final int amOfNodes = 10; //the amount of nodes we generate from a node each time

    //temp set to 0.5, these final doubles are here to work as a factor in computing the score of a node
    private final double distanceHeuristic = 0.25;
    private final double stepHeuristic = 2;

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
                nodes.add(cloneNode);
                //each iteration create a random shot and make a node of where the ball arrives
                cloneNode.generateShot( 0.25, game.getMAX_SPEED()); //min is set to 0.5 as values lower than this will not make the ball move all that much
                double[] locData = cloneNode.executeShot();
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

        double score = 100;
        score -= distanceHeuristic*(node.CalculateDistTo(game.getFlagPositionX(), game.getFlagPositionZ()));
        //System.out.println(score);
        score -= stepHeuristic*amOfSteps(node);
        //System.out.println(score);
        node.setScore(score);
    }

    /**
     * puts all the nodes which lead to the solution in the right order in a list
     * @param node the node for which we want to know the path to from its root
     * @return a list containing all nodes we pass from root to node (root and node included)
     */
    public List<Node> findRoadTo(Node node){

        ArrayList<Node> list = new ArrayList<Node>();
        //we don't add the last node as it contains the location and is not an actual shot to be taken
        list.add(node);

        while(node.hasParent()){
            //adds the node's parent to the start of the list and shifts existing nodes to the right
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

    /**
     * checks whether a given node is within the reach of the goal (a.k.a. we found a solution)
     * @param node the node to be checked
     * @return a boolean indicating whether the node is in range
     */
    public boolean withinRangeOfGoal(Node node){

        if(game.isWin((float)node.getX(), (float)node.getZ())) return true;
        return false;
    }

    /**
     * do one iteration of the A* algorithm
     * @return a boolean whether an iteration can be executed, returns false when a solution is found
     */
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

    /**
     * the running method of the algorithm
     * creates the initial node and then executes iterations until a solution is found
     * @return a List of nodes that forms the solution
     */
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

    /**
     * method that will find how many steps were taken to get to this node
     * @param node given node
     * @return int holding the amount of shots taken to get here
     */
    public int amOfSteps(Node node){

        int steps = 0;

        //we take into account that the last node is not a shot that is taken
        //rather it holds the location of where the last shot ended, thus does not have to be taken into account
        while(node.hasParent()){

            steps += 1;
            node = node.getParent();
        }

        return steps;
    }

    /**
     * getter for the amOfNodes variable
     * this variable holds how many new nodes we create for each layer of the algorithm
     * @return an int containing the amount of nodes
     */
    public int getAmOfNodes(){
        return amOfNodes;
    }
}
