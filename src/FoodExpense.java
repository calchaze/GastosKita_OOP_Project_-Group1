import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class FoodExpense extends Expense {

    private static final String[] TIPS = {
        "Beh, hindi lahat ng cravings kailangang i-checkout! Magbaon naman minsan. Your wallet deserves a break from food deliveries.",
        "Kain nang maayos, pero huwag araw-araw feeling food vlogger! Try bringing snacks or lunch from home a few days a week.",
        "Parang ang mahal ng cravings natin dyan... Plan your meals and limit unnecessary food deliveries. Mas masarap ang pagkain kapag may natitira pang budget."
    };

    public FoodExpense(double a, String d, LocalDate dt)
            throws InvalidInputException {
        super(a, d, dt);
    }

    @Override
    public String getCategoryName() {
        return "Food";
    }

    @Override
    public String getSavingTip() {
        return TIPS[ThreadLocalRandom.current().nextInt(TIPS.length)];
    }
}
