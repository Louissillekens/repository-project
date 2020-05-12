package code.NeuralNet;

public class TrainingData {
    float[] dataInput;
    float[] dataOutput;

    public TrainingData(float[] dataInput, float[] dataOutput){
        this.dataInput = dataInput;
        this.dataOutput = dataOutput;
    }

    public float[] getDataInput() {
        return dataInput;
    }

    public float[] getDataOutput() {
        return dataOutput;
    }
}
