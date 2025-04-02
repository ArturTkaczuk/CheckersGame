import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Checkers {
    private static final int BOARD_SIZE = 8; // Chessboard size
    private final JPanel[][] tiles = new JPanel[BOARD_SIZE][BOARD_SIZE]; // Board tiles
    private final JButton[][] pieces = new JButton[BOARD_SIZE][BOARD_SIZE]; // movable pieces
    private JButton selectedPiece = null; // Currently selected piece

    public Checkers() {
        JFrame frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setResizable(false);

        JPanel board = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        frame.add(board);

        // Create chessboard
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JPanel tile = new JPanel();
                tile.setBackground((row + col) % 2 == 0 ? Color.BLACK : Color.WHITE);
                tiles[row][col] = tile;
                board.add(tile);

                // If tile is Black - add mouseListener to move the selected piece
                if(tile.getBackground().equals(Color.BLACK)){
                    int finalRow = row, finalCol = col;
                    tile.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (selectedPiece != null && tiles[finalRow][finalCol].getComponentCount() == 0) {
                                movePiece(selectedPiece, finalRow, finalCol);
                            }
                        }
                    });
                }
            }
        }

        // Create and place three pieces at different positions
        pieces[0][0] = createRoundButton(Color.RED);
        pieces[1][1] = createRoundButton(Color.RED);
        pieces[2][2] = createRoundButton(Color.BLUE);
        pieces[3][3] = createRoundButton(Color.BLUE);

        placePiece(pieces[0][0], 0, 0);
        placePiece(pieces[1][1], 1, 1);
        placePiece(pieces[2][2], 2, 2);
        placePiece(pieces[3][3], 3, 3);

        frame.setVisible(true);
    }

    // Create a round button representing a piece
    private JButton createRoundButton(Color color) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(55, 55));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(color);

        // Custom painting to make it round
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(button.getBackground());
                g2.fillOval(0, 0, c.getWidth(), c.getHeight());
            }
        });

        // Select the piece when clicked
        button.addActionListener(e -> selectedPiece = button);

        return button;
    }

    // Place a piece on the board
    private void placePiece(JButton piece, int row, int col) {
        tiles[row][col].add(piece);
        tiles[row][col].revalidate();
        tiles[row][col].repaint();
    }

    // Move the selected piece to a new tile
    private void movePiece(JButton piece, int row, int col) {
        // Remove the piece from the previous position
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                tiles[r][c].remove(piece);
                tiles[r][c].revalidate();
                tiles[r][c].repaint();
            }
        }
        // Add the piece to the new position
        tiles[row][col].add(piece);
        tiles[row][col].revalidate();
        tiles[row][col].repaint();
    }

    public static void main(String[] args) {
        new Checkers();
    }
}
