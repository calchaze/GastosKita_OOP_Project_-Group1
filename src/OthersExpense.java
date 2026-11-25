import java.time.LocalDate;

public class OthersExpense extends Expense {

    public OthersExpense(double a, String d, LocalDate dt)
            throws InvalidInputException {
        super(a, d, dt);
    }

    @Override
    public String getCategoryName() {
        return "Others";
    }

    @Override
    public String getSavingTip() {
        return "Others: review your miscellaneous expenses and prioritize your needs before spending.";
    }
}

