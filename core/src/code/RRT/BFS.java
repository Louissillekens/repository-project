package code.RRT;

import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    // For a cell 'A' :Used to get the row and the column numbers of the 4 neighbours of 'A'
    int rowN[] = {-1, 0, 0, 1};
    int colN[] = {0, -1, 1, 0};

    int grid [][];

    Pointer init;
    Pointer finalDest;

    public BFS(int[][] grid, Pointer init, Pointer finalDest) {
        this.grid = grid;
        this.init = init;
        this.finalDest = finalDest;
    }

    // Check if the cell is a an obstacle or not and if it's in the grid
    public boolean cellIsValid(int row, int column){
        return (row >= 0) && (row < grid.length) && (column >= 0) && (column < grid[0].length);
    }



    //init is the initial pos of the cell, finalDest is the final destination pos of the cell
    public int bfs(){

        // Check if the nxt destination of the cell is valid (=1)
        if (grid[init.x][init.y] != 1 || grid[finalDest.x][finalDest.y] != 1) {
            //System.out.println(grid[init.x][init.y] + "  " + grid[finalDest.x][finalDest.y]);
            return -1;
        }

        boolean [][]visited = new boolean[grid.length][grid[0].length];

        // Mark cell as visited
        visited[init.x][init.y] = true;

        // Create a queue
        Queue<NodeTracking> queue = new LinkedList<>();

        // If the distance of source cell = 0
        NodeTracking s = new NodeTracking(init, 0);
        queue.add(s); // add the source cell tp the queue

        // BFS starting from init cell
        while (!queue.isEmpty()){

            NodeTracking curr = queue.peek();
            Pointer pointer = curr.pointer;

            // Destination cell reached
            if (pointer.x == finalDest.x && pointer.y == finalDest.y)
                return curr.distance;

            // If not reached:
            // 1) dequeue the front cell that's in the queue
            // 2) enqueue the adjacent cells of that cell
            queue.remove();

            for (int i = 0; i < 4; i++){
                int row = pointer.x + rowN[i];
                int column = pointer.y + colN[i];

                // Adjacent cell is:
                // 1) valid
                // 2) has a path
                // 3) has not been visited yet
                // => enqueue the cell.

                if (cellIsValid(row, column) && grid[row][column] == 1 && !visited[row][column]){
                    // mark cell as visited + enqueue the cell
                    visited[row][column] = true;
                    NodeTracking Adjacentcell = new NodeTracking(new Pointer(row, column), curr.distance + 1 );
                    queue.add(Adjacentcell);
                }
            }
        }

        // Destination can't be reached
        return -2;
    }
}
