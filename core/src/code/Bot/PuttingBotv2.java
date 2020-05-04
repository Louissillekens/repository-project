package code.Bot;

import code.Physics.Rungekuttasolver;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Comparator;

//For the moment it's a stand alone code using the Runge Kutter for fitness
public class PuttingBotv2 {
    //Hyperparameters
    static final int populationAmount = 300; //amount of individuals in 1 generation
    static final int generations = 100; //amount of generations
    static final double  mutationRate = 0.34; //probability that an individual will mutate
    static final double crossoverRate = 0.4; //UNDER 0.5, will replace from bottom to up fitness. half max

    //Other parameters
    static double angleRangeReducer = 0.05; //% of the adjustment
    static double velocityReducer = 0.08; //% of the adjustment
    static int reducerThreshold = 50; //which generation the optimisation starts
    static final int susCrossover = (int)(crossoverRate*populationAmount); //EVEN NUMBER, <= populationAmount/2,  Number of selections in 1 spin
    static final int susMutation = (int)(mutationRate*populationAmount); //EVEN NUMBER! Number of selections in 1 spin

    //Positions & Velocities
    static double [] angleRange = {-90,90}; //OPTIMISATION by reducing the range of angles (no opposite kick)
    static double [] velocityRange = {0, 15}; //OPTIMISATION by reducing the range of angles (no opposite kick)
    static final double [] flagPos = {35.5,24.5};
    static final int [] startingPos = {0,0};

    //Others
    static final double tolerance = 0.02;
    static final int sf = 8;
    static int countRangeReducerCycles;

    //Timers
    static long start = 0;
    static long stop = 0;

    static double [][] population = new double[populationAmount][3]; //3 being Angle, Velocity and fitness

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

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
                    population[i][j] = random(angleRange[0], angleRange[1], sf);
                }else if (j == 1){  //Velocity
                    population[i][j] = random(velocityRange[0], velocityRange[1], sf);
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
        return  rk.startRK4(startingPos[0],startingPos[1], vxi, vyi);
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
                stop = System.currentTimeMillis();
                printFound(populationTemp[i], true);
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

            //avoid losing the elites
            if (selected[i] == 0 || selected[i] == 1 ){
                population[populationAmount-1-selected[i]] = mutation(population[populationAmount-1-selected[i]][2]);
            }
            population[selected[i]] = mutation(population[selected[i]][2]);
        }
    }

    //Crossover to keep similarities, but increase variety
    static double [] crossover(double [] parent1, double [] parent2){

        double[] child = {parent1[0],parent2[1], 0};

        return child;
    }

    //change individuals to random numbers to increase diversity of the population
    static double [] mutation(double fitnessIndivi){

        double [] individual = new double[] {random(angleRange[0], angleRange[1], sf), random(velocityRange[0], velocityRange[1], sf), fitnessIndivi};
        return individual;
    }


    //Checks if an optimisation is needed
    static void optimisationCheck(int currentGen){

        double decreaseIntervalOfOptimisation = 0.5; //30-15-7-3

        reducerThreshold = (int) (reducerThreshold + (reducerThreshold/countRangeReducerCycles)*decreaseIntervalOfOptimisation); //stop at current gen + half of it

        //do not launch a range reduction directly at the first optimisation
        if (countRangeReducerCycles != 1) {
            angleRangeReducer = Math.pow(angleRangeReducer, 2);
            velocityReducer = Math.pow(velocityReducer, 2);
        }

        //make the range smaller as we evolve in the ga
        if ((reducerThreshold - reducerThreshold *0.5 )> 1){
            reducerThreshold =  (int) (reducerThreshold - reducerThreshold *0.5);
        }


        optimisationYield();
        countRangeReducerCycles++;

        //print2D(population);

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
            System.exit(0);
        } else {
            System.out.println("\n" + "Best option found, not optimal = Angle:" + foundIndividual[0] + ", Velocity: " + foundIndividual[1]);
            System.out.println("Final position of: " + Arrays.toString(RK4(foundIndividual)));
            System.out.println("Extra info: " + Arrays.toString(foundIndividual));
            System.out.println("Time elapsed: " + (stop-start)/1000 + "s.");

        }
    }

    public static void main(String[] args) {

        start = System.currentTimeMillis();
        countRangeReducerCycles = 0;

        initialisation();

        for (int i = 0; i < generations; i++){

            System.out.println("Generation: " + i);

            fitness();
            sort(population);
            selection(); //Includes the mutation & crossover + updates the population

            System.out.println(Arrays.toString(population[0]) + "  " + Arrays.toString(population[1]) + "  " + Arrays.toString(population[2]));

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
