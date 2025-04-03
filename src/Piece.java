import javax.swing.*;
import java.awt.*;

class Piece extends JButton {
    public final Color color;
    public final PieceType pieceType = PieceType.REGULAR;

    public Piece(Color color) {
        this.color = color;
        this.setPreferredSize(new Dimension(55, 55));
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setOpaque(false);
        this.setBackground(color);

        this.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, c.getWidth(), c.getHeight());
            }
        });

        this.addActionListener(e -> {
            Checkers.selectedPiece = this;

            // Find the current row and column of the selected piece
            for (int row = 0; row < Checkers.BOARD_SIZE; row++) {
                for (int col = 0; col < Checkers.BOARD_SIZE; col++) {
                    if (Checkers.tiles[row][col].getComponentCount() > 0 && Checkers.tiles[row][col].getComponent(0) == this) {
                        Checkers.selectedPieceRow = row;
                        Checkers.selectedPieceCol = col;
                        System.out.println("SelectedPiece:( " + Checkers.selectedPieceRow +","+ Checkers.selectedPieceCol +" )");
                        return;
                    }
                }
            }
        });
    }
}