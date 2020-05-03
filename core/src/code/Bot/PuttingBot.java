package code.Bot;

import code.Physics.Rungekuttasolver;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Comparator;

//For the moment it's a stand alone code using the Runge Kutter for fitness
public class PuttingBot {
    //Hyperparameters
    static final int populationAmount = 500;
    static final int generations = 100;
    static final double  mutationRate = 0.1;
    static final int susCrossover = 50; //EVEN NUMBER! Number of selections in 1 spin
    static final int susMutation = (int)mutationRate*populationAmount; //EVEN NUMBER! Number of selections in 1 spin


    static int angleRange = 360; //OPTIMISATION by reducing the range of angles (no opposite kick)
    static int velocityRange = 20;
    static final double [] flagPos = {15,30};
    static final double tolerance = 0.2;

    static double [][] population = new double[populationAmount][3]; //3 being Angle, Velocity and fitness


    //Generates a random double between 2 numberes and rounded to n-sf
    static double random(double firstN, double secondN, int sf){

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
                return -(Double.compare(o2[2], o1[2])); //low to high https://www.geeksforgeeks.org/double-compare-method-in-java-with-examples/
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
                    population[i][j] = 0;
                }
            }
        }
    }

    //Executes the RK4 class giving baack the position of the ball
    static double[] RK4(double [] individual){
        //Convert degrees to radians, radians is the argument for Math.sin or Math.cos
        double angle = (individual[0]*Math.PI)/180;

        //Split the velocity vector into x,y components
        double vxi = (individual[1])*Math.cos(angle);
        double vyi = (individual[1])*Math.sin(angle);

        Rungekuttasolver rk = new Rungekuttasolver();
        return  rk.startRK4(0,0, vxi, vyi);
    }

    //Fitness of the population, returns the same array with their respective fitness
    static void fitness(){

        double [][] populationTemp = population; //make a copy of the original population array

        for (int i = 0; i < populationTemp.length; i++){

            double results[] = RK4(populationTemp[i]);

            //Calculate the fitness by the distance between the flag and the ball
            double fitnessCalc = Math.sqrt(Math.pow((flagPos[0] - results[0]), 2) + (Math.pow((flagPos[1] - results[1]), 2)));

            populationTemp[i][2] = fitnessCalc;

            //If in hole
            if (populationTemp[i][2] <= tolerance){ //Means we're in the diameter of the flag
                System.out.println("OPTIMAL option = Angle: " + populationTemp[i][0] + ", Velocity: " + populationTemp[i][1]);
                System.out.println("Final position of: " + Arrays.toString(RK4(populationTemp[i])));
                System.exit(0);
            }

        }

        population = populationTemp; //Updates the new population with it's corresponding fitness values
    }

    //Stochastic Universal Sampling selection
    //http://puzzloq.blogspot.com/2013/03/stochastic-universal-sampling.html
    static int [] SUS(int susNumber){

        // Calculate total fitness of population
        double f = 0.0;
        for (double [] individual : population) {
            f += individual[2];
        }

        // Calculate distance between the pointers
        double p = f / susNumber;

        // Pick random number between 0 and p
        double start = Math.random() * p;

        // Pick n individuals
        int[] individuals = new int[susNumber];
        int index = 0;
        double sum = population[index][2];

        for (int i = 0; i < susNumber; i++) {

            // Determine pointer to a segment in the population
            double pointer = start + i * p;

            // Find segment, which corresponds to the pointer
            if (sum >= pointer) {
                individuals[i] = index;
            } else {
                for (++index; index < population.length; index++) {
                    sum += population[index][2];
                    if (sum >= pointer) {
                        individuals[i] = index;
                        break;
                    }
                }
            }
        }
        // Return the set of indexes, pointing to the chosen individuals
        return individuals;
    }

    static void selection(){

        int [] selected = SUS(susCrossover);
        for (int i = 0; i < selected.length; i=i+2){
            population[populationAmount-1-i] = crossover(population[selected[i]], population[selected[i+1]]);
        }

        selected = SUS(susMutation);
        for (int i = 0; i < selected.length; i++){
            population[populationAmount-1-susCrossover-i] = mutation(population[selected[i]]);
        }
    }

    //Crossover to keep similarities, but increase variety
    static double [] crossover(double [] parent1, double [] parent2){

        double[] child = {parent1[0],parent2[1], 0};

        return child;
    }

    //change individuals to random numbers to increase diversity of the population
    static double [] mutation(double [] individual){

        individual = new double[] {random(0, angleRange, 2), random(0, velocityRange, 2), 0};

        return individual;
    }

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
        fitness();
        sort(population);

        //print2D(population);

        System.out.println("Best option found, not optimal = Angle:" + population[0][0] + ", Velocity: " + population[0][0]);
        System.out.println("Final position of: " + Arrays.toString(RK4(population[0])));
        System.out.println("Extra info: " + Arrays.toString(population[0]));
    }
}
