package old_code;

import code.Board.*;
import code.Physics.EulerSolver;
import code.Physics.PhysicsEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * class that combines the course and the physics-engine
 */
public class PuttingSimulator extends EulerSolver {

    private PuttingCourse course;
    private PhysicsEngine engine;
    private Vector2d acceleration;
    Vector2d position = new Vector2d(0, 0);
    Vector2d velocity = new Vector2d(0, 0);

    /**
     * parametric constructor
     * @param course the course that is specified
     * @param engine the physics engine that is specified
     */
    public PuttingSimulator(PuttingCourse course, PhysicsEngine engine) {

        this.course = course;
        this.engine = engine;
    }

    /**
     * this method takes shot based on a text file in which the information is specified
     * @param file the file of which we read the input
     */
    public void take_shot_mode2(File file){

        double x_velocity = 0;
        double y_velocity = 0;
        String currentLine;
        String[] tab;
        BufferedReader bufferedReader;
        FileReader fileReader;

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            currentLine = bufferedReader.readLine();
            tab = currentLine.split(" ");
            x_velocity = Double.parseDouble(tab[0]);
            y_velocity = Double.parseDouble(tab[1]);
            bufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Vector2d p = new Vector2d(x_velocity, y_velocity);
        //take_shot(p);
    }
}
