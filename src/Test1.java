import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test1 {
    private static final int BOARD_SIZE = 8; // Chessboard size
    private final JPanel[][] squares = new JPanel[BOARD_SIZE][BOARD_SIZE]; // Board tiles
    private JButton piece; // Movable piece

    public Test1() {
        JFrame frame = new JFrame("Movable Chess Piece");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        JPanel board = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        frame.add(board, BorderLayout.CENTER);

        // Create chessboard
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JPanel square = new JPanel();
                square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.BLACK);
                square.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                squares[row][col] = square;
                board.add(square);

                // Add button move action
                int finalRow = row, finalCol = col;
                square.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        movePiece(finalRow, finalCol);
                    }
                });
            }
        }

        // Create and place the round piece at (0,0)
        piece = createRoundButton();
        squares[0][0].add(piece);

        frame.setVisible(true);
    }

    // Create a round button
    private JButton createRoundButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(50, 50));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(Color.RED);

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

        return button;
    }

    // Move piece to new tile
    private void movePiece(int row, int col) {
        // Remove piece from its current parent panel
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                squares[r][c].remove(piece);
                squares[r][c].revalidate();
                squares[r][c].repaint();
            }
        }
        // Add piece to the new panel
        squares[row][col].add(piece);
        squares[row][col].revalidate();
        squares[row][col].repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Test1::new);
    }
}
