import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel {
    public Board(){
        this.setPreferredSize(new Dimension(600, 600));
        this.setLayout(new GridLayout(Checkers.BOARD_SIZE, Checkers.BOARD_SIZE));

        // Create chessboard
        for (int row = 0; row < Checkers.BOARD_SIZE; row++) {
            for (int col = 0; col < Checkers.BOARD_SIZE; col++) {
                JPanel tile = new JPanel();
                tile.setBackground((row + col) % 2 == 0 ? Color.BLACK : Color.WHITE);
                Checkers.tiles[row][col] = tile;
                this.add(tile);

                // If tile is Black - add mouseListener to move the selected tile
                if(tile.getBackground().equals(Color.BLACK)){
                    int targetRow = row, targetCol = col;
                    tile.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            Checkers.switchPlayerTurn();
                            System.out.println("SelectedPiece:( " + Checkers.selectedPieceRow +","+ Checkers.selectedPieceCol +" )");
                            Checkers.selectedPiece.repaint();
                            if (Checkers.isMoveLegal(targetRow, targetCol)) {
                                Checkers.movePiece(Checkers.selectedPiece, targetRow, targetCol);
                                if(Checkers.checkIfSelectedPieceIsToBePromoted(targetRow)) Checkers.selectedPiece.promoteToKing();
                                Checkers.selectedPiece = null;
                                Checkers.selectedPieceRow = -1;
                                Checkers.selectedPieceCol = -1;
                            }
                        }
                    });
                }
            }
        }
    }
}
