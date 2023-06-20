/* File: ScoreCounter.java
 * Authors: Rafikov Rinat
 * Represents a score counter in a game.
 * Tracks the current score and multiplier values.
 */


import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.HashMap;

public class ScoreCounter extends JPanel {

    double score = 0;
    double multiplier = 1;

    JLabel scoreLabel = new JLabel();
    JLabel multiplierLabel = new JLabel();

    /**
     * Constructs a ScoreCounter object.
     * Creates a panel to display the score and multiplier.
     * Initializes the score and multiplier labels with default values.
     * Sets the background and layout of the score container and multiplier container.
     * Adds the score and multiplier containers to the ScoreCounter panel.
     */
    public ScoreCounter(){
        JPanel scoreContainer = new JPanel();
        scoreContainer.setLayout(new BorderLayout());
        scoreContainer.setBorder(new LineBorder(Color.WHITE, 2));
        scoreContainer.setPreferredSize(new Dimension(330, 50));
        scoreContainer.setBackground(Color.BLACK);
        this.setBackground(Color.BLACK);

        JPanel multiplierContainer = new JPanel();
        multiplierContainer.setLayout(new GridLayout());
        multiplierContainer.setBorder(new MatteBorder(2, 0, 0, 0, Color.WHITE));
        multiplierContainer.setBackground(Color.BLACK);

        scoreLabel.setText(String.valueOf((int) score));
        scoreLabel.setForeground(Color.WHITE);
        multiplierLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        multiplierLabel.setText("Multiplier: " + multiplier);
        multiplierLabel.setHorizontalAlignment(SwingConstants.CENTER);
        multiplierLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        multiplierContainer.add(multiplierLabel);

        scoreContainer.add(scoreLabel);
        scoreContainer.add(multiplierContainer, BorderLayout.SOUTH);

        this.add(scoreContainer);
    }

    /**
     * Sets the multiplier value for the score counter.
     * @param multiplier The new multiplier value.
     */
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
        multiplierLabel.setText("Multiplier: " + multiplier);
    }

    /**
     * Retrieves the current multiplier value of the score counter.
     * @return The current multiplier value.
     */
    public double getMultiplier() {
        return multiplier;
    }

    /**
     * Adds points to the score counter based on the specified amount and the current multiplier.
     * Updates the score label accordingly.
     * @param amount The amount of points to add.
     */
    public void addPoints(int amount){
        score += amount * multiplier;
        scoreLabel.setText(String.valueOf((int) score));
    }

    /**
     * Sets the score value of the score counter.
     * @param amount The new score value.
     */
    public void setScore(int amount) {
        this.score = amount;
    }

    /**
     * Counts the multiplier value based on the given game settings.
     * Updates the multiplier label accordingly.
     * @param settings The game settings used to calculate the multiplier.
     */
    public void countMultiplier(HashMap<GameSettings, Object> settings){
        int nRows = (int) settings.get(GameSettings.NROWS);
        int nColumns = (int) settings.get(GameSettings.NCOLUMNS);
        int timeGap = (int) settings.get(GameSettings.FRAMEGAP);
        int framesForFall = (int) settings.get(GameSettings.FRAMESFORFALL);
        boolean canMoveUp = (boolean) settings.get(GameSettings.CANMOVEUP);
        boolean disableMoveUp = (boolean) settings.get(GameSettings.DISABLEANYUPMOVES);
        boolean shuffledControls = (boolean) settings.get(GameSettings.CONTROLSSHUFFLE);
        boolean doRandomMoves = (boolean) settings.get(GameSettings.RANDOMMOVES);
        int randMoveEachFrame = (int) settings.get(GameSettings.RANDOMMOVEEACHNFRAME);
        boolean limitedVision = (boolean) settings.get(GameSettings.LIMITEDVISION);
        double visionDistance = (double) settings.get(GameSettings.VISIONDISTANCE);

        double mult = (400.0/(nRows * nColumns)) * (100.0/timeGap) * (20.0/framesForFall);

        if(canMoveUp && !disableMoveUp){
            mult *= 0.1;
        }

        if(disableMoveUp){
            if(shuffledControls){
                mult *= 1.5;
            }
            if (doRandomMoves){
                mult *= (30.0/randMoveEachFrame);
            }
        }

        if(limitedVision){
            mult *= Math.min(nRows, nColumns)/visionDistance;
        }

        String numToString = String.valueOf(mult);

        int decimalIndex = numToString.indexOf(".");
        numToString = numToString.substring(0, decimalIndex + 2);

        setMultiplier(Double.parseDouble(numToString));
    }
}
