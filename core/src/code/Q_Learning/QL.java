package code.Q_Learning;

import java.util.Arrays;

public class QL {
    static int xStart = 2;
    static int yStart = 2;

    static int xFlag = 5;
    static int yFlag = 7;

    static int precision = 2; // 1 square will be traversed in 2 steps: 0.5/0.5

    static int maxEp = 200;


    public static void main(String[] args) {
        Grid grid = new Grid(xFlag,yFlag,precision,2,5);
        grid.createR();
        grid.createQ();

        Agent agent = new Agent(xStart, yStart, precision, grid);

        int episode = 0;
        while (episode < maxEp){
            while (compareState() == false){

            }
            agent.reset(xStart, yStart,precision);
            episode++;
        }



    }

    //Checks whether the next state reached its destination
    private static boolean compareState() {
        int [] flag = {xFlag, yFlag};
        int [] nextState = {};

        return Arrays.equals(flag, nextState);
    }
}
