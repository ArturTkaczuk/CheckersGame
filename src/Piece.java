import javax.swing.*;
import java.awt.*;

class Piece extends JButton {
    public final Color color;
    public PieceType pieceType = PieceType.REGULAR; // Default to REGULAR

    public Piece(Color color) {
        this.color = color;
        setPreferredSize(new Dimension(55, 55));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setBackground(color);

        addActionListener(e -> {
            Checkers.selectedPiece = this;

            // Find the current row and column of the selected piece
            for (int row = 0; row < Checkers.BOARD_SIZE; row++) {
                for (int col = 0; col < Checkers.BOARD_SIZE; col++) {
                    if (Checkers.tiles[row][col].getComponentCount() > 0 &&
                            Checkers.tiles[row][col].getComponent(0) == this) {
                        Checkers.selectedPieceRow = row;
                        Checkers.selectedPieceCol = col;
                        System.out.println("SelectedPiece:( " + Checkers.selectedPieceRow +","+ Checkers.selectedPieceCol +" )");
                        return;
                    }
                }
            }
        });
    }

    public void promoteToKing() {
        this.pieceType = PieceType.KING;
        repaint(); // Redraw the piece to show the king indicator
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the main piece circle
        g2.setColor(color);
        g2.fillOval(5, 5, getWidth() - 10, getHeight() - 10);

        // If piece is a KING, draw an inner dark-color circle
        if (pieceType == PieceType.KING) {
            if(this.color.equals(Color.BLUE)){
                g2.setColor(new Color(0, 0, 94));
            }
            else if (this.color.equals(Color.RED)){
                g2.setColor(new Color(87, 0, 0));
            }
            int inset = 15; // Size of the inner circle
            g2.fillOval(inset, inset, getWidth() - 2 * inset, getHeight() - 2 * inset);
        }
    }
}
