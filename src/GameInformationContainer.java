import javax.swing.*;
import java.awt.*;

public class GameInformationContainer extends JPanel {
    public JLabel currentPlayerLabel;
    public JLabel redTimerLabel;
    public JLabel blueTimerLabel;
    public Timer redTimer;
    public Timer blueTimer;
    public int redSecondsLeft = 20;   // Example: 1m 20s = 80 seconds
    public int blueSecondsLeft = 20;

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
        setCurrentPlayer(Checkers.playerTurn); // Default player
        // On label click change playerTurn
        currentPlayerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Checkers.switchPlayerTurn();
            }
        });
        playerTurnPanel.add(currentPlayerLabel);
        this.add(Box.createVerticalStrut(10));

        // Timer panel (for RED and BLUE timers)
        JPanel timerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        this.add(timerPanel);

        // Red player timer
        redTimerLabel = new JLabel("RED Time Left: " + formatTime(redSecondsLeft), SwingConstants.LEFT);
        redTimerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(redTimerLabel);

        // Blue player timer
        blueTimerLabel = new JLabel("BLUE Time Left: " + formatTime(blueSecondsLeft), SwingConstants.RIGHT);
        blueTimerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(blueTimerLabel);

        startTimers();
    }

    // Update the current player label with color
    public void setCurrentPlayer(PlayerTurn player) {
        String colorCode = player.equals(PlayerTurn.RED) ? "red" : "blue";
        currentPlayerLabel.setText("<html><b>PLAYER TURN: <span style='color:" + colorCode + ";'>" + player + "</span></b></html>");
    }

    // Update timer labels
    public void updateRedTimer(String time) {
        redTimerLabel.setText("RED Time Left: " + time);
    }

    public void updateBlueTimer(String time) {
        blueTimerLabel.setText("BLUE Time Left: " + time);
    }

    public String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%dm %02ds", minutes, seconds);
    }

    public void startTimers() {
        redTimer = new Timer(1000, e -> {
            // After any timer reaches 0, stop both
            if(blueSecondsLeft == 0 || redSecondsLeft == 0){
                return;
            }

            if (Checkers.playerTurn == PlayerTurn.RED) {
                redSecondsLeft--;
                updateRedTimer(formatTime(redSecondsLeft));
            }
        });

        blueTimer = new Timer(1000, e -> {
            // After any timer reaches 0, stop both
            if(blueSecondsLeft == 0 || redSecondsLeft == 0){
                return;
            }

            if (Checkers.playerTurn == PlayerTurn.BLUE) {
                blueSecondsLeft--;
                updateBlueTimer(formatTime(blueSecondsLeft));
            }
        });

        redTimer.start();
        blueTimer.start();
    }


    public void resetTimers() {
        redSecondsLeft = 80;
        blueSecondsLeft = 80;
        updateRedTimer(formatTime(redSecondsLeft));
        updateBlueTimer(formatTime(blueSecondsLeft));
    }

    public void stopRedTimer(){
        if (redTimer != null) redTimer.stop();
    }

    public void stopBlueTimer(){
        if (blueTimer != null) blueTimer.stop();
    }

    public void stopBothTimers() {
        stopRedTimer();
        stopBlueTimer();
    }
}
