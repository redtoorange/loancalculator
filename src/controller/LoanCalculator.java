package controller;

import model.LoanPayment;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * LoanCalculator.java - Calculator that calculates the monthly payments required to pay off a loan.
 *
 * @author Andrew McGuiness
 * @version 03/Apr/2017
 */
public class LoanCalculator {
    /**
     * Calculate the LoanPayments required to pay off the loan.
     *
     * @param startDate    Start date of the loan.
     * @param principle    The starting principal of the loan.
     * @param interestRate The annual interest rate of the loan.
     * @param payments     The number of payments.
     * @param monthly      Are the payments made monthly or yearly?
     * @return An ArrayList of LoanPayments that represents the life of the loan.
     */
    public static ArrayList< LoanPayment > buildLoanTable( LocalDate startDate, double principle, double interestRate,
                                                           int payments, boolean monthly ) {

        ArrayList< LoanPayment > loanPayments = new ArrayList< LoanPayment >();

        //Initial values.
        double standingBalance;
        double monthlyInterest = interestRate / 12.0 / 100.0;
        double numberPayments = ( monthly ) ? payments : ( payments * 12 );
        double monthlyPayment, interestPayment, principlePayment;
        double interestPaid = 0;

        //increment the month by one, then calculate the require payments.
        startDate = startDate.plusMonths( 1 );
        monthlyPayment = paymentAmount( monthlyInterest, numberPayments, principle );

        //Handle all but the final month of payments.
        for ( int i = 1; i < numberPayments; i++ ) {
            //calculate this month's values
            interestPayment = principle * monthlyInterest;
            interestPaid += interestPayment;
            principlePayment = monthlyPayment - interestPayment;
            standingBalance = principle - principlePayment;

            //write the data into the collection
            loanPayments.add( new LoanPayment( startDate, monthlyPayment, principlePayment, interestPayment,
                    interestPaid, standingBalance ) );

            //set old values
            startDate = startDate.plusMonths( 1 );
            principle = standingBalance;
        }

        //Handle the final month of the loan (will be less than payment amount).
        principlePayment = principle;
        interestPayment = principle * monthlyInterest;
        monthlyPayment = principlePayment + interestPayment;
        interestPaid += interestPayment;
        standingBalance = 0;

        loanPayments.add( new LoanPayment( startDate, monthlyPayment, principlePayment, interestPayment,
                interestPaid, standingBalance ) );

        return loanPayments;
    }

    /**
     * Helper method to calculate the monthly payments required.
     *
     * @param monthlyInterestRate Amount of interest accrued per month
     * @param numberOfPeriods     The number of periods that the loan is spread over.
     * @param principal           The starting amount of the loan.
     * @return The monthly payment required to pay off the loan in the given number of periods.
     */
    private static double paymentAmount( double monthlyInterestRate, double numberOfPeriods, double principal ) {
        return -1 * principal * Math.pow( monthlyInterestRate + 1, numberOfPeriods ) * monthlyInterestRate / ( 1 - Math.pow( monthlyInterestRate + 1, numberOfPeriods ) );
    }
}