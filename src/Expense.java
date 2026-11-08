import java.time.LocalDate;

/** ABSTRACTION: general Expense. Subclasses define category-specific behavior (POLYMORPHISM). */
public abstract class Expense {
    // ENCAPSULATION: private fields, accessed through methods
    private int id;
    private final double amount;
    private final String description;
    private final LocalDate date;

    public Expense(double amount, String description, LocalDate date) throws InvalidInputException {
        if (Double.isNaN(amount) || Double.isInfinite(amount))
            throw new InvalidInputException("Amount must be a valid number.");
        if (amount <= 0)
            throw new InvalidInputException("Amount must be greater than zero (no negative or zero amounts).");
        if (description == null || description.trim().isEmpty())
            throw new InvalidInputException("Description is required.");
        if (date == null)
            throw new InvalidInputException("Date is required.");
        this.amount = amount;
        this.description = description.trim();
        this.date = date;
    }

    public void addExpense() { /* persisted through ExpenseDAO.addExpense(this) */ }

    public String getDetails() {
        return date + " | " + getCategoryName() + " | " + description + " | " + String.format("%.2f", amount);
    }

    public double getAmount()        { return amount; }
    public String getDescription()   { return description; }
    public LocalDate getDate()       { return date; }
    public int getId()               { return id; }
    public void setId(int id)        { this.id = id; }

    public abstract String getCategoryName();
    /** Each category gives its own saving tip. */
    public abstract String getSavingTip();
}
