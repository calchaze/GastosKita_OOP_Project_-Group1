import java.util.ArrayList;
import java.util.List;

/** Groups expenses under one category (food, transportation, shopping, entertainment). */
public class ExpenseCategory {
    private final String categoryName;
    private final List<Expense> expenses = new ArrayList<>();

    public ExpenseCategory(String categoryName) { this.categoryName = categoryName; }

    public void addExpense(Expense e) { expenses.add(e); }

    public double getCategoryTotal() {
        double total = 0;
        for (Expense e : expenses) total += e.getAmount();
        return total;
    }

    public String displayCategory() {
        StringBuilder sb = new StringBuilder(categoryName + " (" + expenses.size() + " items)\n");
        for (Expense e : expenses) sb.append("  ").append(e.getDetails()).append('\n');
        return sb.toString();
    }

    /** Polymorphism: asks the expense object itself for its tip. */
    public String getSavingTip() {
        return expenses.isEmpty() ? null : expenses.get(0).getSavingTip();
    }

    public String getCategoryName() { return categoryName; }
    public List<Expense> getExpenses() { return expenses; }
}
