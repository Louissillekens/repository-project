package code.NeuralNet;
import code.Physics.Rungekuttasolver;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Alexandre Martens
 */
public class FileMaker {
    int angle;
    int velo;
    String nameFile;

    static TrainingData[] trainingDataSet;


    public FileMaker(int angle, int velo) {
        this.angle = angle;
        this.velo = velo;

        int datasetSize = angle * velo;
        trainingDataSet = new TrainingData[datasetSize]; // Define the length of dataset
    }

    public void createFile() {
        String name = String.valueOf(angle) + String.valueOf(velo) + ".txt";
        this.nameFile = name;

        try {
            File myObj = new File(nameFile);
            if (myObj.createNewFile()) {
                System.out.println("\t" + "File created: " + myObj.getName());
                fillFile();

            } else {
                System.out.println("\t" + "File already exists. No new file created.");
            }
        } catch (IOException e) {
            System.out.println("\t" + "An error occurred.");
            e.printStackTrace();
        }
    }

    // Fills in the file
    public void fillFile() throws IOException {
        FileWriter file = new FileWriter(nameFile);
        System.out.println("\t" + "Filling the file, please wait...");

        for (float i = 0; i < angle; i++) {
            for (int j = 1; j <= velo; j++) {
                try {
                    double angle = (i * Math.PI) / 180;

                    //Split the velocity vector into x,y components
                    double vxi = (j) * Math.cos(angle);
                    double vyi = (j) * Math.sin(angle);

                    Rungekuttasolver rk = new Rungekuttasolver();

                    double[] pos = rk.startRK4(0, 0, vxi, vyi);

                    file.write(i + "," + j + "," + pos[0] + "," + pos[1] + "\n");
                } catch (IOException e) {
                    System.out.println("\t" + "An error occurred.");
                    e.printStackTrace();
                }
            }
        }

        System.out.println("\t" + "Successfully wrote to the file.");
        file.close();
    }


    // Convert file to trainingdataset
    public void convertFile(){
        File file = new File(nameFile);

        int counter = 0;

        try {
            Scanner sc = new Scanner(file);

            // Go true all the file
            while(sc.hasNextLine()){
                float[] inputs = new float[2];
                float[] outputs = new float[2];

                String line = sc.nextLine();
                String[] details = line.split(",");
                inputs[0] = Float.parseFloat(details[0]);
                inputs[1] = Float.parseFloat(details[1]);
                outputs[0] = Float.parseFloat(details[2]);
                outputs[1] = Float.parseFloat(details[3]);

                trainingDataSet[counter] = new TrainingData(inputs, outputs);

                counter++;
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static TrainingData[] getTrainingDataSet() {
        return trainingDataSet;
    }
}

