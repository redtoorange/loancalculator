package model;

import javafx.beans.property.SimpleStringProperty;

import java.text.NumberFormat;
import java.time.LocalDate;

/**
 * LoanPayment.java - A model class representing a single monthly payment on a loan.  The class is used primarily
 * used to hold data so that it can be pushed into a TableView.  The class is immutable.
 *
 * @author Andrew McGuiness
 * @version 03/Apr/2017
 */
public class LoanPayment {
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    private final LocalDate date;
    private final double payment;
    private final double principal;
    private final double interest;
    private final double totalInterest;
    private final double balance;

    /**
     * Construct an immutable representation of a single month's payment on a loan.
     *
     * @param date          The date that the payment was made.
     * @param payment       The amount of the payment.
     * @param principal     The portion of the payment that was paid to the principal.
     * @param interest      The portion of the payment that was paid to interest.
     * @param totalInterest The total interest paid on the loan to date.
     * @param balance       The remaining balance on the loan.
     */
    public LoanPayment( LocalDate date, double payment, double principal, double interest, double totalInterest,
                        double balance ) {
        this.date = date;
        this.payment = payment;
        this.principal = principal;
        this.interest = interest;
        this.totalInterest = totalInterest;
        this.balance = balance;
    }

    /** @return Year of payment. */
    public final LocalDate getDate() {
        return date;
    }

    /** @return Year of payment. */
    public final double getPayment() {
        return payment;
    }

    /** @return Year of payment. */
    public final double getPrincipal() {
        return principal;
    }

    /** @return Year of payment. */
    public final double getInterest() {
        return interest;
    }

    /** @return Year of payment. */
    public final double getTotalInterest() {
        return totalInterest;
    }

    /** @return Year of payment. */
    public final double getBalance() {
        return balance;
    }


    /**
     * Getter method that converts the date into a SimpleStringProperty to be used by the TableView.
     *
     * @return SimpleStringProperty of the payment Month and Year.
     */
    public final SimpleStringProperty dateProperty() {
        return new SimpleStringProperty( date.getMonth() + " " + date.getYear() );
    }

    /**
     * Getter method that converts the total payment this month into a SimpleStringProperty to
     * be used by the TableView.
     *
     * @return SimpleStringProperty of the payment amount formatted as currency.
     */
    public SimpleStringProperty paymentProperty() {
        return new SimpleStringProperty( formatter.format( payment ) );
    }

    /**
     * Getter method that converts the principal paid this month into a SimpleStringProperty to
     * be used by the TableView.
     *
     * @return SimpleStringProperty of the amount paid to principal formatted as currency.
     */
    public SimpleStringProperty principalProperty() {
        return new SimpleStringProperty( formatter.format( principal ) );
    }

    /**
     * Getter method that converts the interest paid this month into a SimpleStringProperty to
     * be used by the TableView.
     *
     * @return SimpleStringProperty of the amount paid to interest formatted as currency.
     */
    public SimpleStringProperty interestProperty() {
        return new SimpleStringProperty( formatter.format( interest ) );
    }

    /**
     * Getter method that converts the total interest paid into a SimpleStringProperty to be used
     * by the TableView.
     *
     * @return SimpleStringProperty of the total interest paid formatted as currency.
     */
    public SimpleStringProperty totalInterestProperty() {
        return new SimpleStringProperty( formatter.format( totalInterest ) );
    }

    /**
     * Getter method that converts the remaining balance into a SimpleStringProperty to be used
     * by the TableView.
     *
     * @return SimpleStringProperty of the remaining balance formatted as currency.
     */
    public SimpleStringProperty balanceProperty() {
        return new SimpleStringProperty( formatter.format( balance ) );
    }
}
