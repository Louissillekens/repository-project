package code.Lets_Go_Champ;

/**
 * @author Alexandre Martens
 */
public class TrainingData {
    float[] dataInput;

    public void setDataOutput(float dataOutput, int place) {
        this.dataOutput[place] = dataOutput;
    }

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
