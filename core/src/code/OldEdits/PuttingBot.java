package code.OldEdits;

import  code.Physics.Rungekuttasolver;
import code.Screens.PuttingGameScreen;

import java.math.BigDecimal;
import java.math.MathContext;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Alexandre Martens
 */
//For the moment it's a stand alone code using the Runge Kutter for fitness
public class PuttingBot {
    //Hyperparameters
    static final int populationAmount = 50; //amount of individuals in 1 generation
    static final int generations = 100; //amount of generations
    static int reducerThreshold = 50; //which generation the optimisation starts
    static final double  mutationRate = 0.34; //probability that an individual will mutate
    static final double crossoverRate = 0.4; //UNDER 0.5, will replace from bottom to up fitness. half max

    //Other parameters
    static double angleRangeReducer = 0.05; //% of the adjustment
    static double velocityReducer = 0.08; //% of the adjustment
    static final int susCrossover = (int)(crossoverRate*populationAmount); //EVEN NUMBER, <= populationAmount/2,  Number of selections in 1 spin
    static final int susMutation = (int)(mutationRate*populationAmount); //EVEN NUMBER! Number of selections in 1 spin

    //Positions & Velocities
    static double [] angleRange = {0,90}; //OPTIMISATION by reducing the range of angles (no opposite kick)
    static double [] velocityRange = {1, 5}; //NOT START FROM '0' !! OPTIMISATION by reducing the range of angles (no opposite kick)

    //will need to calc the height at the end to put the ball at the exact pos
    static final double [] flagPos = {PuttingGameScreen.getFlagPositionX(),PuttingGameScreen.getFlagPositionZ()};
    static final int [] startingPos = {0,0};

    //Others
    static final double tolerance = 0.2;
    static final int sf = 6;
    static int countRangeReducerCycles;

    //Timers
    static long start = 0;
    static long stop = 0;

    static double [][] population = new double[populationAmount][3]; //3 being Angle, Velocity and fitness

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //makes an array of all possiilities and speed + select the best 100.
    static void initialisationByRange(){
        // Will contain all the possible combinations 'int'
        double [][] fullArray = new double[(int)((-velocityRange[0]+velocityRange[1]+1)*(Math.abs(angleRange[0])+angleRange[1]+1))][3];

        int counter = 0;

        //create all the possibilities with fitness
        for (int i = (int) angleRange[0]; i <= angleRange[1]; i++){
            for (int j = (int) velocityRange[0]; j <= velocityRange[1]; j++){
                fullArray[counter] = new double[]{i, j, initFitness(i,j)};
                counter++;
            }
        }

        //sort fitness
        sort(fullArray);
        //copy the best 100 to the original pop
        System.arraycopy(fullArray, 0, population, 0, populationAmount);
        System.out.println(Arrays.deepToString(population));

        // Calc the angle range
        angleRange = new double[2];

        for (int i = 0; i < 10; i++){
            //+1-1 because it can be 60.01
            //check min
            if (population[i][0] < angleRange[0]){
                angleRange[0] = population[i][0]-1;
            }
            //check max
            else if (population[i][0] > angleRange[1]){
                    angleRange[1] = population[i][0]+1;
                }
        }

        // Calc the velocity range
        velocityRange = new double[]{population[0][1], population[0][1]};

        for (int i = 0; i < 10; i++){
            //check min
            if (population[i][1] < velocityRange[0]){
                velocityRange[0] = population[i][1]-1;
            }
            //check max
            else if (population[i][1] > velocityRange[1]){
                velocityRange[1] = population[i][1]+1;
            }
        }

        System.out.println("Range Angle: " + Arrays.toString(angleRange));
        System.out.println("Range Velocity: " + Arrays.toString(velocityRange));
        stop = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (stop-start)/1000 + "s.");

    }

    // Calculates for the INITIALISATION the fitness via RK4 and distance from the hole
    static double initFitness(double angle, double velo){
            double [] fullArray = new double[] {angle,velo,0};

            double results[] = RK4(fullArray);

            //Calculate the fitness by the distance between the flag and the ball
            double fitnessCalc = Math.sqrt(Math.pow((flagPos[0] - results[0]), 2) + (Math.pow((flagPos[1] - results[1]), 2)));

            //If in hole
            if (fitnessCalc <= tolerance){ //Means we're in the diameter of the flag
                stop = System.currentTimeMillis();
                printFound(fullArray, true);
        }
        return fitnessCalc;
    }

    // Generates a random double between 2 numbers and rounded to n-sf
    static double random(double firstN, double secondN, int sf){

        //Generate a random number in the range
        double randomN =  (Math.random()*(secondN-firstN))+firstN;

        //Significant figures rounding
        BigDecimal bd = new BigDecimal(randomN);
        bd = bd.round(new MathContext(sf));
        double roundN = bd.doubleValue();

        return roundN;
    }

    // Sort the fitness values, keeping the whole individual
    static void sort(double [][] array){

        Arrays.sort(array, new Comparator<double[]>() {
            @Override
            public int compare(double[] o1, double[] o2) {
                return -(Double.compare(o2[2], o1[2])); //low to high https://www.geeksforgeeks.org/double-compare-method-in-java-with-examples/
            }
        });
    }

    // Executes the RK4 class giving back the position of the ball
    static double[] RK4(double [] individual){
        //Convert degrees to radians, radians is the argument for Math.sin or Math.cos
        double angle = (individual[0]*Math.PI)/180;

        //Split the velocity vector into x,y components
        double vxi = (individual[1])*Math.cos(angle);
        double vyi = (individual[1])*Math.sin(angle);

        Rungekuttasolver rk = new Rungekuttasolver();
        return  rk.startRK4(startingPos[0],startingPos[1], vxi, vyi);
    }

    // Fitness of the population, returns the same array with their respective fitness
    static void fitness(){

        double [][] populationTemp = population; //make a copy of the original population array

        for (int i = 0; i < populationTemp.length; i++){

            double results[] = RK4(populationTemp[i]);

            //Calculate the fitness by the distance between the flag and the ball
            double fitnessCalc = Math.sqrt(Math.pow((flagPos[0] - results[0]), 2) + (Math.pow((flagPos[1] - results[1]), 2)));

            populationTemp[i][2] = fitnessCalc;

            //If in hole
            if (populationTemp[i][2] <= tolerance){ //Means we're in the diameter of the flag
                stop = System.currentTimeMillis();
                printFound(populationTemp[i], true);
            }
        }

        population = populationTemp; //Updates the new population with it's corresponding fitness values
    }

    // Stochastic Universal Sampling selection
    // http://puzzloq.blogspot.com/2013/03/stochastic-universal-sampling.html
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

    // Selection via SUS
    static void selection(){

        int [] selected = SUS(susCrossover);
        for (int i = 0; i < selected.length; i=i+2){
            population[populationAmount-1-i] = crossover(population[selected[i]], population[selected[i+1]]);
        }

        selected = SUS(susMutation);
        for (int i = 0; i < selected.length; i++){

            //avoid losing the elites
            if (selected[i] == 0 || selected[i] == 1 ){
                population[populationAmount-1-selected[i]] = mutation(population[populationAmount-1-selected[i]][2]);
            }
            population[selected[i]] = mutation(population[selected[i]][2]);
        }
    }

    // Crossover to keep similarities, but increase variety
    static double [] crossover(double [] parent1, double [] parent2){

        double[] child = {parent1[0],parent2[1], 0};

        return child;
    }

    // Change individuals to random numbers to increase diversity of the population
    static double [] mutation(double fitnessIndivi){

        double [] individual = new double[] {random(angleRange[0], angleRange[1], sf), random(velocityRange[0], velocityRange[1], sf), fitnessIndivi};
        return individual;
    }

    //Checks if an optimisation is needed
    static void optimisationCheck(int currentGen){

        double decreaseIntervalOfOptimisation = 0.5; //30-15-7-3

        //updates the range more regularly as we evolve in the ga
        reducerThreshold = (int) (currentGen + (currentGen/countRangeReducerCycles)*decreaseIntervalOfOptimisation); //stop at current gen + half of it

        //do not launch a range reduction directly at the first optimisation
        if (countRangeReducerCycles != 1) {
            angleRangeReducer = Math.pow(angleRangeReducer, 2);
            velocityReducer = Math.pow(velocityReducer, 2);
        }

        optimisationYield();
        countRangeReducerCycles++;

    }

    //Reduces the yield of angle and velocity
    static void optimisationYield(){

        //Angle
        angleRange[0] = (population[0][0] - population[0][0]*angleRangeReducer);
        angleRange[1] = (population[0][0] + population[0][0]*angleRangeReducer);

        //Velocity
        velocityRange[0] = (population[0][1] - population[0][1]*velocityReducer);
        velocityRange[1] = (population[0][1] + population[0][1]*velocityReducer);

        System.out.println("- - - " + "\n" + "Optimisation Angle: " + angleRange[0] + ";" + angleRange[1] + "\n" +
                "Optimisation Velocity: " + velocityRange[0] + ";" + velocityRange[1] + "\n" + "- - - ");
    }

    //prints a 2D array
    static void print2D(){
        for (double [] i: population){
            System.out.println(Arrays.toString(i));
        }
    }

    //prints results when found
    static void printFound(double [] foundIndividual, boolean found){
        if (found == true) {
            System.out.println("*********************************************************");
            System.out.println("OPTIMAL option = Angle: " + foundIndividual[0] + ", Velocity: " + foundIndividual[1]);
            System.out.println("Final position of: " + Arrays.toString(RK4(foundIndividual)));
            System.out.println("Extra info: " + Arrays.toString(foundIndividual));
            System.out.println("Time elapsed: " + (stop - start) / 1000 + "s.");
            System.out.println("*********************************************************");
            return;
        } else {
            System.out.println("\n" + "Best option found, not optimal = Angle:" + foundIndividual[0] + ", Velocity: " + foundIndividual[1]);
            System.out.println("Final position of: " + Arrays.toString(RK4(foundIndividual)));
            System.out.println("Extra info: " + Arrays.toString(foundIndividual));
            System.out.println("Time elapsed: " + (stop-start)/1000 + "s.");
            return;
        }
    }

    public static void main(String[] args) {

        // Starting details
        System.out.println("Genetic algorithm starting...");
        start = System.currentTimeMillis();
        countRangeReducerCycles = 0;

        // Initialise the population by range to increase speed and targeting faster
        initialisationByRange();

        for (int i = 0; i < generations; i++){

            System.out.println("Generation: " + i);

            fitness();
            sort(population);
            selection(); //Includes the mutation & crossover + updates the population

            System.out.println(Arrays.toString(population[0]) + "  " + Arrays.toString(population[1]) + "  " + Arrays.toString(population[2]));

            //Interval optimisation
            if (i != 0 && i % reducerThreshold == 0){
                countRangeReducerCycles++;
                optimisationCheck(i);
            }
        }

        fitness();
        sort(population);
        stop = System.currentTimeMillis();
        printFound(population[0],false);
        //print2D();
    }
}
