package code.Bot;

import java.util.ArrayList;
import java.util.Arrays;

public class OptimiseHyperParams {
    static int [] populationAmount = {200,150,250,300,350,400};
    static double [] mutationRate = {0.45,0.4,0.35,0.3,0.25,0.2,0.15,0.1,0.05};
    static double [] crossoverRate = {0.5,0.4,0.3,0.2,0.1};
    static int [] reducerTreshhold = {15,20,25,30,35,40};

    static int generations = 50;
    static int roundsPerParams = 3;

    static double [][] finalRes = new double[10000][4];

    public static void main(String[] args) {
        int n = 0;

        for (int i = 0; i < populationAmount.length; i++){
            System.out.println("pop; " + populationAmount[i]);
            for (int j = 0; j < mutationRate.length; j++){
                System.out.println("mut; " + mutationRate[j]);
                for (int k = 0; k < crossoverRate.length; k++){
                    System.out.println("cross; " + crossoverRate[k]);
                    for (int h = 0; h < reducerTreshhold.length; h++){

                        HyperparamsOpt bot = new HyperparamsOpt(populationAmount[i],mutationRate[j],crossoverRate[k],
                                reducerTreshhold[h], generations,roundsPerParams);

                        boolean success = bot.start();

                        if (success == true){
                            System.out.println("success: " + success);
                            finalRes[n] = new double[]{populationAmount[i], mutationRate[j], crossoverRate[k], reducerTreshhold[h]};
                            System.out.println("===> " + Arrays.toString(finalRes[n]));
                            n++;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < finalRes.length; i++){
            System.out.println(Arrays.toString(finalRes[i]));
            if ((finalRes[i][0] == 0 && finalRes[i][1] == 0)){
                break;
            }
        }
    }
}
