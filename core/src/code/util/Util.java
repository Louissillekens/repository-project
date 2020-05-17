package code.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

    /**
     * this simple method can round a number to a number of decimal places
     * @param value the value we wish to round
     * @param places the places after the comma to round to
     * @return the rounded value
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
