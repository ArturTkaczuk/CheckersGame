import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Checkers {
    public static Piece selectedPiece = null; // Currently selected piece
    public static int selectedPieceRow = -1, selectedPieceCol = -1;
    public static PlayerTurn playerTurn = PlayerTurn.BLUE; // Default starting player is BLUE
    public static List<Piece> legalMovePieces = new ArrayList<>();

    // Top panel
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
        Board board = new Board();
        container.add(board, BorderLayout.CENTER);

        root.add(container);

        board.addPiecesToBoard();

        root.setVisible(true);
    }

    public static void switchPlayerTurn(){
        if(playerTurn == PlayerTurn.RED){
            playerTurn = PlayerTurn.BLUE;
            gameInformationContainer.setCurrentPlayer(PlayerTurn.BLUE);
        }
        else if(playerTurn == PlayerTurn.BLUE){
            playerTurn = PlayerTurn.RED;
            gameInformationContainer.setCurrentPlayer(PlayerTurn.RED);
        }
    }

    public static boolean checkIfSelectedPieceIsToBePromoted(int targetRow){
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

    public static boolean isMoveLegal(Piece piece, int finalRow, int finalCol) {
        // Find the current position of the piece
        int pieceRow = -1;
        int pieceCol = -1;

        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                if (Board.tiles[row][col].getComponentCount() > 0 && Board.tiles[row][col].getComponent(0) == piece) {
                    pieceRow = row;
                    pieceCol = col;
                    break;
                }
            }
        }

        if (pieceRow == -1 || pieceCol == -1) return false; // piece not found

        boolean targetTileIsNotOccupied = Board.tiles[finalRow][finalCol].getComponentCount() == 0;

        // #################### MOVE LOGIC #################################################
        boolean targetTileIsNextToPiece = false;

        if (piece.pieceType.equals(PieceType.REGULAR)) {
            boolean isRedPiece = piece.color.equals(Color.RED);
            boolean isBluePiece = piece.color.equals(Color.BLUE);
            targetTileIsNextToPiece =
                    (Math.abs(finalCol - pieceCol) == 1) &&
                            ((isRedPiece && finalRow == pieceRow + 1) || (isBluePiece && finalRow == pieceRow - 1));
        } else if (piece.pieceType.equals(PieceType.KING)) {
            targetTileIsNextToPiece =
                    (Math.abs(finalRow - pieceRow) == 1 && Math.abs(finalCol - pieceCol) == 1);
        }

        // #################### JUMPING OVER PIECE LOGIC ###################################
        int middleRow = (pieceRow + finalRow) / 2;
        int middleCol = (pieceCol + finalCol) / 2;

        boolean isJumpValid = (Math.abs(finalRow - pieceRow) == 2 && Math.abs(finalCol - pieceCol) == 2);
        boolean middleTileHasEnemyPiece = Board.tiles[middleRow][middleCol].getComponentCount() == 1 &&
                isEnemyPiece(piece, middleRow, middleCol);
        boolean canJumpOverEnemyPiece = isJumpValid && middleTileHasEnemyPiece;

        // #################### FINAL CHECK #################################################
        if (targetTileIsNotOccupied && (targetTileIsNextToPiece || canJumpOverEnemyPiece)) {
            if (canJumpOverEnemyPiece) {
                Board.tiles[middleRow][middleCol].removeAll();
                Board.tiles[middleRow][middleCol].revalidate();
                Board.tiles[middleRow][middleCol].repaint();
            }
            return true;
        }

        return false;
    }

    // TODO:
    public void updateLegalMovePieces(){
        // clear last player's list
        legalMovePieces.clear();

        // add all current player's pieces

        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                Component[] pieces = Board.tiles[row][col].getComponents();

                if (pieces.length > 0 && pieces[0] instanceof Piece) {
                    Piece piece = (Piece) pieces[0];

                    // Check if the piece belongs to the current player
                    if ((playerTurn == PlayerTurn.RED && piece.color == Color.RED) ||
                            (playerTurn == PlayerTurn.BLUE && piece.color == Color.BLUE)) {
                        legalMovePieces.add(piece); // Add piece to list
                    }
                }
            }
        }
    }

    public static boolean isEnemyPiece(Piece attacker, int row, int col) {
        if (Board.tiles[row][col].getComponentCount() == 0) {
            return false; // No piece on the tile
        }

        Component comp = Board.tiles[row][col].getComponent(0);
        Piece target = (Piece) comp;

        // Pieces of different colors are enemies
        return !attacker.color.equals(target.color);
    }


    public static void main(String[] args) {
        new Checkers();
    }
}
