import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {

    // Păstrăm numele jucătorului curent ca să îl putem folosi mai târziu
    public static String currentUser = "";

    public static void main(String[] args) {
        // 1. Pregătim baza de date
        DatabaseManager.createNewTable();

        // 2. Cerem numele jucătorului printr-o fereastră de tip pop-up
        currentUser = JOptionPane.showInputDialog(null, "Introdu numele tău pentru a juca:", "Login Breakout", JOptionPane.QUESTION_MESSAGE);

        // Dacă utilizatorul apasă Cancel sau nu scrie nimic, îi punem un nume default
        if (currentUser == null || currentUser.trim().isEmpty()) {
            currentUser = "Anonim";
        }

        // 3. Salvăm/Logăm utilizatorul în baza de date
        DatabaseManager.loginUser(currentUser);

        // 4. Pornim interfața grafică a jocului
        JFrame frame = new JFrame();
        GamePlay gamePlay = new GamePlay();

        frame.setBounds(10, 10, 700, 600);
        // Punem numele jucătorului sus pe bara ferestrei
        frame.setTitle("Breakout Ball - Jucător: " + currentUser);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(gamePlay);
        frame.setVisible(true);
    }
}