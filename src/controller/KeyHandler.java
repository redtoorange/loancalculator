package controller;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * KeyHandler.java - Custom input handler for TextFields.  This allows for simple filtering of unwanted keys.
 *
 * @author Andrew McGuiness
 * @version 29/Mar/2017
 */
public class KeyHandler implements EventHandler< KeyEvent > {
    private final String regex;
    private final boolean allowDecimal;


    /**
     * Constructor that is used for numbers.
     *
     * @param decimal Are decimals allowed?
     */
    public KeyHandler( boolean decimal ) {
        this( "[0-9]", decimal );
    }

    /**
     * Create a custom filter, only keys that matched the passed regex will be allowed.
     *
     * @param regex   The keys that will be allowed, do NOT include decimals.
     * @param decimal Are decimals allowed?
     */
    public KeyHandler( String regex, boolean decimal ) {
        this.allowDecimal = decimal;
        this.regex = regex;
    }

    /**
     * This catches KeyEvents on the TextFields.  It will use the regex to filter unwanted characters out of the input.
     * If decimals are allowed, than is will only allow for there to be one per TextField.
     *
     * @param event The KeyEvent passed by the TextField, the event will be consumed if it doesn't match the regex.
     */
    @Override
    public void handle( KeyEvent event ) {
        TextField field = ( TextField ) event.getSource();

        //Does the passed key match the regex, if not it is a candidate for consumption
        if ( !event.getCharacter().matches( regex ) )
            //if decimals aren't allowed, consume it, if they are, make sure there isn't one already in the field.
            if ( !( allowDecimal && event.getCharacter().matches( "[.]" ) && !field.getText().contains( "." ) ) )
                event.consume();
    }
}
