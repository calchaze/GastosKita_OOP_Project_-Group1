import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * GastosKita web server. Serves the website in /web and a small JSON API backed by MySQL.
 * Uses only the JDK (com.sun.net.httpserver) + the MySQL connector jar.
 */
public class GastosKitaServer {
    private static final ExpenseDAO dao = new ExpenseDAO();
    private static final Budget budget = new Budget();
    private static final PopupMessages popups = new PopupMessages();
    private static final Path WEB = Paths.get("web").toAbsolutePath().normalize();

    public static void main(String[] args) throws Exception {
        DBConnection.initSchema();
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/", GastosKitaServer::api);
        server.createContext("/", GastosKitaServer::staticFile);
        server.start();
        System.out.println("GastosKita is running -> open http://localhost:" + port);
    }

    // ---------------------------------------------------------------- API router
    private static synchronized void api(HttpExchange ex) throws IOException {
        String route = ex.getRequestMethod() + " " + ex.getRequestURI().getPath();
        try {
            Map<String, String> p = params(ex);
            String json;
            switch (route) {
                case "GET /api/summary":          json = summary(); break;
                case "POST /api/budget":          json = setBudget(p); break;
                case "GET /api/expenses":         json = expenses(p); break;
                case "POST /api/expenses":        json = addExpense(p); break;
                case "POST /api/expenses/delete": json = delete(p); break;
                case "GET /api/daily":            json = daily(p); break;
                case "GET /api/breakdown":        json = breakdown(); break;
                default: send(ex, 404, "application/json", error("Not found")); return;
            }
            send(ex, 200, "application/json", json);
        } catch (InvalidInputException e) {
            send(ex, 400, "application/json", error(e.getMessage()));
        } catch (SQLException e) {
            send(ex, 500, "application/json", error("Database error: " + e.getMessage()));
        } catch (Exception e) {
            send(ex, 500, "application/json", error("Server error: " + e.getMessage()));
        }
    }

    // ---------------------------------------------------------------- handlers
    private static String summary() throws SQLException {
        budget.reload(dao);
        Budget.Status st = budget.checkBudget();
        return "{\"budget\":" + num(budget.getTotalBudget())
                + ",\"spent\":" + num(budget.getTotalSpent())
                + ",\"remaining\":" + num(budget.getRemainingBudget())
                + ",\"percent\":" + num(budget.getPercentUsed())
                + ",\"status\":\"" + st.name() + "\""
                + ",\"message\":" + str(popups.generateWitty(st)) + "}";
    }

    private static String setBudget(Map<String, String> p) throws InvalidInputException, SQLException {
        double amount = parseAmount(p.get("amount"), "Budget");
        budget.setBudget(amount);
        dao.saveBudget(amount);
        return summary();
    }

    private static String addExpense(Map<String, String> p) throws InvalidInputException, SQLException {
        double amount = parseAmount(p.get("amount"), "Amount");
        LocalDate date;
        try {
            date = LocalDate.parse(p.getOrDefault("date", "").trim());
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Date must be in yyyy-MM-dd format.");
        }
        Expense e = ExpenseFactory.create(p.get("category"), amount, p.get("description"), date);
        dao.addExpense(e);
        return summary(); // the website shows the witty popup from this
    }

    private static String delete(Map<String, String> p) throws InvalidInputException, SQLException {
        try {
            dao.deleteExpense(Integer.parseInt(p.getOrDefault("id", "")));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid expense id.");
        }
        return "{\"ok\":true}";
    }

    private static String expenses(Map<String, String> p) throws InvalidInputException, SQLException {
        List<Expense> list;
        try {
            if (p.containsKey("from") && p.containsKey("to"))
                list = dao.getBetween(LocalDate.parse(p.get("from")), LocalDate.parse(p.get("to")));
            else
                list = dao.getAll();
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date range.");
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            Expense e = list.get(i);
            if (i > 0) sb.append(',');
            sb.append("{\"id\":").append(e.getId())
              .append(",\"date\":\"").append(e.getDate()).append("\"")
              .append(",\"category\":").append(str(e.getCategoryName()))
              .append(",\"description\":").append(str(e.getDescription()))
              .append(",\"amount\":").append(num(e.getAmount())).append('}');
        }
        return sb.append(']').toString();
    }

    private static String daily(Map<String, String> p) throws InvalidInputException, SQLException {
        YearMonth ym;
        try {
            ym = YearMonth.parse(p.getOrDefault("month", YearMonth.now().toString()));
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Month must be yyyy-MM.");
        }
        Map<LocalDate, Double> map = dao.getDailyTotals(ym.atDay(1), ym.atEndOfMonth());
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<LocalDate, Double> en : map.entrySet()) {
            if (!first) sb.append(',');
            first = false;
            sb.append('"').append(en.getKey()).append("\":").append(num(en.getValue()));
        }
        return sb.append('}').toString();
    }

    private static String breakdown() throws SQLException {
        YearMonth ym = YearMonth.now();
        List<ExpenseCategory> cats = new ArrayList<>();
        for (String name : ExpenseFactory.CATEGORIES) cats.add(new ExpenseCategory(name));
        double total = 0;
        for (Expense e : dao.getBetween(ym.atDay(1), ym.atEndOfMonth())) {
            for (ExpenseCategory c : cats)
                if (c.getCategoryName().equals(e.getCategoryName())) c.addExpense(e);
            total += e.getAmount();
        }
        StringBuilder sb = new StringBuilder("{\"total\":" + num(total) + ",\"categories\":[");
        for (int i = 0; i < cats.size(); i++) {
            ExpenseCategory c = cats.get(i);
            double pct = total <= 0 ? 0 : c.getCategoryTotal() / total * 100;
            if (i > 0) sb.append(',');
            sb.append("{\"name\":").append(str(c.getCategoryName()))
              .append(",\"total\":").append(num(c.getCategoryTotal()))
              .append(",\"percent\":").append(num(pct)).append('}');
        }
        sb.append("],\"suggestion\":").append(str(popups.giveSuggestion(cats))).append('}');
        return sb.toString();
    }

    // ---------------------------------------------------------------- helpers
    private static double parseAmount(String s, String label) throws InvalidInputException {
        if (s == null || s.trim().isEmpty()) throw new InvalidInputException(label + " is required.");
        try {
            double v = Double.parseDouble(s.trim());
            if (Double.isNaN(v) || Double.isInfinite(v)) throw new NumberFormatException();
            return v;
        } catch (NumberFormatException e) {
            throw new InvalidInputException(label + " must be a number (e.g. 150.50).");
        }
    }

    private static Map<String, String> params(HttpExchange ex) throws IOException {
        Map<String, String> map = new HashMap<>();
        parseInto(ex.getRequestURI().getRawQuery(), map);
        byte[] body = ex.getRequestBody().readAllBytes();
        if (body.length > 0) parseInto(new String(body, StandardCharsets.UTF_8), map);
        return map;
    }

    private static void parseInto(String raw, Map<String, String> map) {
        if (raw == null || raw.isEmpty()) return;
        for (String pair : raw.split("&")) {
            int i = pair.indexOf('=');
            String k = i < 0 ? pair : pair.substring(0, i);
            String v = i < 0 ? "" : pair.substring(i + 1);
            map.put(URLDecoder.decode(k, StandardCharsets.UTF_8), URLDecoder.decode(v, StandardCharsets.UTF_8));
        }
    }

    private static String num(double d) { return String.format(Locale.ROOT, "%.2f", d); }

    private static String error(String msg) { return "{\"error\":" + str(msg) + "}"; }

    private static String str(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        return sb.append('"').toString();
    }

    private static void send(HttpExchange ex, int code, String type, String body) throws IOException {
        send(ex, code, type, body.getBytes(StandardCharsets.UTF_8));
    }

    private static void send(HttpExchange ex, int code, String type, byte[] data) throws IOException {
        ex.getResponseHeaders().set("Content-Type", type + "; charset=utf-8");
        ex.sendResponseHeaders(code, data.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(data); }
    }

    // ---------------------------------------------------------------- static website files
    private static void staticFile(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        if (path.equals("/")) path = "/index.html";
        Path file = WEB.resolve(path.substring(1)).normalize();
        if (!file.startsWith(WEB) || !Files.isRegularFile(file)) {
            send(ex, 404, "text/plain", "404 Not Found");
            return;
        }
        String name = file.getFileName().toString();
        String type = name.endsWith(".html") ? "text/html"
                : name.endsWith(".css") ? "text/css"
                : name.endsWith(".js") ? "application/javascript"
                : "application/octet-stream";
        send(ex, 200, type, Files.readAllBytes(file));
    }
}
