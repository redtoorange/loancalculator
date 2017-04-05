package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;

/**
 * MainViewController.java - This is the primary controller that JavaFX will inject into.  It maintains references to two
 * sub controllers that manage the individual tabs.  This is a modular approach, as adding more tabs would only require
 * the addition of more sub controllers.  The primary job of this controller is to maintain links to all fx:id's and to
 * route event's.
 *
 * @author Andrew McGuiness
 * @version 29/Mar/2017
 */
public class MainViewController {
    @FXML
    private ChoiceBox< String > termChoiceBox;
    @FXML
    private TextField loanAmount, termCount, interestRate;
    @FXML
    private DatePicker startDate;
    @FXML
    private Button calculateButton;
    @FXML
    private TableView loanTable, savingsTable;
    @FXML
    private TableColumn dateCol, paymentCol, principalCol, interestCol, totalInterestCol, balanceCol;
    @FXML
    private TableColumn ageCol, contribCol, employerMatchCol, balWithoutMatchCol, balWithMatchCol;
    @FXML
    private PieChart pieChart;
    @FXML
    private WebView webView;
    @FXML
    private GridPane sliderFieldPane;
    @FXML
    private BarChart< Number, Number > barChart;
    @FXML
    private BarChart< Number, Number > largeBarChart;

    private RetirementController retirementController;
    private LoanController loanController;


    /**
     * Construct the two sub-controllers and allocate them all of the relevant fx:id's
     */
    @FXML
    public void initialize() {
        retirementController = new RetirementController( sliderFieldPane, barChart, largeBarChart, webView, savingsTable,
                ageCol, contribCol, employerMatchCol, balWithoutMatchCol, balWithMatchCol );

        loanController = new LoanController( termChoiceBox, loanAmount, termCount, interestRate, startDate, calculateButton,
                loanTable, dateCol, paymentCol, principalCol, interestCol, totalInterestCol, balanceCol, pieChart );

    }

    /**
     * Event that is passed into the LoanController for handling.
     *
     * @param event ActionEvent passed in by the Loan View's "calculate" button.
     */
    public void calculateButtonClicked( ActionEvent event ) {
        loanController.calculateButtonClicked( event );
    }

    /**
     * Event that is passed into the RetirementController for handling.
     *
     * @param event ActionEvent passed in by the Retirement View's "?" buttons.
     */
    public void tooltipButtonClicked( ActionEvent event ) {
        retirementController.tooltipButtonClicked( event );
    }
}
