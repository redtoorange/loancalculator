package controller;

import model.LoanPayment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

/**
 * LoanController.java - The controller for the Loan View tab of the Application.  The calculations are handed
 * off to a separate class.  This class only handle events and updating the view elements.
 *
 * @author Andrew McGuiness
 * @version 03/Apr/2017
 */
public class LoanController {
    private ObservableList< LoanPayment > loanPayments = FXCollections.observableArrayList();

    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance( Locale.US );
    private NumberFormat percentFormatter = NumberFormat.getPercentInstance( Locale.US );
    private NumberFormat integerFormatter = NumberFormat.getIntegerInstance( Locale.US );

    private ChoiceBox< String > termChoiceBox;
    private TextField loanAmount, termCount, interestRate;
    private DatePicker startDate;
    private Button calculateButton;
    private TableView loanTable;
    private TableColumn dateCol, paymentCol, principalCol, interestCol, totalInterestCol, balanceCol;
    private PieChart pieChart;
    private double totalInterest;
    private double totalPrincipal;

    /**
     * Initialize the controller and set the nodes based on the View.
     *
     * @param termChoiceBox    The box that contains the choice of term length.
     * @param loanAmount       The principal of the loan.
     * @param termCount        The number of terms to spread the loan over.
     * @param interestRate     The yearly interest rate for the loan.
     * @param startDate        The start date of the loan
     * @param calculateButton  The button that will fire the calculation of the loan.
     * @param loanTable        The table that contains the loan break down by month.
     * @param dateCol          The date column of the LoanTable.
     * @param paymentCol       The total payment column of the LoanTable.
     * @param principalCol     The amount of payment that goes to principal column of the LoanTable.
     * @param interestCol      The amount of payment that foes to interest column of the LoanTable.
     * @param totalInterestCol The total interest paid to date column of the LoanTable.
     * @param balanceCol       The remaining balance column of the LoanTable.
     * @param pieChart         The pie chart that holds out data.
     */
    public LoanController( ChoiceBox< String > termChoiceBox, TextField loanAmount, TextField termCount,
                           TextField interestRate, DatePicker startDate, Button calculateButton, TableView loanTable,
                           TableColumn dateCol, TableColumn paymentCol, TableColumn principalCol, TableColumn interestCol,
                           TableColumn totalInterestCol, TableColumn balanceCol, PieChart pieChart ) {

        this.termChoiceBox = termChoiceBox;
        this.loanAmount = loanAmount;
        this.termCount = termCount;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.calculateButton = calculateButton;
        this.loanTable = loanTable;
        this.dateCol = dateCol;
        this.paymentCol = paymentCol;
        this.principalCol = principalCol;
        this.interestCol = interestCol;
        this.totalInterestCol = totalInterestCol;
        this.balanceCol = balanceCol;
        this.pieChart = pieChart;

        init();
    }

    /** Initialize the controller. */
    private void init() {
        initFormatters();

        initChoiceBox();
        initTextFields();
        initLoanTable();

        updateAllTextFields();
    }

    /** Set up the formatters. Mostly a stub. */
    private void initFormatters() {
        percentFormatter.setMaximumFractionDigits( 2 );
    }

    /** Initialize the table to be able to parse the LoanPayment objects. */
    private void initLoanTable() {
        dateCol.setCellValueFactory( new PropertyValueFactory< LoanPayment, String >( "date" ) );
        paymentCol.setCellValueFactory( new PropertyValueFactory< LoanPayment, String >( "payment" ) );
        principalCol.setCellValueFactory( new PropertyValueFactory< LoanPayment, String >( "principal" ) );
        interestCol.setCellValueFactory( new PropertyValueFactory< LoanPayment, String >( "interest" ) );
        totalInterestCol.setCellValueFactory( new PropertyValueFactory< LoanPayment, String >( "totalInterest" ) );
        balanceCol.setCellValueFactory( new PropertyValueFactory< LoanPayment, String >( "balance" ) );

        loanTable.setItems( loanPayments );
    }

    /** Initialize the choice box with values. */
    private void initChoiceBox() {
        termChoiceBox.setItems( FXCollections.observableArrayList(
                "Yearly", "Monthly"
        ) );

        termChoiceBox.getSelectionModel().selectFirst();
        startDate.setValue( LocalDate.now() );
    }

    /** Add handlers to all TextFields. */
    private void initTextFields() {
        loanAmount.addEventHandler( KeyEvent.KEY_TYPED, new KeyHandler( true ) );
        loanAmount.focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
            updateAllTextFields();
        } );

        termCount.addEventHandler( KeyEvent.KEY_TYPED, new KeyHandler( false ) );
        termCount.focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
            updateAllTextFields();
        } );

        interestRate.addEventHandler( KeyEvent.KEY_TYPED, new KeyHandler( true ) );
        interestRate.focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
            updateAllTextFields();
        } );
    }

    /**
     * Calculate new loan data based on the TextFields.
     *
     * @param event Not Used.
     */
    public void calculateButtonClicked( ActionEvent event ) {
        if ( fieldsFilled() ) {
            try {
                Double la = new Double( loanAmount.getText().replaceAll( "[$,]", "" ) );
                Double ir = new Double( interestRate.getText().replace( "%", "" ) );
                Integer tc = new Integer( termCount.getText() );

                loanPayments.clear();

                boolean monthly = false;
                if ( termChoiceBox.getValue().equals( "Monthly" ) )
                    monthly = true;


                loanPayments.setAll( LoanCalculator.buildLoanTable( startDate.getValue(), la, ir, tc, monthly ) );
                totalInterest = loanPayments.get( loanPayments.size() - 1 ).getTotalInterest();
                totalPrincipal = la;

                updatePieChart();
            } catch ( NumberFormatException nfe ) {
                //
            }
        }
    }

    /** Reset the PieChart and insert new information based on a new loan calculation. */
    private void updatePieChart() {
        pieChart.setData( FXCollections.observableArrayList(
                new PieChart.Data( "Interest", totalInterest / ( totalInterest + totalPrincipal ) ),
                new PieChart.Data( "Principal", totalPrincipal / ( totalInterest + totalPrincipal ) )
        ) );
    }

    /** Update the formatting of all TextFields based on their formatters. */
    private void updateAllTextFields() {
        if ( fieldsFilled() ) {
            try {
                Double la = new Double( loanAmount.getText().replaceAll( "[$,]", "" ) );
                Double ir = new Double( interestRate.getText().replace( "%", "" ) );
                Integer tc = new Integer( termCount.getText() );

                loanAmount.setText( currencyFormatter.format( la ) );
                termCount.setText( integerFormatter.format( tc ) );
                interestRate.setText( percentFormatter.format( ir / 100 ) );
            } catch ( NumberFormatException nfe ) {
                //
            }
        }
    }

    /** @return True if all fields are filled. */
    private boolean fieldsFilled() {
        return ( !loanAmount.getText().isEmpty() && !interestRate.getText().isEmpty() && !termCount.getText().isEmpty() );
    }
}