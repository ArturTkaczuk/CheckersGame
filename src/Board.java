import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel {
    public static final int BOARD_SIZE = 8; // Chessboard size
    public static final JPanel[][] tiles = new JPanel[BOARD_SIZE][BOARD_SIZE]; // Board tiles
    public static final Piece[][] pieces = new Piece[BOARD_SIZE][BOARD_SIZE]; // movable pieces

    public Board(){
        this.setPreferredSize(new Dimension(600, 600));
        this.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        // Create chessboard
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JPanel tile = new JPanel();
                tile.setBackground((row + col) % 2 == 0 ? Color.BLACK : Color.WHITE);
                tiles[row][col] = tile;
                this.add(tile);

                // If tile is Black - add mouseListener to move the selected tile
                if(tile.getBackground().equals(Color.BLACK)){
                    int targetRow = row, targetCol = col;
                    tile.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("SelectedPiece:( " + Checkers.selectedPieceRow +","+ Checkers.selectedPieceCol +" )");
                            boolean isAvailableJumpMoveForSelectedPiece = Checkers.isAvailableJumpMove(Checkers.selectedPiece);
                            if (Checkers.isMoveLegal(Checkers.selectedPiece, targetRow, targetCol)) {
                                if(isAvailableJumpMoveForSelectedPiece == false) {
                                    // Selected piece moved by one tile
                                    Checkers.switchPlayerTurn();
                                }
                                movePieceOnBoard(Checkers.selectedPiece, targetRow, targetCol);
                                if(Checkers.checkIfSelectedPieceIsToBePromoted(targetRow)) Checkers.selectedPiece.promoteToKing();
                            }
                            Piece.repaintAllPieces();
                            Checkers.selectedPiece = null;
                            Checkers.selectedPieceRow = -1;
                            Checkers.selectedPieceCol = -1;
                        }
                    });
                }
            }
        }
    }

    public void addPiecesToBoard() {
        // Place RED pieces (top three rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) { // Place only on black tiles
                    pieces[row][col] = new Piece(Color.RED);
                    placePieceOnBoard(pieces[row][col], row, col);
                }
            }
        }

        // Place BLUE pieces (bottom three rows)
        for (int row = Board.BOARD_SIZE - 3; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) { // Place only on black tiles
                    pieces[row][col] = new Piece(Color.BLUE);
                    placePieceOnBoard(pieces[row][col], row, col);
                }
            }
        }
    }

    // Place a piece on the board
    public void placePieceOnBoard(Piece piece, int row, int col) {
        tiles[row][col].add(piece);
        tiles[row][col].revalidate();
        tiles[row][col].repaint();
    }

    public static void movePieceOnBoard(Piece piece, int row, int col) {
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
}
