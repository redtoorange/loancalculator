package utilities;

/**
 * MathUtil.java - A custom math library to handle common calculations.
 *
 * @author Andrew McGuiness
 * @version 03/Apr/2017
 */
public class MathUtil {
    /**
     * Clamp the value between the min and the max.
     *
     * @param value Value to clamp.
     * @param min   Minimum value.
     * @param max   Maximum value.
     * @return The clamped value.
     */
    public static double clamp( double value, double min, double max ) {
        double result = value;

        if ( result < min )
            result = min;
        if ( result > max )
            result = max;

        return result;
    }
}
