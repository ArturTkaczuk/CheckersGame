import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Checkers {
    // Core game state
    public static final int BOARD_SIZE = 8; // Chessboard size
    public static final JPanel[][] tiles = new JPanel[BOARD_SIZE][BOARD_SIZE]; // Board tiles
    public static final Piece[][] pieces = new Piece[BOARD_SIZE][BOARD_SIZE]; // movable pieces
    public static Piece selectedPiece = null; // Currently selected piece
    public static int selectedPieceRow = -1, selectedPieceCol = -1;

    // Top panel state
    public static GameInformationContainer gameInformationContainer;

    public Checkers() {
        Root root = new Root("Checkers");

        // Main container panel with BorderLayout
        JPanel container = new JPanel(new BorderLayout());
        container.setPreferredSize(new Dimension(600, 700)); // Fixing the size

        // Add GameInformationContainer at the top
        gameInformationContainer = new GameInformationContainer();
        container.add(gameInformationContainer, BorderLayout.NORTH);

        // Create the board and add it to the container (not directly to root)
        JPanel board = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        board.setPreferredSize(new Dimension(600, 600));
        container.add(board, BorderLayout.CENTER);

        root.add(container);

        // Create chessboard
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JPanel tile = new JPanel();
                tile.setBackground((row + col) % 2 == 0 ? Color.BLACK : Color.WHITE);
                tiles[row][col] = tile;
                board.add(tile);

                // If tile is Black - add mouseListener to move the selected tile
                if(tile.getBackground().equals(Color.BLACK)){
                    int targetRow = row, targetCol = col;
                    tile.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("SelectedPiece:( " + selectedPieceRow +","+ selectedPieceCol +" )");
                            selectedPiece.repaint();
                            if (isMoveLegal(targetRow, targetCol)) {
                                movePiece(selectedPiece, targetRow, targetCol);
                                if(checkIfSelectedPieceIsToBePromoted(targetRow)) selectedPiece.promoteToKing();
                                selectedPiece = null;
                                selectedPieceRow = -1;
                                selectedPieceCol = -1;
                            }
                        }
                    });
                }
            }
        }

        addPiecesToBoard();

        root.setVisible(true);
    }

    public boolean checkIfSelectedPieceIsToBePromoted(int targetRow){
        // REGULAR pieces only get promoted, not kings
        if(selectedPiece.pieceType.equals(PieceType.REGULAR)){
            if(selectedPiece.color.equals(Color.BLUE) && targetRow == 0){
                return true;
            } else if (selectedPiece.color.equals(Color.RED) && targetRow == 7) {
                return true;
            }
        }
        return false;
    }

    private void addPiecesToBoard() {
        // Place RED pieces (top three rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) { // Place only on black tiles
                    pieces[row][col] = new Piece(Color.RED);
                    placePiece(pieces[row][col], row, col);
                }
            }
        }

        // Place BLUE pieces (bottom three rows)
        for (int row = BOARD_SIZE - 3; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) { // Place only on black tiles
                    pieces[row][col] = new Piece(Color.BLUE);
                    placePiece(pieces[row][col], row, col);
                }
            }
        }
    }

    public static boolean isMoveLegal(int finalRow, int finalCol){
        boolean pieceToMoveIsSelected = selectedPiece != null;
        boolean targetTileIsNotOccupied = tiles[finalRow][finalCol].getComponentCount() == 0;

        // #################### MOVE LOGIC #################################################
        // Allow only legal one-step diagonal movement based on color
        boolean targetTileIsNextToSelectedPiece = false;

        if(selectedPiece.pieceType.equals(PieceType.REGULAR)){
            boolean isRedPiece = selectedPiece.color.equals(Color.RED);
            boolean isBluePiece = selectedPiece.color.equals(Color.BLUE);
            targetTileIsNextToSelectedPiece =
                (Math.abs(finalCol - selectedPieceCol) == 1) &&
                ((isRedPiece && finalRow == selectedPieceRow + 1) || (isBluePiece && finalRow == selectedPieceRow - 1));
        }
        else if (selectedPiece.pieceType.equals(PieceType.KING)){
            targetTileIsNextToSelectedPiece =
                (Math.abs(finalRow - selectedPieceRow) == 1 && Math.abs(finalCol - selectedPieceCol) == 1);
        }


        // #################### JUMPING OVER PIECE LOGIC ###################################
        // Calculate middle tile coordinates
        int middleRow = (selectedPieceRow + finalRow) / 2;
        int middleCol = (selectedPieceCol + finalCol) / 2;

        // Check if a jump move is valid
        boolean isJumpValid = (Math.abs(finalRow - selectedPieceRow) == 2 && Math.abs(finalCol - selectedPieceCol) == 2);
        boolean middleTileHasEnemyPiece = tiles[middleRow][middleCol].getComponentCount() == 1 && isEnemyPiece(middleRow, middleCol);
        boolean canJumpOverEnemyPiece = isJumpValid && middleTileHasEnemyPiece;


        // #################### ALL LOGIC COMBINED TO FINAL IF STATEMENT ####################
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
            selectedPieceRow = -1;
            selectedPieceCol = -1;
            return false;
        }
    }

    public static boolean isEnemyPiece(int row, int col) {
        if (tiles[row][col].getComponentCount() == 0) {
            return false; // No piece in the middle tile
        }

        Piece piece = (Piece) tiles[row][col].getComponent(0);

        // Assuming Red pieces play against Blue pieces
        return (selectedPiece.color.equals(Color.RED) && piece.color.equals(Color.BLUE)) ||
                (selectedPiece.color.equals(Color.BLUE) && piece.color.equals(Color.RED));
    }

    // Place a piece on the board
    public void placePiece(Piece piece, int row, int col) {
        tiles[row][col].add(piece);
        tiles[row][col].revalidate();
        tiles[row][col].repaint();
    }

    // Move the selected piece to a new tile
    public void movePiece(Piece piece, int row, int col) {
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
