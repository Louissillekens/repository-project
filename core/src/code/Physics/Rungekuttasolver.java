package code.Physics;

import code.Screens.PuttingGameScreen;

public class Rungekuttasolver{

    private double g = 9.81;
    private double dx = 0.01;
    private double dy = 0.01;
    private double dt = 0.01;
    private double mu;
    private double vx;
    private double vy;
    private double x;
    private double y;
    private final double onesixth =(double)1/6;
    private static boolean hasStopped;

    public static void main(String args[]){

        Rungekuttasolver solver = new Rungekuttasolver();
        solver.setValues(0,0,3.9726,8.519);
        int i = 0;
        while (!hasStopped) {
            solver.RK4();
            System.out.println("x: "+solver.getX());
            System.out.println("y: "+solver.getY());
            System.out.println();
            System.out.println("vx: " + solver.getVx());
            System.out.println("vy: " + solver.getVy());
            if (solver.getVx() < 0.1 && solver.getVy() < 0.1) {
                System.out.println("i = " + i);
            }
            i++;
        }
        System.out.println("the x and y coordinates are:");
        System.out.println("x: "+solver.getX());
        System.out.println("y: "+solver.getY());
    }

    public void setValues(double xi,double yi, double vxi, double vyi){

        x = xi;
        y = yi;
        vx = vxi;
        vy = vyi;
    }

    //PLEASE DO NOT TOUCH, IT'S for the AI
    public double[] startRK4(double xPos,double yPos, double xV, double yV){

        Rungekuttasolver solver = new Rungekuttasolver();
        solver.setValues(xPos,yPos,xV,yV);

        //Run the solver
        for (int i=0; i<6000; i++) {
            solver.RK4();
        }

        return new double[] {solver.getX(), solver.getY()};
    }

    public boolean hasBallStopped(){

        hasStopped =false;
        if(vx==0 && vy==0 /*&& ax==0 && ay == 0*/){
            hasStopped = true;
        }
        return hasStopped;
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

    public void RK4(){
        double k1x  = vx;
        double k1y  = vy;
        double k1vx = getFHeightX(x,y) + getFFrictionX(vx,vy,x,y);
        double k1vy = getFHeightY(x,y) + getFFrictionY(vx,vy,x,y);

        double k2x  = vx + 0.5*dt*k1vx;
        double k2y  = vy + 0.5*dt*k1vy;
        double k2vx = getFHeightX(x+0.5*dt*k1x, y+0.5*dt*k1y) + getFFrictionX(vx+0.5*dt*k1vx, vy+0.5*dt*k1vy,x+0.5*dt*k1x, y+0.5*dt*k1y);
        double k2vy = getFHeightY(x+0.5*dt*k1x, y+0.5*dt*k1y) + getFFrictionY(vx+0.5*dt*k1vx, vy+0.5*dt*k1vy,x+0.5*dt*k1x, y+0.5*dt*k1y);

        double k3x  = vx + 0.5*dt*k2vx;
        double k3y  = vy + 0.5*dt*k2vy;
        double k3vx = getFHeightX(x+0.5*dt*k2x, y+0.5*dt*k2y) + getFFrictionX(vx+0.5*dt*k2vx, vy+0.5*dt*k2vy,x+0.5*dt*k2x, y+0.5*dt*k2y);
        double k3vy = getFHeightY(x+0.5*dt*k2x, y+0.5*dt*k2y) + getFFrictionY(vx+0.5*dt*k2vx, vy+0.5*dt*k2vy,x+0.5*dt*k2x, y+0.5*dt*k2y);

        double k4x  = vx + dt*k3vx;
        double k4y  = vy + dt*k3vy;
        double k4vx = getFHeightX(x+dt*k3x, y+dt*k3y) + getFFrictionX(vx+dt*k3vx, vy+dt*k3vy,x+dt*k3x, y+dt*k3y);
        double k4vy = getFHeightY(x+dt*k3x, y+dt*k3y) + getFFrictionY(vx+dt*k3vx, vy+dt*k3vy,x+dt*k3x, y+dt*k3y);

        double vxa  = vx + onesixth*dt*(k1vx +2*k2vx +2*k3vx +k4vx);
        double vya  = vy + onesixth*dt*(k1vy +2*k2vy +2*k3vy +k4vy);
        double xa   = x  + onesixth*dt*(k1x  +2*k2x  +2*k3x  +k4x );
        double ya   = y  + onesixth*dt*(k1y  +2*k2y  +2*k3y  +k4y );
        setValues(xa,ya,vxa,vya);

    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
    
    public double getVx(){
        return vx;
    }

    public double getVy(){
        return vy;
    }

}