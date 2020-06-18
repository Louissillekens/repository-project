package code.NN;

import java.io.Serializable;

/**
 * @author Alexandre Martens
 * Hols the data of the input and output layers
 */
public class Data implements Serializable {
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
