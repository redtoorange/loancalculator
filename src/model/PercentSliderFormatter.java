package model;

import javafx.util.StringConverter;

import java.text.NumberFormat;

/**
 * PercentSliderFormatter.java - Formatter to correctly format the text on a slider from a decimal number into
 * a percent.  This works both ways, to convert from a double into a percent, and from a percent back to a double.
 *
 * @author Andrew McGuiness
 * @version 03/Apr/2017
 */
public class PercentSliderFormatter extends StringConverter< Double > {
    private NumberFormat formatter = NumberFormat.getPercentInstance();

    /**
     * Convert a Double value into a String value on the slider.
     *
     * @param inputD The double that needs to be parsed into a percent String.
     * @return A String value that represents inputD as a percent.  If inputD is null, a value of "0.0%" will
     * be returned.
     */
    @Override
    public String toString( Double inputD ) {
        String outputS = "0.0%";

        if ( inputD != null )
            outputS = formatter.format( inputD.doubleValue() / 100 );

        return outputS;
    }

    /**
     * Convert a String value on the slider into a Double value.
     *
     * @param inputS The String that represent a percent value that needs to be converted into a double.
     * @return A Double object containing the parsed value of inputS.  If inputS is empty, a value of 0 will be
     * returned.
     */
    @Override
    public Double fromString( String inputS ) {
        Double outputD = new Double( 0.0 );

        if ( !inputS.isEmpty() )
            outputD = Double.parseDouble( inputS.replaceAll( "[%]", "" ) ) * 100;

        return outputD;
    }
}
