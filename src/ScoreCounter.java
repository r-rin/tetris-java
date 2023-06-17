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

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
        multiplierLabel.setText("Multiplier: " + multiplier);
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void addPoints(int amount){
        score += amount * multiplier;
        scoreLabel.setText(String.valueOf((int) score));
    }

    public void setScore(int amount) {
        this.score = amount;
    }

    public void countMultiplier(HashMap<GameSettings, Object> settings){
        int nRows = (int) settings.get(GameSettings.NROWS);
        int nColumns = (int) settings.get(GameSettings.NCOLUMNS);
        int timeGap = (int) settings.get(GameSettings.FRAMEGAP);
        int framesForFall = (int) settings.get(GameSettings.FRAMESFORFALL);
        boolean canMoveUp = (boolean) settings.get(GameSettings.CANMOVEUP);

        double mult = (400.0/(nRows * nColumns)) * (100.0/timeGap) * (20.0/framesForFall);

        if(canMoveUp){
            mult *= 0.1;
        }

        setMultiplier(mult);
    }
}
