import javax.swing.*;
import java.awt.*;

public class Checkers {
    // Constants
    public static final int BOARD_SIZE = 8;

    // Global Variables
    public static JPanel[][] tiles = new JPanel[BOARD_SIZE][BOARD_SIZE];

    public static void main(String[] args) {
        JFrame frame = new JFrame("Checkers Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        frame.setResizable(false);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JPanel tile = new JPanel();
                if ((row + col) % 2 == 0) {
                    tile.setBackground(Color.WHITE);
                } else {
                    tile.setBackground(Color.BLACK);
                }
                tiles[row][col] = tile;

                frame.add(tile);
            }
        }
        frame.setVisible(true);
    }
}
