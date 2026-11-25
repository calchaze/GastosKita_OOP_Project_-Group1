import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class OthersExpense extends Expense {

    private static final String[] TIPS = {
        "Miscellaneous lang dapat, hindi mystery spending! Ihiwalay ang needs sa wants at mag-set ng weekly limit.",
        "Beh, saan napunta ang pera? Review your miscellaneous expenses and check which purchases you can avoid next time.",
        "Small gastos can become big gastos! Keep track of your extra purchases and prioritize your actual needs."
    };

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
        return TIPS[ThreadLocalRandom.current().nextInt(TIPS.length)];
    }
}
