import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class ShoppingExpense extends Expense {

    private static final String[] TIPS = {
        "Ayarn add to cart pa! Wait 24 hours bago bumili. Baka naman gusto mo lang, hindi mo talaga kailangan.",
        "Te, hindi lahat ng sale ay savings! Compare prices and make a shopping list before buying anything.",
        "Your cart is full, but is your wallet ready? Avoid impulse buying and prioritize the things you actually need."
    };

    public ShoppingExpense(double a, String d, LocalDate dt)
            throws InvalidInputException {
        super(a, d, dt);
    }

    @Override
    public String getCategoryName() {
        return "Shopping";
    }

    @Override
    public String getSavingTip() {
        return TIPS[ThreadLocalRandom.current().nextInt(TIPS.length)];
    }
}
