package code.Bot;

public class OptimiseHyperParams {
    static int [] populationAmount = {150,200,250,300,350,400};
    static double [] mutationRate = {0.05,0.1,0.2,0.25,0.3,0.35,0.4,0.45};
    static double [] crossoverRate = {0.1,0.2,0.3,0.4,0.5};
    static int [] reducerTreshhold = {15,20,25,30,35,40};

    static int generations = 20;
    static int roundsPerParams = 3;


    public static void main(String[] args) {

        for (int i = 0; i < populationAmount.length; i++){
            for (int j = 0; j < mutationRate.length; j++){
                for (int k = 0; k < crossoverRate.length; k++){
                    for (int h = 0; h < reducerTreshhold.length; h++){
                        System.out.println(h);

                        HyperparamsOpt bot = new HyperparamsOpt(populationAmount[i],mutationRate[j],crossoverRate[k],
                                reducerTreshhold[h], generations,roundsPerParams);
                        int success = bot.start();

                        if (success >=2){
                            System.out.println("Pop: " + populationAmount [i] + ", mut: " + mutationRate[j] +
                                    ", cross: " + crossoverRate[k] + ", reducer: " + reducerTreshhold[h]);
                        }
                    }
                }
            }
        }
    }


}
