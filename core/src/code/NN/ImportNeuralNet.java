package code.NN;

import code.Lets_Go_Champ.DQN;

import java.io.*;

public class ImportNeuralNet {

    public static DQN importNetwork(String network){
        try {

            // Find the file
            FileInputStream file_network = new FileInputStream(new File(network));

            // Create a stream
            ObjectInputStream in_network = new ObjectInputStream(file_network);

            DQN loaded_network = (DQN) in_network.readObject();

            // Close the steam
            in_network.close();

            // Close the file
            file_network.close();

            System.out.println(" ===> " + network + " imported");
            return loaded_network;

        } catch (FileNotFoundException e) {
            System.out.println("File with name: " + network + " not found! Recheck spelling");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error initializing stream");
        }
        System.out.println(" ! ===> NO NETWORK IMPORTED");
        return null;
    }
}
