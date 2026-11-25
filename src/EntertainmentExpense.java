import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class EntertainmentExpense extends Expense {

    private static final String[] TIPS = {
        "Enjoy life, pero huwag ubusin ang budget sa kakagala! Set a weekly allowance and try free activities with friends.",
        "Try to go out while saving sis! Explore free events and activities instead of always spending money.",
        "Your friends can have fun without an expensive bill! Set a weekly entertainment budget and stick to it."
    };

    public EntertainmentExpense(double a, String d, LocalDate dt)
            throws InvalidInputException {
        super(a, d, dt);
    }

    @Override
    public String getCategoryName() {
        return "Entertainment";
    }

    @Override
    public String getSavingTip() {
        return TIPS[ThreadLocalRandom.current().nextInt(TIPS.length)];
    }
}
