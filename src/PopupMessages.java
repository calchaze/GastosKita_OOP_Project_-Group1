
import java.util.List;
import java.util.Random;

/** Predefined witty messages + randomized saving suggestions. */
public class PopupMessages {

    private String message;
    private String popupType;
    private Budget.Status budgetStatus;
    private final Random random = new Random();

    private static final String[] NONE = {
        "May laman ba 'yan o hangin lang? Check mo muna.",
        "Grabe, mas marami pang laman ang chismis kaysa wallet mo ah."
    };

    private static final String[] WITHIN = {
        "Ay WOW! Responsible adult na yarn? Character development!",
        "Yern, hindi na nagpa-budol. Proud ang nanay at tatay mo sa'yo!",
        "Ipon mode: ON. Financially stable ang atake. Sana all!"
    };

    private static final String[] NEARING = {
        "Beh, kalma. Hindi ka tagapagmana ng SM, kaloka ka!",
        "Konting gastos pa, sardinas na ang magiging love language mo.",
        "Your wallet is whispering: baby, enough na!"
    };

    private static final String[] EXCEEDED = {
        "Wala na. Empty na. Pati hangin wala.",
        "Beh, ang gastos mo naman! Time to slow down a little.",
        "Your budget needs a vacation from your spending habits!"
    };

    public String generateWitty(Budget.Status status) {

        String[] pool;

        switch (status) {
            case WITHIN:
                pool = WITHIN;
                break;

            case NEARING:
                pool = NEARING;
                break;

            case EXCEEDED:
                pool = EXCEEDED;
                break;

            default:
                pool = NONE;
                break;
        }

        this.budgetStatus = status;
        this.popupType = status.name();
        this.message = pool[random.nextInt(pool.length)];

        return message;
    }

    /**
     * Selects the highest-spending category
     * and displays its randomized saving suggestion.
     */
    public String giveSuggestion(List<ExpenseCategory> categories) {

        ExpenseCategory top = null;
        double total = 0;

        for (ExpenseCategory c : categories) {

            total += c.getCategoryTotal();

            if (top == null ||
                c.getCategoryTotal() > top.getCategoryTotal()) {

                top = c;
            }
        }

        if (top == null || total <= 0) {
            return "No expenses recorded yet this month. "
                 + "Start tracking your gastos so I can help you save!";
        }

        double pct = top.getCategoryTotal() / total * 100;

        return String.format(
            "Your biggest gastos this month: %s (%.0f%% of your spending)."
            + "%n%n"
            + "Saving suggestion: %s",
            top.getCategoryName(),
            pct,
            top.getSavingTip()
        );
    }

    public String getMessage() {
        return message;
    }

    public String getPopupType() {
        return popupType;
    }

    public Budget.Status getBudgetStatus() {
        return budgetStatus;
    }
}
