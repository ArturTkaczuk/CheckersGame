import javax.swing.*;
import java.awt.*;

public class GameInformationContainer extends JPanel {
    public JLabel currentPlayerLabel;
    public JLabel redTimerLabel;
    public JLabel blueTimerLabel;

    public GameInformationContainer() {
        this.setPreferredSize(new Dimension(600, 100));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 20px padding

        // Panel to center current player label
        JPanel playerTurnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.add(playerTurnPanel);

        // Current player label
        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        setCurrentPlayer("RED"); // Default to RED
        playerTurnPanel.add(currentPlayerLabel);

        this.add(Box.createVerticalStrut(20));

        // Timer panel (for RED and BLUE timers)
        JPanel timerPanel = new JPanel(new GridLayout(1, 2, 50, 0)); // 50px gap
        this.add(timerPanel);

        // Red player timer
        redTimerLabel = new JLabel("RED Time Left: 1m 20s", SwingConstants.LEFT);
        redTimerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(redTimerLabel);

        // Blue player timer
        blueTimerLabel = new JLabel("BLUE Time Left: 1m 20s", SwingConstants.RIGHT);
        blueTimerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(blueTimerLabel);
    }

    // Update the current player label with color
    public void setCurrentPlayer(String player) {
        String colorCode = player.equalsIgnoreCase("RED") ? "red" : "blue";
        currentPlayerLabel.setText("<html><b>PLAYER TURN: <span style='color:" + colorCode + ";'>" + player + "</span></b></html>");
    }

    // Update timer labels
    public void updateRedTimer(String time) {
        redTimerLabel.setText("RED Time Left: " + time);
    }

    public void updateBlueTimer(String time) {
        blueTimerLabel.setText("BLUE Time Left: " + time);
    }
}
