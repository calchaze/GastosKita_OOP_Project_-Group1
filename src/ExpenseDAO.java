import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** All MySQL queries live here. */
public class ExpenseDAO {

    public void addExpense(Expense e) throws SQLException {
        String sql = "INSERT INTO expenses (amount, category, description, expense_date) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, e.getAmount());
            ps.setString(2, e.getCategoryName());
            ps.setString(3, e.getDescription());
            ps.setDate(4, Date.valueOf(e.getDate()));
            ps.executeUpdate();
        }
    }

    public void deleteExpense(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM expenses WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Expense> getAll() throws SQLException {
        return query("SELECT * FROM expenses ORDER BY expense_date DESC, id DESC", null, null);
    }

    public List<Expense> getBetween(LocalDate from, LocalDate to) throws SQLException {
        return query("SELECT * FROM expenses WHERE expense_date BETWEEN ? AND ? "
                + "ORDER BY expense_date DESC, id DESC", from, to);
    }

    public double getTotalBetween(LocalDate from, LocalDate to) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount),0) FROM expenses WHERE expense_date BETWEEN ? AND ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        }
    }

    /** Total spending per day (used by the calendar view). */
    public Map<LocalDate, Double> getDailyTotals(LocalDate from, LocalDate to) throws SQLException {
        Map<LocalDate, Double> map = new HashMap<>();
        String sql = "SELECT expense_date, SUM(amount) FROM expenses WHERE expense_date BETWEEN ? AND ? "
                + "GROUP BY expense_date";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) map.put(rs.getDate(1).toLocalDate(), rs.getDouble(2));
            }
        }
        return map;
    }

    public double getBudget() throws SQLException {
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT total_budget FROM budget WHERE id = 1")) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    public void saveBudget(double amount) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE budget SET total_budget = ? WHERE id = 1")) {
            ps.setDouble(1, amount);
            ps.executeUpdate();
        }
    }

    private List<Expense> query(String sql, LocalDate from, LocalDate to) throws SQLException {
        List<Expense> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (from != null) {
                ps.setDate(1, Date.valueOf(from));
                ps.setDate(2, Date.valueOf(to));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        Expense e = ExpenseFactory.create(rs.getString("category"), rs.getDouble("amount"),
                                rs.getString("description"), rs.getDate("expense_date").toLocalDate());
                        e.setId(rs.getInt("id"));
                        list.add(e);
                    } catch (InvalidInputException ex) {
                        // skip corrupted rows
                    }
                }
            }
        }
        return list;
    }
}