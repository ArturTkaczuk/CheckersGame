import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Checkers {
    private static final int BOARD_SIZE = 8; // Chessboard size
    private static final JPanel[][] tiles = new JPanel[BOARD_SIZE][BOARD_SIZE]; // Board tiles
    private static final JButton[][] pieces = new JButton[BOARD_SIZE][BOARD_SIZE]; // movable pieces
    private static JButton selectedPiece = null; // Currently selected piece
    private static int selectedRow = -1, selectedCol = -1;

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
                            System.out.println("SelectedPiece:( " + selectedRow +","+ selectedCol +" )");
                            if (isMoveLegal(finalRow, finalCol)) {
                                movePiece(selectedPiece, finalRow, finalCol);
                                selectedPiece = null;
                                selectedRow = -1;
                                selectedCol = -1;
                            }
                        }
                    });
                }
            }
        }

        // Create and place three pieces at different positions
        pieces[0][0] = createRoundPiece(Color.RED);
        pieces[1][1] = createRoundPiece(Color.RED);
        pieces[2][2] = createRoundPiece(Color.BLUE);
        pieces[3][3] = createRoundPiece(Color.BLUE);

        placePiece(pieces[0][0], 0, 0);
        placePiece(pieces[1][1], 1, 1);
        placePiece(pieces[2][2], 2, 2);
        placePiece(pieces[3][3], 3, 3);

        frame.setVisible(true);
    }

    public static boolean isMoveLegal(int finalRow, int finalCol){
        boolean pieceToMoveIsSelected = selectedPiece != null;
        boolean targetTileIsNotOccupied = tiles[finalRow][finalCol].getComponentCount() == 0;
        boolean targetTileIsNextToSelectedPiece = (Math.abs(finalRow - selectedRow) == 1 && Math.abs(finalCol - selectedCol) == 1);

        // Calculate middle tile coordinates
        int middleRow = (selectedRow + finalRow) / 2;
        int middleCol = (selectedCol + finalCol) / 2;

        // Check if a jump move is valid
        boolean isJumpValid = (Math.abs(finalRow - selectedRow) == 2 && Math.abs(finalCol - selectedCol) == 2);
        boolean middleTileHasEnemyPiece = tiles[middleRow][middleCol].getComponentCount() == 1 && isEnemyPiece(middleRow, middleCol);

        boolean canJumpOverEnemyPiece = isJumpValid && middleTileHasEnemyPiece;

        if(pieceToMoveIsSelected && targetTileIsNotOccupied && (targetTileIsNextToSelectedPiece || canJumpOverEnemyPiece)){
            // if canJumpOverEnemyPiece is true, remove enemy piece from the board
            if(canJumpOverEnemyPiece){
                tiles[middleRow][middleCol].removeAll();
                tiles[middleRow][middleCol].revalidate();
                tiles[middleRow][middleCol].repaint();
            }
            return true;
        } else {
            selectedPiece = null;
            selectedRow = -1;
            selectedCol = -1;
            return false;
        }
    }

    public static boolean isEnemyPiece(int row, int col) {
        if (tiles[row][col].getComponentCount() == 0) {
            return false; // No piece in the middle tile
        }

        JButton piece = (JButton) tiles[row][col].getComponent(0);

        // Assuming Red pieces play against Blue pieces
        return (selectedPiece.getBackground() == Color.RED && piece.getBackground() == Color.BLUE) ||
                (selectedPiece.getBackground() == Color.BLUE && piece.getBackground() == Color.RED);
    }


    // Create a round button representing a piece
    private JButton createRoundPiece(Color color) {
        JButton piece = new JButton();
        piece.setPreferredSize(new Dimension(55, 55));
        piece.setContentAreaFilled(false);
        piece.setFocusPainted(false);
        piece.setBorderPainted(false);
        piece.setOpaque(false);
        piece.setBackground(color);

        // Custom painting to make it round
        piece.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(piece.getBackground());
                g2.fillOval(0, 0, c.getWidth(), c.getHeight());
            }
        });

        // Select the piece when clicked
        piece.addActionListener(e -> {
            selectedPiece = piece;

            // Find the current row and column of the selected piece
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    if (tiles[row][col].getComponentCount() > 0 && tiles[row][col].getComponent(0) == piece) {
                        selectedRow = row;
                        selectedCol = col;
                        System.out.println("SelectedPiece:( " + selectedRow +","+ selectedCol +" )");
                        return;
                    }
                }
            }
        });

        return piece;
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
