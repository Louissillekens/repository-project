package code.NN;

/**
 * @author Alexandre Martens
 */
public class Data {
    private  float[] dataInput;
    private float[] dataOutput;

    Data(float[] dataInput, float[] dataOutput){
        this.dataInput = dataInput;
        this.dataOutput = dataOutput;
    }


    public void setDataOutput(float value, int position) {
        dataOutput[position] = value;
    }

    public float[] getDataInput() {
        return dataInput;
    }

    public float[] getDataOutput() {
        return dataOutput;
    }

}
