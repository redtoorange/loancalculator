package controller;

import model.RetirementYear;
import utilities.MathUtil;
import org.apache.poi.ss.formula.functions.FinanceLib;

import java.util.ArrayList;

/**
 * RetirementCalculator.java - Calculator that computes a retirement plan.
 *
 * @author Andrew McGuiness
 * @version 03/Apr/2017
 */
public class RetirementCalculator {
    /**
     * Calculate a retirement plan.  Interest is compounded yearly, new values are calculated, and then a retirement
     * year object is created and saved.
     *
     * @param nper            Payments per year.
     * @param years           Number of years to calculate.
     * @param rate            The annual projected rate of return.
     * @param salary          The salary of the employee.
     * @param salaryIncrease  The projected annual salary increase.
     * @param contribution    The percent of the salary the employee will contribute annually.
     * @param startingBalance The starting balance of the retirement account.
     * @param employerMatch   The amount the employer will match.
     * @param employerMax     The maximum percent of the employee's salary the employer will match.
     * @return An ArrayList of RetirementYears that represents the life of the retirement plan.
     */
    public static ArrayList< RetirementYear > calculateRetirement( int nper, int years, double rate, double salary, double salaryIncrease, double contribution, double startingBalance, double employerMatch, double employerMax ) {
        ArrayList< RetirementYear > retirementYears = new ArrayList< RetirementYear >();

        //calculate initial values
        double annualContributionAmount = salary * contribution;
        double balance = startingBalance;
        double balanceEmployer = startingBalance;

        double employerMatchAmount = annualContributionAmount * employerMatch;
        double employerMaxAmount = salary * employerMax * employerMatch;

        //clamp the amount of money to the max.
        employerMatchAmount = MathUtil.clamp( employerMatchAmount, 0, employerMaxAmount );


        for ( int i = 1; i < years; i++ ) {
            //calculate new values for this year
            double interest = FinanceLib.fv( rate / nper, nper, -( annualContributionAmount ) / nper, -balance, false ) - ( balance + annualContributionAmount );
            double interestEmployer = FinanceLib.fv( rate / nper, nper, -( annualContributionAmount + employerMatchAmount ) / nper, -balanceEmployer, false ) - ( balanceEmployer + annualContributionAmount + employerMatchAmount );

            double newBalance = balance + interest + annualContributionAmount;
            double newBalanceEmployer = balanceEmployer + interestEmployer + annualContributionAmount + employerMatchAmount;

            //Add year to the collection
            retirementYears.add( new RetirementYear( i, balance, newBalance, salary, annualContributionAmount, interest, interestEmployer, employerMatchAmount, newBalanceEmployer, balanceEmployer ) );

            //set old values to new values
            balance = newBalance;
            balanceEmployer = newBalanceEmployer;

            //recalculate the salary and contributions
            salary = salary + salary * salaryIncrease;
            annualContributionAmount = salary * contribution;

            employerMatchAmount = annualContributionAmount * employerMatch;
            employerMaxAmount = salary * employerMax * employerMatch;

            //clamp the amount of money to the max.
            employerMatchAmount = MathUtil.clamp( employerMatchAmount, 0, employerMaxAmount );
        }

        return retirementYears;
    }
}