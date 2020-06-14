package code.Q_Learning;

public class Agent {
    int x;
    int y;

    int precision;

    Grid grid;

    Agent(int xStart, int yStart, int precision, Grid grid){
        this.precision = precision;

        this.x = xStart*precision;
        this.y = yStart*precision;

        this.grid = grid;
    }

    void update(int x, int y){
        this.x += x;
        this.y += y;
    }

    void reset(int xStart, int yStart, int precision){
        this.x = xStart*precision;
        this.y = yStart*precision;
    }

}
