package code.Physics;


import code.Board.Friction_function;
import code.Board.Height_function;
import code.Board.Vector2d;

public class EulerSolver {

    private double dt;
    private Vector2d P;
    private Vector2d Pprev;
    private Vector2d V;
    private Vector2d a;
    private Height_function height_f;
    private Friction_function friction_f;
    private double resistance;
    private double height;
    private final double g = 9.81;    


    public void set_step_size(double h) {

        dt = h;
    }

    public void set_start_acceleration(double x, double y) {
        a.change_both(x, y);
    }

    public void reset_ball() {
        P.change_both(0,0);
        V.change_both(0,0);

    }

    public void update_P() {
        Pprev = P;
        double Px = P.get_x();
        double Py = P.get_y();
        double Vx = V.get_x();
        double Vy = V.get_y();
        P.change_both(Px+Vx*dt, Py+Vy*dt);
    }

    public void update_V() {
        double ax = a.get_x();
        double ay = a.get_y();
        double Vx = V.get_x();
        double Vy = V.get_y();
        V.change_both(Vx+ax*dt, Vy+ay*dt);
    }

    public void update_a() {
        double ax = a.get_x();
        double ay = a.get_y();

        ay = ay - g*(height_f.evaluate(P)-height_f.evaluate(Pprev))-friction_f.evaluate(P)*g*(V.get_y()/Math.sqrt(V.get_x()*V.get_x()+V.get_y()*V.get_y()));
        ax = ax - g*(height_f.evaluate(P)-height_f.evaluate(Pprev))-friction_f.evaluate(P)*g*(V.get_x()/Math.sqrt(V.get_x()*V.get_x()+V.get_y()*V.get_y()));

        a.change_both(ax, ay);
    }

    public double get_resistance(double x, double y) {

        Vector2d p = new Vector2d(x, y);
        return friction_f.evaluate(p);
    }

    public double get_height(double x, double y) {

        Vector2d p = new Vector2d(x, y);
        return height_f.evaluate(p);
    }
    public void update_all(){
        update_P();
        update_V();
        update_a();
    }
}