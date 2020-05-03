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
   double vx;
   double vy;   
   double x;
   double y;
   double onesixth =(double)1/6;
   
   public static void main(String args[]){
       Rungekuttasolver solver = new Rungekuttasolver();
       solver.setValues(0,0,50,20);
       for(int i=0; i<8000; i++){
       solver.RK4();
       }
       System.out.println("the x and y coordinates are:");
       System.out.println("x: "+solver.getX());
       System.out.println("y: "+solver.getY());
   }

 public void setValues(double xi,double yi, double vxi, double vyi){
       x=xi;
       y=yi;
       vx=vxi;
       vy=vyi;
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
       double height =0;
       return height;
   }
   public double getResistance(double x, double y){
       //TO DO: make reistance change based on coordinates
       double resistance = 0.13;
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
/*
    //test to find where the error is
       System.out.println("k1x"+k1x);
       System.out.println("k1y"+k1y);
       System.out.println("k1vx"+k1vx);
       System.out.println("k1vy"+k1vy);
       System.out.println("k2x"+k2x);
       System.out.println("k2y"+k2y);
       System.out.println("k2vx"+k2vx);
       System.out.println("k2vy"+k2vy);
       System.out.println("k3x"+k3x);
       System.out.println("k3y"+k3y);
       System.out.println("k3vx"+k3vx);
       System.out.println("k3vy"+k3vy);
       System.out.println("k4x"+k4x);
       System.out.println("k4y"+k4y);
       System.out.println("k4vx"+k4vx);
       System.out.println("k4vy"+k4vy);
              */
       System.out.println("vx"+vx);
       System.out.println("vy"+vy);
       System.out.println("x"+x);
       System.out.println("y"+y);

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