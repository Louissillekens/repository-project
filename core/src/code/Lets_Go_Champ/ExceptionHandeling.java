package code.Lets_Go_Champ;

public class ExceptionHandeling extends Exception {

    public ExceptionHandeling(String error){
            if (error == "activation_function"){
                System.out.println("ERROR: activation function is written incorrectly or does not exist");
            }
    }
}
