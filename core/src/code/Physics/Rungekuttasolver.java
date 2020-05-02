package code.Physics;

import code.Board.Friction_function;
import code.Board.Height_function;
import code.Board.Vector2d;

public class Rungekuttasolver{
   double g= 9.81;
   double dx = 0.01;
   double dy = 0.01;
   double dt = 0.01;
   double mu;
   double vx=20;
   double vy=0;   
   double x = 0;
   double y = 0;

   public static void main(String args[]){
       Rungekuttasolver solver = new Rungekuttasolver();
       solver.RK4(5,5);//just testing if it might work with actual numbers as input
       solver.RK4(solver.getVX(), solver.getVY());
       solver.RK4(solver.getVX(), solver.getVY());
       solver.RK4(solver.getVX(), solver.getVY());
       solver.RK4(solver.getVX(), solver.getVY());
       solver.RK4(solver.getVX(), solver.getVY());
       solver.RK4(solver.getVX(), solver.getVY());
       solver.RK4(solver.getVX(), solver.getVY());
       solver.RK4(solver.getVX(), solver.getVY());
       solver.RK4(solver.getVX(), solver.getVY());

       System.out.println(solver.getX());
       System.out.println(solver.getY());
   }


   public boolean hasBallStopped(){
       boolean stopped =false;
       if(vx==0 && vy==0 /*&& ax==0 && ay == 0*/){
           stopped = true;
       }
       return stopped;
   }
   public double getHeight(double x, double y){
       //TO DO: make height change based on coordinates
       double height =1;
       return height;
   }
   public double getResistance(double x, double y){
       //TO DO: make reistance change based on coordinates
       double resistance = 1;
       return resistance;
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
   public double getFFrictionX(double x, double y){
       mu = (getResistance(x,y));
       double resistance_acceleration = (mu*g*(vx/Math.sqrt(vx*vx+vy*vy)))*-1;
       return resistance_acceleration;
   }
   public double getFFrictionY(double x, double y){
       mu = (getResistance(x,y));
       double resistance_acceleration = (mu*g*(vy/Math.sqrt(vx*vx+vy*vy)))*-1;
       return resistance_acceleration;
   }
   public double combineHeightFrictionX(double x, double y){
       double changeInAX= getFHeightX(x,y)+getFFrictionX(x,y);
       return changeInAX;
   }
   public double combineHeightFrictionY(double x, double y){
       double changeInAY= getFHeightY(x,y)+getFFrictionY(x,y);
       return changeInAY;
   }
   public void RK4(double vx, double vy){
    double k1x = vx;
    double k1y = vy;
    double k1vx = getFHeightX(x,y) + getFFrictionX(x,y);
    double k1vy = getFHeightY(x,y) + getFFrictionY(x,y);
    double k2x = vx +(dt*k1vx)/2;
    double k2y = vy +(dt*k1vy)/2;
    double k2vx = getFHeightX(x+(k1x*dt)/2,y+(k1y*dt)/2) + getFFrictionX(x+(k1x*dt)/2,y+(k1y*dt)/2);
    double k2vy = getFHeightY(x+(k1x*dt)/2,y+(k1y*dt)/2) + getFFrictionY(x+(k1x*dt)/2,y+(k1y*dt)/2);
    double k3x = vx +(dt*k2vx)/2;
    double k3y = vy +(dt*k2vy)/2;
    double k3vx = getFHeightX(x+(k2x*dt)/2,y+(k2y*dt)/2) + getFFrictionX(x+(k2x*dt)/2,y+(k2y*dt)/2);
    double k3vy = getFHeightY(x+(k2x*dt)/2,y+(k2y*dt)/2) + getFFrictionY(x+(k2x*dt)/2,y+(k2y*dt)/2);
    double k4x = vx +dt*k3vx;
    double k4y = vy +dt*k3vy;
    double k4vx = getFHeightX(x+(k3x*dt),y+(k3y*dt)) + getFFrictionX(x+(k3x*dt),y+(k3y*dt));
    double k4vy = getFHeightY(x+(k3x*dt),y+(k3y*dt)) + getFFrictionY(x+(k3x*dt),y+(k3y*dt));
    vx = vx + 1/6*dt*(k1vx+2*k2vx+2*k3vx+k4vx);
    vy = vy + 1/6*dt*(k1vy+2*k2vy+2*k3vy+k4vy);
    x = x + 1/6*dt*(k1x+2*k2x+2*k3x+k4x);
    y = y + 1/6*dt*(k1y+2*k2y+2*k3y+k4y);
   }

   public double getVX(){
       return vx;
   }

   public double getVY(){
       return vy;
   }

   public double getX(){
       return x;
   }

   public double getY(){
       return y;
   }

}