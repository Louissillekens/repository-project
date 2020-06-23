package code.Lets_Go_Champ;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReplayMemory {
    private int capacity; // Capacity of the memory at max capacity
    public int push_count; // Keep track of how many times we added an experience to the memory

    private List<Experience> memory ;

    /**
     * @param capacity The capacity of the memory = number of experiences it can hold
     */
    ReplayMemory(int capacity){
        this.capacity = capacity;

        this.memory = new ArrayList<>(); // To store all the experiences
        this.memory.clear(); // Make it empty
        this.push_count = 0;
    }

    /**
     * @param experience add an experience to the memory
     */
    public void push(Experience experience){
        if (memory.size() < capacity){
            memory.add(experience); // Memory has room for the new exp, add it
        }
        else {
            memory.set(push_count % capacity, experience); // Memory is full, we start to fill it from the beginning
        }
        push_count++; // Tell the object we added an experiece
    }

    /**
     * @param batch_size The number of samples we want to have
     *                   1 sample is 1 experience (S,A,S',R)
     * @return list of experieces of size batch_size chosen at RANDOM
     */
    public List<Experience> getSample(int batch_size){
        Random rand = new Random();

        List<Experience> samples = new ArrayList<>();

        for (int i = 0; i < batch_size; i++) {
            int randomIndex = rand.nextInt(memory.size());
            samples.add(memory.get(randomIndex));
        }
        return samples;
    }

    public boolean canProvideSample(int batch_size){
        return memory.size() >= batch_size;
    }

}
