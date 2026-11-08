import java.time.LocalDate;

/** Creates the right Expense subclass from a category name. */
public class ExpenseFactory {
    public static final String[] CATEGORIES = {"Food", "Transportation", "Shopping", "Entertainment"};

    public static Expense create(String category, double amount, String desc, LocalDate date)
            throws InvalidInputException {
        if (category == null) throw new InvalidInputException("Category is required.");
        switch (category) {
            case "Food":           return new FoodExpense(amount, desc, date);
            case "Transportation": return new TransportationExpense(amount, desc, date);
            case "Shopping":       return new ShoppingExpense(amount, desc, date);
            case "Entertainment":  return new EntertainmentExpense(amount, desc, date);
            default: throw new InvalidInputException("Unknown category: " + category);
        }
    }
}
