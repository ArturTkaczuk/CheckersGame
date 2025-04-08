import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Checkers {
    public static Piece selectedPiece = null; // Currently selected piece
    public static int selectedPieceRow = -1, selectedPieceCol = -1;
    public static PlayerTurn playerTurn = PlayerTurn.BLUE; // Default starting player is BLUE
    public static Piece pieceThatMadeJumpMoveForCurrentPlayer = null;

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
        boolean isCurrentPlayersPiece =
                (playerTurn == PlayerTurn.RED && piece.color.equals(Color.RED)) ||
                (playerTurn == PlayerTurn.BLUE && piece.color.equals(Color.BLUE));

        if(isCurrentPlayersPiece == false){
            return false;
        }

        // if pieceThatMadeJumpMoveForCurrentPlayer is not null and this piece isn't a pieceThatMadeJumpMoveForCurrentPlayer, return false
        if(Checkers.pieceThatMadeJumpMoveForCurrentPlayer != null && piece != Checkers.pieceThatMadeJumpMoveForCurrentPlayer){
            return false;
        }

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

        // #################### BLOCK MOVE BY ONE IF OTHER PIECE HAS JUMP MOVE AVAILABLE ##################
        boolean isAvailableJumpMove = isAvailableJumpMove(piece);
        if(isAvailableJumpMove == false && isAvailableJumpMoveOnAnyCurrentPlayerPiece()){
            return false;
        }

        // #################### MOVE BY ONE IS NOT ALLOWED IF THERE IS JUMP MOVE AVAILABLE ################
        if (isAvailableJumpMove && targetTileIsNextToPiece){
            return false;
        }

        // #################### JUMPING OVER PIECE LOGIC ##################################################
        int middleRow = (pieceRow + finalRow) / 2;
        int middleCol = (pieceCol + finalCol) / 2;

        boolean isJumpValid = (Math.abs(finalRow - pieceRow) == 2 && Math.abs(finalCol - pieceCol) == 2);
        boolean middleTileHasEnemyPiece = Board.tiles[middleRow][middleCol].getComponentCount() == 1 &&
                isEnemyPiece(piece, middleRow, middleCol);
        boolean canJumpOverEnemyPiece = isJumpValid && middleTileHasEnemyPiece;

        // #################### FINAL CHECK ###############################################################
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

    public static boolean isAvailableJumpMove(Piece piece) {
        // Find piece position
        int pieceRow = -1, pieceCol = -1;
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                if (Board.tiles[row][col].getComponentCount() > 0 &&
                        Board.tiles[row][col].getComponent(0) == piece) {
                    pieceRow = row;
                    pieceCol = col;
                    break;
                }
            }
            if (pieceRow != -1) break;
        }

        if (pieceRow == -1 || pieceCol == -1) return false;

        // Check all 4 diagonal jump directions for any piece type
        int[][] directions = {
                {-2, -2}, {-2, 2}, {2, -2}, {2, 2}
        };

        for (int[] dir : directions) {
            int targetRow = pieceRow + dir[0];
            int targetCol = pieceCol + dir[1];

            // Bounds check
            if (targetRow < 0 || targetRow >= Board.BOARD_SIZE ||
                    targetCol < 0 || targetCol >= Board.BOARD_SIZE) continue;

            // Destination must be empty
            if (Board.tiles[targetRow][targetCol].getComponentCount() != 0) continue;

            // Midpoint check (enemy to jump over)
            int midRow = (pieceRow + targetRow) / 2;
            int midCol = (pieceCol + targetCol) / 2;

            if (Board.tiles[midRow][midCol].getComponentCount() == 1 &&
                    isEnemyPiece(piece, midRow, midCol)) {
                return true; // Valid jump exists
            }
        }

        return false; // No jump available
    }

    public static boolean isAvailableJumpMoveOnAnyCurrentPlayerPiece() {
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                if (Board.tiles[row][col].getComponentCount() == 1) {
                    Component comp = Board.tiles[row][col].getComponent(0);
                    if (comp instanceof Piece) {
                        Piece piece = (Piece) comp;

                        boolean isCurrentPlayersPiece =
                                (playerTurn == PlayerTurn.RED && piece.color.equals(Color.RED)) ||
                                        (playerTurn == PlayerTurn.BLUE && piece.color.equals(Color.BLUE));

                        if (isCurrentPlayersPiece && isAvailableJumpMove(piece)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
