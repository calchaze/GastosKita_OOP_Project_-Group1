
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class TransportationExpense extends Expense {

    private static final String[] TIPS = {
        "I suggest lakarin kung malapit lang! Plan your trips and combine errands. Hindi kailangang bawat lakad may pamasahe.",
        "Bawat sakay, bawas sa budget! Try using affordable public transportation and plan your trips ahead.",
        "Hindi ka naman laging nagmamadali, beh! Combine errands in one trip to save time and transportation expenses."
    };

    public TransportationExpense(double a, String d, LocalDate dt)
            throws InvalidInputException {
        super(a, d, dt);
    }

    @Override
    public String getCategoryName() {
        return "Transportation";
    }

    @Override
    public String getSavingTip() {
        return TIPS[ThreadLocalRandom.current().nextInt(TIPS.length)];
    }
}
