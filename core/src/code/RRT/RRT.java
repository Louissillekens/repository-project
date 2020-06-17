package code.RRT;

import java.util.Arrays;

public class RRT {

    //TODO sync pos smaller bfs with actual terrain
    int xDomain = 111;
    int yDomain = 111;
    int xFlag = 99;
    int yFlag = 58;


    int[][] grid;

    public  int function(float x, float y, String terrain){
        if (terrain == "flat")
            return 1;
        else{
            float value =  (float)(((Math.sin(x) + Math.sin(y))/4));
            // TODO Should also check if there is an object at that place
            if(value < 0){
                return 0;
            }
            return 1;
        }
    }

    public  void genGrid(double stepsize, String terrain){
        int aurgemtedStepsize = (int) Math.pow(stepsize, -1);

        grid = new int[yDomain*aurgemtedStepsize][xDomain*aurgemtedStepsize];

        //Creates a binary grid with 0 for obstacles
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                grid[i][j] = function(j,i,terrain);
                System.out.println(grid[i][j]);
            }
        }
        System.out.println("ok");

        //Creates a distance grid with BFS alg
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                Pointer source = new Pointer(i, j);
                Pointer dest = new Pointer(xFlag, yFlag);

                BFS cell = new BFS(grid, source, dest);

                grid[i][j] = cell.bfs();
            }
        }

    }

    public static void main(String[] args) {
        RRT bot = new RRT();
        bot.genGrid( 1, "flat");

        for (int i = 0; i < bot.grid.length; i++)
            System.out.println(Arrays.deepToString(new int[][]{bot.grid[i]}));
    }
}
