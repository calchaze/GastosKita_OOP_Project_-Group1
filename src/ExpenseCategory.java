import java.util.ArrayList;
import java.util.List;

public class ExpenseCategory {

    public static final String FOOD = "Food";
    public static final String TRANSPORTATION = "Transportation";
    public static final String SHOPPING = "Shopping";
    public static final String ENTERTAINMENT = "Entertainment";
    public static final String OTHERS = "Others";

    private final String categoryName;
    private final List<Expense> expenses = new ArrayList<>();

    public ExpenseCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public void addExpense(Expense e) {
        expenses.add(e);
    }

    public double getCategoryTotal() {
        double total = 0;

        for (Expense e : expenses) {
            total += e.getAmount();
        }

        return total;
    }

    public String displayCategory() {
        StringBuilder sb = new StringBuilder(
            categoryName + " (" + expenses.size() + " items)\n"
        );

        for (Expense e : expenses) {
            sb.append("  ")
              .append(e.getDetails())
              .append('\n');
        }

        return sb.toString();
    }

    public String getSavingTip() {
        return expenses.isEmpty() ? null : expenses.get(0).getSavingTip();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}
