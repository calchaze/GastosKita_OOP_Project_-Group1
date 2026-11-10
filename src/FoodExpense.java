import java.time.LocalDate;

public class FoodExpense extends Expense {
    public FoodExpense(double a, String d, LocalDate dt) throws InvalidInputException { super(a, d, dt); }
    @Override public String getCategoryName() { return "Food"; }
    @Override public String getSavingTip() {
        return "Food: pack baon a few days a week and limit milk tea / delivery orders.";
    }
}
