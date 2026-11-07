import java.time.LocalDate;

public class EntertainmentExpense extends Expense {
    public EntertainmentExpense(double a, String d, LocalDate dt) throws InvalidInputException { super(a, d, dt); }
    @Override public String getCategoryName() { return "Entertainment"; }
    @Override public String getSavingTip() {
        return "Entertainment: share subscriptions with friends and look for free or student-discount events.";
    }
}
