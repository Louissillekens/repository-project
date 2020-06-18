package code.NN;

import code.Lets_Go_Champ.DQN;

import java.io.*;

/**
 * @author Alexandre Martens
 */
public class ExportNeuralNets {

    /**
     * Saves the network to a local file
     * @param policy_network DQN policy_network object
     * @param target_network DQN target_network object
     */
    public static void exportNetworks(DQN policy_network, DQN target_network){
        try {
            // Create the files
            FileOutputStream file_policy_network = new FileOutputStream(new File("policy_network"));
            FileOutputStream file_target_network = new FileOutputStream(new File("target_network"));

            // Create a stream
            ObjectOutputStream out_policy_network = new ObjectOutputStream(file_policy_network);
            ObjectOutputStream out_target_network = new ObjectOutputStream(file_target_network);

            // Write networks to file
            out_policy_network.writeObject(policy_network);
            out_target_network.writeObject(target_network);

            // Close the steam
            out_policy_network.close();
            out_target_network.close();

            // Close the file
            file_policy_network.close();
            file_target_network.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }

        System.out.println(" ===> All networks exported");
    }
}
