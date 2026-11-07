import java.util.List;
import java.util.Random;

/** Predefined witty messages (written by the developers) + saving suggestions. */
public class PopupMessages {
    private String message;
    private String popupType;
    private Budget.Status budgetStatus;
    private final Random random = new Random();

    private static final String[] NONE = {
        "No budget yet? Set one so your wallet has a game plan!",
        "A budget a day keeps the 'asan ang pera ko?' away."
    };
    private static final String[] WITHIN = {
        "Wow, ang galing! Your wallet is smiling today.",
        "Budget status: chill. Keep it up, future millionaire!",
        "Ipon mode: ON. Your future self says thank you!"
    };
    private static final String[] NEARING = {
        "Easy lang! Your budget is already sweating a little.",
        "Almost at the limit! Pause muna bago mag-checkout.",
        "Your wallet is whispering: 'tama na muna, please.'"
    };
    private static final String[] EXCEEDED = {
        "Oops! Over budget na. Time for a 'baon-only' week!",
        "Your budget said goodbye... but you can still win next week!",
        "Budget exceeded! Walang judgment, pero bawas-bawas muna tayo."
    };

    public String generateWitty(Budget.Status status) {
        String[] pool;
        switch (status) {
            case WITHIN:   pool = WITHIN;   break;
            case NEARING:  pool = NEARING;  break;
            case EXCEEDED: pool = EXCEEDED; break;
            default:       pool = NONE;     break;
        }
        this.budgetStatus = status;
        this.popupType = status.name();
        this.message = pool[random.nextInt(pool.length)];
        return message;
    }

    /** Suggests where to reduce spending based on the highest-spending category. */
    public String giveSuggestion(List<ExpenseCategory> categories) {
        ExpenseCategory top = null;
        double total = 0;
        for (ExpenseCategory c : categories) {
            total += c.getCategoryTotal();
            if (top == null || c.getCategoryTotal() > top.getCategoryTotal()) top = c;
        }
        if (top == null || total <= 0)
            return "No expenses recorded yet this month. Add some so I can suggest where to save!";
        double pct = top.getCategoryTotal() / total * 100;
        return String.format("Most of your money (%.0f%%) went to %s.%n%nSaving tip: %s",
                pct, top.getCategoryName(), top.getSavingTip());
    }

    public String getMessage()            { return message; }
    public String getPopupType()          { return popupType; }
    public Budget.Status getBudgetStatus(){ return budgetStatus; }
}
