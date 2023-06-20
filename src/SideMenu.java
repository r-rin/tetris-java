/* File: SideMenu.java
 * Authors: Rafikov Rinat, ?
 * Клас, який описує бічне меню, у якому будуть міститися різні кнопки, налаштування, панель вибору ускладнень тощо.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SideMenu extends JPanel {

    GameWindow gameWindow;

    ScoreCounter scoreCounter = new ScoreCounter();
    public SideMenu(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        this.setLayout(new GridBagLayout());
        this.setBorder(new LineBorder(Color.black, 1));
        this.setBackground(Color.BLACK);

        initHolderPanel();
    }

    private void initHolderPanel() {
        JPanel holderPanel = new JPanel();
        holderPanel.setBackground(Color.BLACK);
        holderPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        holderPanel.setLayout(new GridLayout(0, 1));

        JLabel title = new JLabel("D A R K S T A C K", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);

        holderPanel.add(title);
        holderPanel.add(scoreCounter);

        holderPanel.add(new JLabel());

        JButton startRound = new JButton("START");
        startRound.setBackground(Color.DARK_GRAY);
        startRound.setBorder(new LineBorder(Color.WHITE, 2, true));
        startRound.setForeground(Color.WHITE);
        startRound.setFocusable(false);
        startRound.addActionListener(e -> {
            try {
                gameWindow.wrapper.removeAll();
                gameWindow.tetrisField = new TetrisField(gameWindow.settings, gameWindow);
                gameWindow.wrapper.add(gameWindow.tetrisField);
                gameWindow.wrapper.revalidate();
                gameWindow.wrapper.repaint();
                gameWindow.pack();
                gameWindow.getTetrisField().gameHandler.restart(startRound);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        holderPanel.add(startRound);

        JButton openSettings = new JButton("SETTINGS");
        openSettings.setBackground(Color.DARK_GRAY);
        openSettings.setBorder(new LineBorder(Color.WHITE, 1, true));
        openSettings.setForeground(Color.WHITE);
        openSettings.setFocusable(false);
        openSettings.addActionListener(e -> {
            SettingsDialog settingsDialog = new SettingsDialog(gameWindow, "Game Settings", true);
            settingsDialog.setVisible(true);
        });

        JButton openLeaderboard = new JButton("LEADERBOARD");
        openLeaderboard.setBackground(Color.DARK_GRAY);
        openLeaderboard.setBorder(new LineBorder(Color.WHITE, 1, true));
        openLeaderboard.setForeground(Color.WHITE);
        openLeaderboard.setFocusable(false);
        openLeaderboard.addActionListener(e -> {
            gameWindow.leaderboard.openDialog();
        });

        holderPanel.add(openSettings);
        holderPanel.add(openLeaderboard);

        try {
            Font customFont = new Font("RupturedSans", Font.PLAIN, 36);
            title.setFont(customFont);

            scoreCounter.scoreLabel.setFont(new Font("Sathu", Font.BOLD, 12));
            scoreCounter.multiplierLabel.setFont(new Font("Sathu", Font.BOLD, 13));

            openSettings.setFont(new Font("Sathu", Font.BOLD, 20));
            startRound.setFont(new Font("Sathu", Font.BOLD, 20));
            openLeaderboard.setFont(new Font("Sathu", Font.BOLD, 20));

        } catch (Exception e){
            System.out.println("Unable to load title font.");
        }

        this.add(holderPanel, new GridBagConstraints());
    }
}
