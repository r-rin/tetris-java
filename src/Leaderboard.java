/* File: Leaderboard.java
 * Authors: Rafikov Rinat
 * Class, that is used to store and edit rounds and their stats.
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Leaderboard {

    /**
     * The GameWindow instance associated with the leaderboard.
     */
    GameWindow gameWindow;

    /**
     * The current name to be displayed on the leaderboard.
     */
    String currentName = "Player001";

    /**
     * The list of round statistics representing the leaderboard.
     */
    ArrayList<RoundStats> leaderboard = new ArrayList<>();


    /**
     * Constructs a Leaderboard object with a reference to the GameWindow instance. The leaderboard is used to display and manage
     * round statistics for the game.
     *
     * @param gameWindow The GameWindow instance associated with the leaderboard.
     */
    public Leaderboard(GameWindow gameWindow){
        this.gameWindow = gameWindow;
    }

    /**
     * Opens the leaderboard dialog. Creates an instance of LeaderboardDialog, initializes it with the necessary data,
     * and displays it to the user.
     */
    public void openDialog(){
        LeaderboardDialog leaderboardDialog = new LeaderboardDialog(gameWindow, "Leaderboard", true);
        leaderboardDialog.pack();
        leaderboardDialog.loadTableData(getTableData());
        leaderboardDialog.setVisible(true);
    }

    /**
     * Adds a new round result to the leaderboard. The provided RoundStats object is appended to the leaderboard.
     *
     * @param stats The RoundStats object representing the statistics of the completed round.
     */
    public void addNewRoundResult(RoundStats stats){
        leaderboard.add(stats);
    }

    /**
     * Retrieves the table data for the leaderboard. Constructs a two-dimensional array of objects containing the necessary
     * data for each row of the leaderboard table. The table data includes round statistics, such as player name, points,
     * multiplier, time, and a settings button for each row.
     *
     * @return The two-dimensional array of objects representing the table data for the leaderboard.
     */
    public Object[][] getTableData(){
        Object[][] dataArray = new Object[leaderboard.size()][5];
        for(int row = 0; row < dataArray.length; row++){
            RoundStats currentStat = leaderboard.get(row);
            JButton settingsButton = new JButton("Settings");
            settingsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SettingsDialog settingsDialog = new SettingsDialog(gameWindow, "Round Settings of "+currentStat.playerName, true);
                    settingsDialog.setVisible(true);
                }
            });

            dataArray[row] = new Object[]{currentStat, currentStat.amountOfPoints, currentStat.multiplier, currentStat.turnSecondsIntoTime(), settingsButton};
        }
        return dataArray;
    }

    /**
     * Sets the current name to be displayed on the leaderboard. This is used to update the name of the current player.
     *
     * @param currentName The name of the current player.
     */
    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    /**
     * Sets the leaderboard with a new list of round statistics. Replaces the existing leaderboard with the provided list.
     *
     * @param leaderboard The ArrayList of RoundStats representing the updated leaderboard.
     */
    public void setLeaderboard(ArrayList<RoundStats> leaderboard) {
        this.leaderboard = leaderboard;
    }

    /**
     * Retrieves the current leaderboard. Returns the ArrayList of RoundStats representing the current state of the leaderboard.
     *
     * @return The ArrayList of RoundStats representing the leaderboard.
     */
    public ArrayList<RoundStats> getLeaderboard() {
        return leaderboard;
    }
}
