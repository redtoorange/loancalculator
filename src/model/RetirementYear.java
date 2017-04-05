package model;

import javafx.beans.property.SimpleStringProperty;

import java.text.NumberFormat;

/**
 * RetirementYear.java - A model that represents a single year within a retirement plan.  The class is used primarily
 * used to hold data so that it can be pushed into a TableView.  The class is immutable.
 *
 * @author Andrew McGuiness
 * @version 03/Apr/2017
 */
public class RetirementYear {
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    private final int year;
    private final double yearStartBalance;
    private final double yearEndBalance;
    private final double yearEndBalanceWithEmployer;
    private final double yearStartBalanceWithEmployer;
    private final double salary;
    private final double contribution;
    private final double interestEarned;
    private final double interestEarnedEmployer;
    private final double employerContribution;

    /**
     * Construct an immutable representation of a single year's interest and contributions for a 401(k) plan.
     *
     * @param year                         The year that this RetirementYear represents.
     * @param yearStartBalance             The starting balance of this year wo/employer matching.
     * @param yearEndBalance               The ending balance of this year wo/employer matching.
     * @param salary                       The annual salary for this year.
     * @param contribution                 The total employee contribution this year.
     * @param interestEarned               The interest earned wo/employer matching.
     * @param interestEarnedEmployer       The interest earned w/employer matching.
     * @param employerContribution         The employer's contribution.
     * @param yearEndBalanceWithEmployer   The ending balance of this year w/employer matching.
     * @param yearStartBalanceWithEmployer The starting balance of this year w/employer matching.
     */
    public RetirementYear( int year, double yearStartBalance, double yearEndBalance, double salary, double contribution,
                           double interestEarned, double interestEarnedEmployer, double employerContribution,
                           double yearEndBalanceWithEmployer, double yearStartBalanceWithEmployer ) {
        this.year = year;
        this.yearStartBalance = yearStartBalance;
        this.yearEndBalance = yearEndBalance;
        this.salary = salary;
        this.contribution = contribution;
        this.interestEarned = interestEarned;
        this.interestEarnedEmployer = interestEarnedEmployer;
        this.employerContribution = employerContribution;

        this.yearEndBalanceWithEmployer = yearEndBalanceWithEmployer;
        this.yearStartBalanceWithEmployer = yearStartBalanceWithEmployer;
    }

    /** @return Year of payment. */
    public final int getYear() {
        return year;
    }

    /** @return Employer's contribution this year. */
    public final double getEmployerContribution() {
        return employerContribution;
    }

    /** @return Year's start balance without employer matching. */
    public final double getYearStartBalance() {
        return yearStartBalance;
    }

    /** @return Year's end balance without employer matching. */
    public final double getYearEndBalance() {
        return yearEndBalance;
    }

    /** @return Employee's salary this year. */
    public final double getSalary() {
        return salary;
    }

    /** @return Employee's contribution this year. */
    public final double getContribution() {
        return contribution;
    }

    /** @return Year's interest earned without employer matching. */
    public final double getInterestEarned() {
        return interestEarned;
    }

    /** @return Year's end balance with employer matching. */
    public final double getYearEndBalanceWithEmployer() {
        return yearEndBalanceWithEmployer;
    }

    /** @return Year's start balance with employer matching. */
    public final double getYearStartBalanceWithEmployer() {
        return yearStartBalanceWithEmployer;
    }

    /** @return Year's interest earned with employer matching. */
    public final double getInterestEarnedEmployer() {
        return interestEarnedEmployer;
    }

    /**
     * Getter method that converts the year into a SimpleStringProperty to
     * be used by the TableView.
     *
     * @return SimpleStringProperty of the year.
     */
    public SimpleStringProperty yearProperty() {
        return new SimpleStringProperty( year + "" );
    }

    /**
     * Getter method that converts the year end balance wo/employer matching into a SimpleStringProperty to
     * be used by the TableView.
     *
     * @return SimpleStringProperty of the year end balance wo/employer formatted as currency.
     */
    public SimpleStringProperty yearEndBalanceProperty() {
        return new SimpleStringProperty( formatter.format( yearEndBalance ) );
    }

    /**
     * Getter method that converts the employee contribution amount into a SimpleStringProperty to
     * be used by the TableView.
     *
     * @return SimpleStringProperty of the employee contribution amount formatted as currency.
     */
    public SimpleStringProperty contributionProperty() {
        return new SimpleStringProperty( formatter.format( contribution ) );
    }

    /**
     * Getter method that converts the employer contribution amount into a SimpleStringProperty to
     * be used by the TableView.
     *
     * @return SimpleStringProperty of the employer contribution amount formatted as currency.
     */
    public SimpleStringProperty employerContributionProperty() {
        return new SimpleStringProperty( formatter.format( employerContribution ) );
    }

    /**
     * Getter method that converts the year end balance w/employer matching into a SimpleStringProperty to
     * be used by the TableView.
     *
     * @return SimpleStringProperty of the year end balance w/employer formatted as currency.
     */
    public SimpleStringProperty yearEndBalanceWithEmployerProperty() {
        return new SimpleStringProperty( formatter.format( yearEndBalanceWithEmployer ) );
    }
}
