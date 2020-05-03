package code.Bot;

import code.Board.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Comparator;

//For the moment it's a stand alone code using the Runge Kutter for fitness
public class PuttingBot {
    //Hyperparameters
    static int populationAmount = 10;
    static int generations = 10;
    double mutationRate = 0.1;

    static int angleRange = 360; //OPTIMISATION by reducing the range of angles (no opposite kick)
    static int velocityRange = 20;

    static double [][] population = new double[populationAmount][3]; //3 being Angle, Velocity and fitness



    //Generates a random double between 2 numberes and rounded to n-sf
    static double random(int firstN, int secondN, int sf){

        //Generate a random number in the range
        double randomN =  (Math.random()*(secondN-firstN))+firstN;

        //Significant figures rounding
        BigDecimal bd = new BigDecimal(randomN);
        bd = bd.round(new MathContext(sf));
        double roundN = bd.doubleValue();

        return roundN;
    }

    //Sort the fitness values
    static void sort(double [][] array){

        Arrays.sort(array, new Comparator<double[]>() {
            @Override
            public int compare(double[] o1, double[] o2) {
                return Double.compare(o2[2], o1[2]);
            }
        });
    }

    //Initialisation of the empty population array
    static void initialisation(){

        for (int i=0; i < population.length; i++){
            for (int j=0; j < population[i].length; j++){

                if (j == 0){    //Angle
                    population[i][j] = random(0, angleRange, 2);
                }else if (j == 1){  //Velocity
                    population[i][j] = random(0, velocityRange, 2);
                }else{  //Fitness
                    population[i][j] = random(0,10,1);
                }
            }
        }
    }

    //Fitness of the population, returns the same array with their respective fitness
    static void fitness(){

        double [][] populationTemp = population; //make a copy of the original population array

        for (int i = 0; i < populationTemp.length; i++){



            if (populationTemp[i][2] == 0){ //Fitness == 0 means we found the good combination
                System.out.println("Angle: " + populationTemp[i][0] + "Velocity: " + populationTemp[i][1]);
            }

        }

        population = populationTemp; //Updates the new population with it's corresponding fitness values
    }

    static void selection(){
        
    }

    static void crossover(){}

    static void mutation(){}

    //prints a 2D array
    static void print2D(double [][] array){
        for (double [] i: array){
            System.out.println(Arrays.toString(i));
        }
    }

    public static void main(String[] args) {
        initialisation();

        for (int i = 0; i < generations; i++){
            fitness();
            sort(population);
            selection(); //Includes the mutation & crossover + updates the population
        }

        print2D(population);
    }

}
