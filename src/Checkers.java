import javax.swing.*;
import java.awt.*;

public class Checkers {
    // Core game state
    public static Piece selectedPiece = null; // Currently selected piece
    public static int selectedPieceRow = -1, selectedPieceCol = -1;
    public static PlayerTurn playerTurn = PlayerTurn.BLUE; // Default starting player is BLUE

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

    public static boolean isMoveLegal(int finalRow, int finalCol){
        boolean pieceToMoveIsSelected = selectedPiece != null;
        boolean targetTileIsNotOccupied = Board.tiles[finalRow][finalCol].getComponentCount() == 0;

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
        boolean middleTileHasEnemyPiece = Board.tiles[middleRow][middleCol].getComponentCount() == 1 && isEnemyPiece(middleRow, middleCol);
        boolean canJumpOverEnemyPiece = isJumpValid && middleTileHasEnemyPiece;


        // #################### ALL LOGIC COMBINED TO FINAL IF STATEMENT ####################
        if(pieceToMoveIsSelected && targetTileIsNotOccupied && (targetTileIsNextToSelectedPiece || canJumpOverEnemyPiece)){
            // if canJumpOverEnemyPiece is true, remove enemy piece from the board
            if(canJumpOverEnemyPiece){
                Board.tiles[middleRow][middleCol].removeAll();
                Board.tiles[middleRow][middleCol].revalidate();
                Board.tiles[middleRow][middleCol].repaint();
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
        if (Board.tiles[row][col].getComponentCount() == 0) {
            return false; // No piece in the middle tile
        }

        Piece piece = (Piece) Board.tiles[row][col].getComponent(0);

        // Assuming Red pieces play against Blue pieces
        return (selectedPiece.color.equals(Color.RED) && piece.color.equals(Color.BLUE)) ||
                (selectedPiece.color.equals(Color.BLUE) && piece.color.equals(Color.RED));
    }

    public static void main(String[] args) {
        new Checkers();
    }
}
