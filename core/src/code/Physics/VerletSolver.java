package code.Physics;
import code.Screens.PuttingGameScreen;

public class VerletSolver{
    
    private double g = 9.81;
    private double dx = 0.01;
    private double dy = 0.01;
    private double dt = 0.01;
    private double mu;
    private double vx;
    private double vy;
    private double x;
    private double y;

    public void setValues(double xi,double yi, double vxi, double vyi){

        x = xi;
        y = yi;
        vx = vxi;
        vy = vyi;
    }
    public double getHeight(double x, double y){

        return PuttingGameScreen.defineFunction(x,y);
    }
    public double getResistance(double x, double y){

        return (double) PuttingGameScreen.getFriction(x,y);
    }
    //alternatively I could make an updateheight function that calculates x and y and stores them instead of 2 different methods  that return x and y, same for the resistance getters!
    public double getFHeightX(double x, double y){
        double height_acceleration = (g*((getHeight(x+dx,y)-getHeight(x,y))/dx))*-1;
        return height_acceleration;
    }
    public double getFHeightY(double x, double y){
        double height_acceleration = (g*((getHeight(x,y+dy)-getHeight(x,y))/dx))*-1;
        return height_acceleration;
    }
    public double getFFrictionX(double vx, double vy, double x, double y){
        double resistance_acceleration = ( getResistance(x,y)*g*(vx/Math.sqrt((vx*vx)+(vy*vy))) )*-1;
        return resistance_acceleration;
    }
    public double getFFrictionY(double vx, double vy, double x, double y){
        double resistance_acceleration = ( getResistance(x,y)*g*(vy/Math.sqrt(vx*vx+vy*vy)) )*-1;
        return resistance_acceleration;
    }
    public void Verlet(){
        double nax = getFHeightX(x,y)+ getFFrictionX(vx, vy, x, y);
        double nvx = vx + nax*dt;
        double nx = x + nvx*dt;

        double nay = getFHeightY(x,y)+ getFFrictionY(vx, vy, x, y);
        double nvy = vy + nay*dt;
        double ny = y + nvy*dt;

        setValues(nx,ny,nvx,nvy);


    }


    
}