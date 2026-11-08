import java.time.LocalDate;

public class ShoppingExpense extends Expense {
    public ShoppingExpense(double a, String d, LocalDate dt) throws InvalidInputException { super(a, d, dt); }
    @Override public String getCategoryName() { return "Shopping"; }
    @Override public String getSavingTip() {
        return "Shopping: wait 24 hours before buying non-essentials and keep a wishlist.";
    }
}
