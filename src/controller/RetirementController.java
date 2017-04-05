package controller;

import model.PercentSliderFormatter;
import model.RetirementYear;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.text.NumberFormat;
import java.util.*;

/**
 * RetirementController.java - The controller for the Retirement View tab of the Application.  The calculations are handed
 * off to a separate class.  This class only handle events and updating the view elements.
 *
 * @author Andrew McGuiness
 * @version 03/Apr/2017
 */
public class RetirementController {
    private ObservableList< RetirementYear > savingsTableRows = FXCollections.observableArrayList();

    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance( Locale.US );
    private NumberFormat percentFormatter = NumberFormat.getPercentInstance( Locale.US );
    private NumberFormat integerFormatter = NumberFormat.getIntegerInstance( Locale.US );

    private Dictionary< String, Slider > sliders = new Hashtable<>();
    private Dictionary< String, TextField > textFields = new Hashtable<>();
    private BarChart< Number, Number > barChart;
    private BarChart< Number, Number > largeBarChart;
    private XYChart.Series withoutEmployerData = new XYChart.Series();
    private XYChart.Series withEmployerData = new XYChart.Series();
    private XYChart.Series largeWithoutEmployerData = new XYChart.Series();
    private XYChart.Series largeWithEmployerData = new XYChart.Series();
    private ArrayList< RetirementYear > retirementYears;
    private Label employeeContributions;
    private Label employerContributions;
    private WebView webView;
    private WebEngine webEngine;

    private TableView savingsTable;
    private TableColumn ageCol, contribCol, employerMatchCol, balWithoutMatchCol, balWithMatchCol;
    private String definitionsLocation;
    private GridPane gridPane;

    /**
     * Initialize the controller and set the nodes based on the View.
     *
     * @param gridPane           The parent node for the interface (Sliders and TextFields).
     * @param barChart           The barChart on the front page.
     * @param largeBarChart      The Detailed section's barChart.
     * @param webView            The webView used to view the definitions file.
     * @param savingsTable       The Table in the Detailed View that has a yearly breakdown.
     * @param ageCol             The year column of the savingsTable.
     * @param contribCol         The employee contribution column of the savingsTable.
     * @param employerMatchCol   The employer contribution column of the savingsTable.
     * @param balWithoutMatchCol The year end balance w/o employer matching column of the savingsTable.
     * @param balWithMatchCol    The year end balance w/ employer matching column of the savingsTable.
     */
    public RetirementController( GridPane gridPane, BarChart< Number, Number > barChart, BarChart< Number, Number > largeBarChart, WebView webView,
                                 TableView savingsTable, TableColumn ageCol, TableColumn contribCol, TableColumn employerMatchCol,
                                 TableColumn balWithoutMatchCol, TableColumn balWithMatchCol ) {
        this.barChart = barChart;
        this.largeBarChart = largeBarChart;
        this.savingsTable = savingsTable;
        this.ageCol = ageCol;
        this.contribCol = contribCol;
        this.employerMatchCol = employerMatchCol;
        this.balWithoutMatchCol = balWithoutMatchCol;
        this.balWithMatchCol = balWithMatchCol;
        this.webView = webView;
        this.gridPane = gridPane;

        initialize();
    }

    /** Initialize all elements within the view. */
    private void initialize() {
        parseNodes();

        initFormatters();
        initSliders();
        initTextFields();
        initWebView();
        initCharts();
        initSavingsTable();

        updateAllTextFields();
        updateRetirementCalc();
    }

    /** Set up the formatters. Mostly a stub. */
    private void initFormatters() {
        percentFormatter.setMaximumFractionDigits( 2 );
    }

    /** Initialize the definitions tab to the local web page. */
    private void initWebView() {
        webEngine = webView.getEngine();
        //load the definitions page from the resources folder.
        webEngine.load( getClass().getResource( "../view/definitions.html" ).toExternalForm() );
        definitionsLocation = webEngine.getLocation();
    }

    /** Initialize the Table on the detailed view. */
    private void initSavingsTable() {
        ageCol.setCellValueFactory( new PropertyValueFactory< RetirementYear, String >( "year" ) );
        contribCol.setCellValueFactory( new PropertyValueFactory< RetirementYear, String >( "contribution" ) );
        employerMatchCol.setCellValueFactory( new PropertyValueFactory< RetirementYear, String >( "employerContribution" ) );
        balWithoutMatchCol.setCellValueFactory( new PropertyValueFactory< RetirementYear, String >( "yearEndBalance" ) );
        balWithMatchCol.setCellValueFactory( new PropertyValueFactory< RetirementYear, String >( "yearEndBalanceWithEmployer" ) );

        savingsTable.setItems( savingsTableRows );
    }

    /** Initialize the two barCharts. */
    private void initCharts() {
        withoutEmployerData.setName( "Without Employer Contributions" );
        withEmployerData.setName( "With Employer Contributions" );

        //Chart on the first tab
        barChart.getXAxis().setLabel( "Year" );
        barChart.getYAxis().setLabel( "Dollars" );
        barChart.getData().add( withoutEmployerData );
        barChart.getData().add( withEmployerData );

        //Chart on the second tab
        largeBarChart.getXAxis().setLabel( "Year" );
        largeBarChart.getYAxis().setLabel( "Dollars" );
        largeBarChart.getData().add( largeWithoutEmployerData );
        largeBarChart.getData().add( largeWithEmployerData );
    }

    /** Parse the GridPane for all of it children, then put them into Dictionaries based on their prefix. */
    private void parseNodes() {
        for ( Node n : gridPane.getChildren() ) {
            if ( n.getId() != null ) {
                if ( n instanceof Slider )
                    sliders.put( n.getId().replace( "Slider", "" ), ( Slider ) n );
                else if ( n instanceof TextField )
                    textFields.put( n.getId().replace( "Text", "" ), ( TextField ) n );
                else if ( n instanceof Label ) {
                    if ( n.getId().equals( "employeeContributions" ) )
                        employeeContributions = ( Label ) n;
                    else if ( n.getId().equals( "employerContributions" ) )
                        employerContributions = ( Label ) n;
                }
            }
        }
    }

    /** Initialize all of the TextFields.  Add handlers for events */
    private void initTextFields() {
        Enumeration< TextField > elements = textFields.elements();
        while ( elements.hasMoreElements() ) {
            TextField textField = elements.nextElement();

            textField.addEventHandler( KeyEvent.KEY_TYPED, new KeyHandler( true ) );
            textField.addEventHandler( Event.ANY, new TextFieldHandler() );

            //Lambda to update the textFields when the focus changes.
            textField.focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                updateAllTextFields();
            } );

            //Lambda to recalculate when the value has changed.
            textField.textProperty().addListener( ( observable, oldValue, newValue ) -> {
                updateRetirementCalc();
            } );
        }
    }

    /** Initialize all the Sliders with their handlers and their formatting preference. */
    private void initSliders() {
        Enumeration< Slider > elements = sliders.elements();

        while ( elements.hasMoreElements() ) {
            Slider slider = elements.nextElement();
            slider.setUserData( "none" );
            slider.addEventHandler( Event.ANY, new SliderHandler() );
        }

        sliders.get( "percentContribute" ).setLabelFormatter( new PercentSliderFormatter() );
        sliders.get( "percentContribute" ).setUserData( "percent" );

        sliders.get( "salaryIncrease" ).setLabelFormatter( new PercentSliderFormatter() );
        sliders.get( "salaryIncrease" ).setUserData( "percent" );

        sliders.get( "rateOfReturn" ).setLabelFormatter( new PercentSliderFormatter() );
        sliders.get( "rateOfReturn" ).setUserData( "percent" );

        sliders.get( "employerMatch" ).setLabelFormatter( new PercentSliderFormatter() );
        sliders.get( "employerMatch" ).setUserData( "percent" );

        sliders.get( "employerMatchEnds" ).setLabelFormatter( new PercentSliderFormatter() );
        sliders.get( "employerMatchEnds" ).setUserData( "percent" );

        sliders.get( "salary" ).setUserData( "dollar" );
        sliders.get( "balance" ).setUserData( "dollar" );
    }

    /** Update the TextField to align with the Sliders. */
    private void updateAllTextFields() {
        Enumeration< TextField > fields = textFields.elements();

        while ( fields.hasMoreElements() ) {
            TextField textField = fields.nextElement();
            Slider slider = sliders.get( textField.getId().replace( "Text", "" ) );

            sliderToTextField( slider, textField );
        }
    }

    /** Parse the TextField and then recalculate the retirement plan.  This will update the charts and the table. */
    private void updateRetirementCalc() {
        //simple sanity check
        if ( allFieldsFilled() ) {
            try {
                int years = Integer.parseInt( textFields.get( "retirementAge" ).getText() ) - Integer.parseInt( textFields.get( "currentAge" ).getText() );

                //is the sliders move past each other, the value will be negative, and will fail here.
                if ( years < 1 )
                    throw new NumberFormatException();

                double rate = getParsedValue( "rateOfReturn", "[%]" ) / 100;
                double salary = getParsedValue( "salary", "[$,]" );
                double salaryIncrease = getParsedValue( "salaryIncrease", "[%]" ) / 100;
                double contribution = getParsedValue( "percentContribute", "[%]" ) / 100;
                double startingBalance = getParsedValue( "balance", "[$,]" );
                double employerMatch = getParsedValue( "employerMatch", "[%]" ) / 100;
                double employerMax = getParsedValue( "employerMatchEnds", "[%]" ) / 100;

                retirementYears = new ArrayList< RetirementYear >( RetirementCalculator.calculateRetirement(
                        12, years, rate, salary, salaryIncrease,
                        contribution, startingBalance, employerMatch, employerMax ) );

                recalculateBarGraph( Integer.parseInt( textFields.get( "currentAge" ).getText() ) );
                savingsTableRows.setAll( retirementYears );

            } catch ( NumberFormatException nfe ) {
                //  Thrown if any of the fields failed to parse, not a fatal issue, so I ignore it.
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Helper method to extract the data from a TextField.
     *
     * @param name  The name of the TextField.
     * @param regex The character to extract from the TextField before parsing.
     * @return The parsed TextField as a double.
     */
    private double getParsedValue( String name, String regex ) {
        return Double.parseDouble( textFields.get( name ).getText().replaceAll( regex, "" ) );
    }

    /** @return True if all fields are filled. */
    private boolean allFieldsFilled() {
        Enumeration< TextField > fields = textFields.elements();
        boolean filled = true;

        while ( fields.hasMoreElements() && filled ) {
            filled = !fields.nextElement().getText().isEmpty();
        }

        return filled;
    }

    /**
     * Rebuild the graph based on the newly calculated retirement data.
     *
     * @param startYear The first year of the retirement.
     */
    private void recalculateBarGraph( int startYear ) {
        clearDataSets();

        double contributions = 0;
        double employerContrib = 0;

        for ( RetirementYear r : retirementYears ) {
            withoutEmployerData.getData().add( new XYChart.Data< String, Number >( "" + ( startYear + r.getYear() ), r.getYearEndBalance() ) );
            largeWithoutEmployerData.getData().add( new XYChart.Data< String, Number >( "" + ( startYear + r.getYear() ), r.getYearEndBalance() ) );

            withEmployerData.getData().add( new XYChart.Data< String, Number >( "" + ( startYear + r.getYear() ), r.getYearEndBalanceWithEmployer() ) );
            largeWithEmployerData.getData().add( new XYChart.Data< String, Number >( "" + ( startYear + r.getYear() ), r.getYearEndBalanceWithEmployer() ) );

            contributions += r.getContribution();
            employerContrib += r.getEmployerContribution();
        }

        calculateChartLabels( contributions, employerContrib );
    }

    /**
     * After a new set of data is calculate, the chart must be updated along with the labels in the two sections.
     *
     * @param contributions   The total employee contributions.
     * @param employerContrib The total employer contributions.
     */
    private void calculateChartLabels( double contributions, double employerContrib ) {
        RetirementYear lastYear = retirementYears.get( retirementYears.size() - 1 );

        //update the chart legend
        withoutEmployerData.setName( "Without Employer Contributions " + currencyFormatter.format( lastYear.getYearEndBalance() ) );
        largeWithoutEmployerData.setName( "Without Employer Contributions " + currencyFormatter.format( lastYear.getYearEndBalance() ) );

        withEmployerData.setName( "With Employer Contributions " + currencyFormatter.format( lastYear.getYearEndBalanceWithEmployer() ) );
        largeWithEmployerData.setName( "With Employer Contributions " + currencyFormatter.format( lastYear.getYearEndBalanceWithEmployer() ) );

        //update labels inside the form
        employeeContributions.setText( currencyFormatter.format( contributions ) );
        employerContributions.setText( currencyFormatter.format( employerContrib ) );
    }

    /** Wipe the old data from the dataSets before a new retirement calculation is committed. */
    private void clearDataSets() {
        withoutEmployerData.getData().clear();
        largeWithoutEmployerData.getData().clear();

        withEmployerData.getData().clear();
        largeWithEmployerData.getData().clear();
    }

    /**
     * When one of the "?" buttons are pressed, this will direct the accordion view to expand the definitions tab and
     * then navigate to the definition's location.
     *
     * @param event The click event sent from the "?" button.
     */
    public void tooltipButtonClicked( ActionEvent event ) {
        //Load the definition anchor location
        webEngine.load( definitionsLocation + "#" + ( ( Node ) event.getSource() ).getId() );

        //expand the definition tab
        ( ( Accordion ) webView.getScene().lookup( "#mainAccordian" ) ).setExpandedPane(
                ( TitledPane ) webView.getScene().lookup( "#definitionsPane" ) );
    }

    /**
     * Helper method to translate the value of a Slider into the correct format for a TextField
     *
     * @param slider    The source Slider.
     * @param textField The destination TextField.
     */
    private void sliderToTextField( Slider slider, TextField textField ) {
        if ( slider.getUserData().equals( "percent" ) )
            textField.setText( percentFormatter.format( slider.getValue() / 100 ) );

        else if ( slider.getUserData().equals( "dollar" ) )
            textField.setText( currencyFormatter.format( slider.getValue() ) );

        else
            textField.setText( integerFormatter.format( slider.getValue() ) );
    }

    /** SubClass that contains the code needed to transfer values from a Slider into a TextField. */
    class SliderHandler implements EventHandler {
        /**
         * Push the value of a Slider into it's matching TextField
         *
         * @param event Any event from the Slider.
         */
        @Override
        public void handle( Event event ) {
            Slider slider = ( Slider ) event.getSource();
            TextField textField = textFields.get( slider.getId().replace( "Slider", "" ) );

            sliderToTextField( slider, textField );
        }
    }

    /** SubClass that contains the code needed to transfer values from a TextField into a Slider. */
    class TextFieldHandler implements EventHandler {
        /**
         * Push the value of a TextField into it's matching Slider.
         *
         * @param event Any event from the TextField.
         */
        @Override
        public void handle( Event event ) {
            try{
                TextField textField = ( TextField ) event.getSource();
                Slider slider = sliders.get( textField.getId().replace( "Text", "" ) );
                String textFieldValue = textField.getText().replaceAll( "[%$-,]", "" );

                if ( !textFieldValue.isEmpty() ) {
                    double value = Double.parseDouble( textFieldValue );

                    if ( slider.getValue() != value ) {
                        //clamp the value of the TextField
                        if ( value > slider.getMax() )
                            value = slider.getMax();
                        else if ( value < slider.getMin() )
                            value = slider.getMin();

                        slider.setValue( value );
                    }
                }
            }
            catch( NumberFormatException nfe){
                //
            }
        }
    }
}