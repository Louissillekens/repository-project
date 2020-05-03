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
   double onesixth =(0.16666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666667);

   public static void main(String args[]){
       Rungekuttasolver solver = new Rungekuttasolver();
       solver.setValues(0,0,50,20);
       for(int i=0; i<1; i++){
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
       double resistance = 0.001;
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
   public double getFFrictionX(double vx, double vy){
       double resistance_acceleration = ( getResistance(x,y)*g*(vx/Math.sqrt((vx*vx)+(vy*vy))) )*-1;
       resistance_acceleration = -3;
       return resistance_acceleration;
   }
   public double getFFrictionY(double vx, double vy){
       double resistance_acceleration = ( getResistance(x,y)*g*(vy/Math.sqrt(vx*vx+vy*vy)) )*-1;
       resistance_acceleration = -3;
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
   public void RK4(){
      // System.out.println(onesixth);
       //System.out.println(vx);
       //System.out.println(vy);
    double k1x = x;
    double k1y = y;
    //System.out.println("heightx1"+ getFHeightX(x,y) + "heighty1"+getFHeightY(x,y));
    //System.out.println("frictx1"+ getFFrictionX(x,y) + "fricty1"+getFFrictionY(x,y));
    double k1vx =vx+ getFHeightX(x,y) + getFFrictionX(vx,vy);
    double k1vy =vy+ getFHeightY(x,y) + getFFrictionY(vx,vy);
    double k2x = x +(dt*k1vx)/2;
    double k2y = y +(dt*k1vy)/2;
    double k2vx =vx+ getFHeightX(x+(k1x*dt)/2,y+(k1y*dt)/2) + getFFrictionX(x+(k1x*dt)/2,y+(k1y*dt)/2);
    double k2vy =vy+ getFHeightY(x+(k1x*dt)/2,y+(k1y*dt)/2) + getFFrictionY(x+(k1x*dt)/2,y+(k1y*dt)/2);
    double k3x = x +(dt*k2vx)/2;
    double k3y = y +(dt*k2vy)/2;
    double k3vx =vx+ getFHeightX(x+(k2x*dt)/2,y+(k2y*dt)/2) + getFFrictionX(x+(k2x*dt)/2,y+(k2y*dt)/2);
    double k3vy =vy+ getFHeightY(x+(k2x*dt)/2,y+(k2y*dt)/2) + getFFrictionY(x+(k2x*dt)/2,y+(k2y*dt)/2);
    double k4x = x +dt*k3vx;
    double k4y = y +dt*k3vy;
    double k4vx =vx+ getFHeightX(x+(k3x*dt),y+(k3y*dt)) + getFFrictionX(x+(k3x*dt),y+(k3y*dt));
    double k4vy =vy+ getFHeightY(x+(k3x*dt),y+(k3y*dt)) + getFFrictionY(x+(k3x*dt),y+(k3y*dt));
    double vxa = vx - onesixth*dt*(k1vx+2*k2vx+2*k3vx+k4vx);
    //System.out.println(vxa);
    double vya = vy - onesixth*dt*(k1vy+2*k2vy+2*k3vy+k4vy);
    //System.out.println(vya);
    double xa = x+onesixth*dt*(k1x+2*k2x+2*k3x+k4x);
    double ya = y+onesixth*dt*(k1y+2*k2y+2*k3y+k4y);
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