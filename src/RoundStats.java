import java.io.Serializable;
import java.util.HashMap;

public class RoundStats implements Serializable {

    String playerName;
    HashMap<GameSettings, Object> roundSettings;
    int amountOfPoints;
    long roundTimeSecs;
    double multiplier;


    public RoundStats(String playername, double amountOfPoints, double multiplier, long roundTimeSecs, HashMap<GameSettings, Object> roundSettings){
        this.playerName = playername;
        this.roundSettings = roundSettings;
        this.amountOfPoints = (int) amountOfPoints;
        this.roundTimeSecs = roundTimeSecs;
        this.multiplier = multiplier;
    }

    public String turnSecondsIntoTime(){
        long hours = roundTimeSecs / 3600;
        long minutes = (roundTimeSecs % 3600) / 60;
        long seconds = roundTimeSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String toString() {
        return playerName;
    }
}
