import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:breakout.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.out.println("Eroare la conectarea la baza de date: " + e.getMessage());
        }
        return conn;
    }

    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " username TEXT UNIQUE NOT NULL,\n"
                + " score INTEGER DEFAULT 0,\n"
                + " current_level INTEGER DEFAULT 1\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Baza de date și tabelul 'users' funcționează perfect!");
        } catch (Exception e) {
            System.out.println("Eroare la crearea tabelului: " + e.getMessage());
        }
    }

    public static void loginUser(String username) {
        String insertSQL = "INSERT OR IGNORE INTO users(username, score, current_level) VALUES(?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, 0);
            pstmt.setInt(3, 1);
            pstmt.executeUpdate();
            System.out.println("Utilizator '" + username + "' pregătit pentru joc.");
        } catch (Exception e) {
            System.out.println("Eroare la logare: " + e.getMessage());
        }
    }

    public static void updateScore(String username, int additionalScore) {
        String sql = "UPDATE users SET score = score + ? WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, additionalScore);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Eroare la actualizarea scorului: " + e.getMessage());
        }
    }

    // --- NOU: Citește nivelul curent al jucătorului ---
    public static int getUserLevel(String username) {
        String sql = "SELECT current_level FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("current_level");
            }
        } catch (Exception e) {
            System.out.println("Eroare la obținerea nivelului: " + e.getMessage());
        }
        return 1; // Default dacă apare vreo eroare
    }

    // --- NOU: Actualizează nivelul când jucătorul câștigă ---
    public static void updateLevel(String username, int newLevel) {
        String sql = "UPDATE users SET current_level = ? WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newLevel);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Eroare la actualizarea nivelului: " + e.getMessage());
        }
    }
}