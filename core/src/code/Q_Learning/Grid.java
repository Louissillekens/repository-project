package code.Q_Learning;

public class Grid {
    int R[][];
    int Q[][];

    int precision;

    int xTarget;
    int yTarget;


    Grid(int x, int y, int precision, int xTarget, int yTarget){

        this.precision = precision;

        this.R = new int[y*precision][x*precision];

        this.xTarget = xTarget*precision;
        this.yTarget = yTarget*precision;
    }

    public void createR(){
        for (int i = 0; i < R.length; i++){
            for (int j = 0; j < R[i].length; j++){

                // Checks if the coordinate is a playable place
                R[i][j] = function(j/precision,i/precision);
                //TODO: needs the information where obstacles have been placed

                // The reward the agent receives when he finds the hole
                if (i == yTarget && j == xTarget){
                    R[i][j] = 100;
                }
            }
        }
    }

    public void createQ(){
        Q = new int[R.length][R[0].length]; // Initialised with all 0's
    }

    public  int function(float x, float y){

        float value =  (float) (((Math.sin(x) + Math.sin(y))/8)+0.4);

        if(value < 0){
            return -1;
        }
        // TODO Should also check if there is an object at that place, if yes return -1, otherwise return 0
        return 0;
    }

    public int[][] getR() {
        return R;
    }

    public int[][] getQ() {
        return Q;
    }

    public int getxTarget() {
        return xTarget;
    }

    public int getyTarget() {
        return yTarget;
    }

    public int getPrecision() {
        return precision;
    }
}
