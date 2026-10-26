import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.Scanner;

/**
 * Clasa MapGenerator este responsabilă pentru citirea și desenarea hărților de joc.
 * Hărțile sunt încărcate din fișiere text externe pentru a permite adăugarea facilă de noi niveluri.
 */
public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    private int totalBricks = 0;

    /**
     * Constructorul clasei. Citește matricea nivelului dintr-un fișier.
     *
     * @param filePath Calea către fișierul text care conține configurația nivelului (ex: "nivel1.txt").
     */
    public MapGenerator(String filePath) {
        try {
            Scanner scanner = new Scanner(new File(filePath));
            int rows = scanner.nextInt();
            int cols = scanner.nextInt();
            map = new int[rows][cols];

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    int val = scanner.nextInt();
                    map[i][j] = val;
                    if (val > 0) {
                        totalBricks++;
                    }
                }
            }
            scanner.close();

            brickWidth = 540 / cols;
            brickHeight = 150 / rows;
        } catch (Exception e) {
            System.out.println("Eroare la citirea hartii: " + e.getMessage());
        }
    }

    /**
     * Desenează cărămizile pe interfața grafică pe baza matricei citite.
     *
     * @param g Obiectul de tip Graphics2D folosit pentru desenarea elementelor pe panou.
     */
    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLACK);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    /**
     * Modifică valoarea unei anumite cărămizi din matrice (ex: setare pe 0 când este lovită).
     *
     * @param value Noua valoare a cărămizii (0 pentru a o face să dispară).
     * @param row   Rândul la care se află cărămida.
     * @param col   Coloana la care se află cărămida.
     */
    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }

    /**
     * Returnează numărul total de cărămizi rămase pe ecran.
     *
     * @return Numărul de cărămizi.
     */
    public int getTotalBricks() {
        return totalBricks;
    }
}