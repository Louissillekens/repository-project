package code.OldEdits;

import code.Physics.Rungekuttasolver;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Comparator;

//For the moment it's a stand alone code using the Runge Kutter for fitness
public class HyperparamsOpt {
    //Hyperparameters
    static int populationAmount;
    static int generations;
    static double  mutationRate;
    static double  crossoverRate; //under 0.5
    static int roundsPerParam;
    static  int reducerThreshold; //wich generation the optimisation starts

    static int success;


    static final int susCrossover = (int)(crossoverRate*populationAmount); //EVEN NUMBER, >= populationAmount/2,  Number of selections in 1 spin
    static final int susMutation = (int)(mutationRate*populationAmount); //EVEN NUMBER! Number of selections in 1 spin
    static double angleRangeReducer = 0.03; //% of the adjustment
    static double velocityReducer = 0.1; //% of the adjustment

    static double [][] population; //3 being Angle, Velocity and fitness


    static final int [] velocityRange = {0, 15};
    static final double [] flagPos = {10,20};
    static final double tolerance = 0.05;
    static final int sf = 8;
    static int [] angleRange = {-90,90}; //OPTIMISATION by reducing the range of angles (no opposite kick)

    HyperparamsOpt(int populationAmount, double mutationRate, double crossoverRate, int reducerThreshold, int generations, int roundsPerParam){
        this.populationAmount = populationAmount;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.reducerThreshold = reducerThreshold;
        this.generations = generations;
        this.roundsPerParam = roundsPerParam;
    }

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
    static void initialisation(double [][] population){

        for (int i=0; i < population.length; i++){
            for (int j=0; j < population[i].length; j++){

                if (j == 0){    //Angle
                    population[i][j] = random(angleRange[0], angleRange[1], sf);
                }else if (j == 1){  //Velocity
                    population[i][j] = random(velocityRange[0], velocityRange[1], sf);
                }else{  //Fitness
                    population[i][j] = 0;
                }
            }
        }
    }

    //Executes the RK4 class giving back the position of the ball
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
                success++;
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
            population[selected[i]] = mutation();
        }
    }

    //Crossover to keep similarities, but increase variety
    static double [] crossover(double [] parent1, double [] parent2){

        double[] child = {parent1[0],parent2[1], 0};

        return child;
    }

    //change individuals to random numbers to increase diversity of the population
    static double [] mutation(){

        double[] individual = new double[]{random(angleRange[0], angleRange[1], sf), random(velocityRange[0], velocityRange[1], sf), 0};
        return individual;
    }

    //Reduces the yield of angle and velocity
    static void optimisationYield(){

        //Angle
        angleRange[0] = (int)(population[0][0] - population[0][0]*angleRangeReducer);
        angleRange[1] = (int)(population[0][0] + population[0][0]*angleRangeReducer);

        //Velocity
        velocityRange[0] = (int)(population[0][1] - population[0][1]*velocityReducer);
        velocityRange[1] = (int)(population[0][1] + population[0][1]*velocityReducer);
    }

    //prints a 2D array
    static void print2D(double [][] array){
        for (double [] i: array){
            System.out.println(Arrays.toString(i));
        }
    }

    public boolean start(){
        success = 0;

        for (int ii = 0; ii < roundsPerParam; ii++) {

            double [][] pop = new double[populationAmount][3]; //3 being Angle, Velocity and fitness

            initialisation(pop);
            //System.out.println(Arrays.toString(pop));

            population = pop;

            for (int i = 0; i < generations; i++) {
                fitness();

                if (success >= roundsPerParam){
                    return true;
                }

                sort(population);
                selection(); //Includes the mutation & crossover + updates the population
                if (i == reducerThreshold) {
                    optimisationYield();
                }
            }
            fitness();
        }

        return false;
    }
}

