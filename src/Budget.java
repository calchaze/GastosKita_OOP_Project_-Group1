import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;

/** Tracks the user's monthly budget and remaining money. */
public class Budget {
    public enum Status { NONE, WITHIN, NEARING, EXCEEDED }

    private double totalBudget;
    private double totalSpent;
    private double remainingBudget;

    public void setBudget(double amount) throws InvalidInputException {
        if (Double.isNaN(amount) || amount <= 0)
            throw new InvalidInputException("Budget must be greater than zero.");
        this.totalBudget = amount;
        calculateRemaining();
    }

    public double calculateRemaining() {
        remainingBudget = totalBudget - totalSpent;
        return remainingBudget;
    }

    /** Within (< 80%), nearing (80%-100%), or exceeded (> 100%). */
    public Status checkBudget() {
        if (totalBudget <= 0) return Status.NONE;
        double ratio = totalSpent / totalBudget;
        if (ratio > 1.0)  return Status.EXCEEDED;
        if (ratio >= 0.8) return Status.NEARING;
        return Status.WITHIN;
    }

    /** Loads the budget and this month's total spending from MySQL. */
    public void reload(ExpenseDAO dao) throws SQLException {
        YearMonth ym = YearMonth.from(LocalDate.now());
        totalBudget = dao.getBudget();
        totalSpent = dao.getTotalBetween(ym.atDay(1), ym.atEndOfMonth());
        calculateRemaining();
    }

    public double getTotalBudget()     { return totalBudget; }
    public double getTotalSpent()      { return totalSpent; }
    public double getRemainingBudget() { return remainingBudget; }
    public double getPercentUsed()     { return totalBudget <= 0 ? 0 : (totalSpent / totalBudget) * 100.0; }
}
