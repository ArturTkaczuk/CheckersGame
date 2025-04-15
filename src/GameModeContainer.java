import javax.swing.*;
import java.awt.*;

public class GameModeContainer extends JPanel {

    public GameModeContainer(JPanel rootContainer) {
        // Set layout for this GameModeContainer itself
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setLayout(new GridLayout(3, 1, 10, 10));

        JButton pvpButton = new JButton("PvP");
        JButton pveButton = new JButton("PvE");
        JButton pvpLanButton = new JButton("PvP LAN");

        Font buttonFont = new Font("Arial", Font.BOLD, 40);
        pvpButton.setFont(buttonFont);
        pveButton.setFont(buttonFont);
        pvpLanButton.setFont(buttonFont);

        pvpButton.setFocusPainted(false);
        pveButton.setFocusPainted(false);
        pvpLanButton.setFocusPainted(false);

        this.add(pvpButton);
        this.add(pveButton);
        this.add(pvpLanButton);

        pvpButton.addActionListener(e -> {
            Checkers.gameMode = GameMode.PVP;
            switchToGame(rootContainer);
        });

        pveButton.addActionListener(e -> {
            Checkers.gameMode = GameMode.PVE;
            switchToGame(rootContainer);
        });

        pvpLanButton.addActionListener(e -> {
            Checkers.gameMode = GameMode.PVP_LAN;
            switchToGame(rootContainer);
        });

        pvpLanButton.setEnabled(false); // Disable LAN button
    }

    private void switchToGame(JPanel rootContainer) {
        rootContainer.remove(this);

        Checkers.gameInformationContainer = new GameInformationContainer();
        rootContainer.add(Checkers.gameInformationContainer, BorderLayout.NORTH);

        Board board = new Board();
        rootContainer.add(board, BorderLayout.CENTER);
        board.addPiecesToBoard();

        rootContainer.revalidate();
        rootContainer.repaint();
    }
}
