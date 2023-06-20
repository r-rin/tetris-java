/* File: Leaderboard.java
 * Authors: Rafikov Rinat
 * Class, that is used to store and edit rounds and their stats.
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Leaderboard {

    GameWindow gameWindow;
    String currentName = "Player001";
    ArrayList<RoundStats> leaderboard = new ArrayList<>();

    public Leaderboard(GameWindow gameWindow){
        this.gameWindow = gameWindow;
    }

    public void openDialog(){
        LeaderboardDialog leaderboardDialog = new LeaderboardDialog(gameWindow, "Leaderboard", true);
        leaderboardDialog.pack();
        leaderboardDialog.loadTableData(getTableData());
        leaderboardDialog.setVisible(true);
    }

    public void addNewRoundResult(RoundStats stats){
        leaderboard.add(stats);
    }

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

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public void setLeaderboard(ArrayList<RoundStats> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public ArrayList<RoundStats> getLeaderboard() {
        return leaderboard;
    }
}
