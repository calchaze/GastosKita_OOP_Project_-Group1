import java.time.LocalDate;

public class TransportationExpense extends Expense {
    public TransportationExpense(double a, String d, LocalDate dt) throws InvalidInputException { super(a, d, dt); }
    @Override public String getCategoryName() { return "Transportation"; }
    @Override public String getSavingTip() {
        return "Transportation: share rides, walk short distances, or use a commute pass where possible.";
    }
}
