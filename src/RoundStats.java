/* File: RoundStats.java
 * Authors: Rafikov Rinat
 * Represents the statistics of a round in the game.
 * Contains information such as the player name, round settings, amount of points, round time in seconds, and multiplier.
 */


import java.io.Serializable;
import java.util.HashMap;

public class RoundStats implements Serializable {

    /**
     * The name of the player associated with the round statistics.
     */
    String playerName;

    /**
     * The round settings for the specific round.
     * It is a HashMap that maps GameSettings to their corresponding values.
     */
    HashMap<GameSettings, Object> roundSettings;

    /**
     * The amount of points earned in the round.
     */
    int amountOfPoints;

    /**
     * The duration of the round in seconds.
     */
    long roundTimeSecs;

    /**
     * The multiplier applied to the points earned in the round.
     */
    double multiplier;

    /**
     * Creates a new RoundStats object with the specified player name, amount of points, multiplier, round time in seconds,
     * and round settings.
     *
     * @param playerName     The name of the player associated with the round statistics.
     * @param amountOfPoints The amount of points earned in the round.
     * @param multiplier     The multiplier applied to the points earned in the round.
     * @param roundTimeSecs  The duration of the round in seconds.
     * @param roundSettings  The round settings for the specific round, represented as a HashMap mapping GameSettings to their corresponding values.
     */
    public RoundStats(String playerName, double amountOfPoints, double multiplier, long roundTimeSecs, HashMap<GameSettings, Object> roundSettings){
        this.playerName = playerName;
        this.roundSettings = roundSettings;
        this.amountOfPoints = (int) amountOfPoints;
        this.roundTimeSecs = roundTimeSecs;
        this.multiplier = multiplier;
    }

    /**
     * Converts the round time in seconds into a formatted string representation of the time in the format "HH:MM:SS".
     *
     * @return The formatted time string.
     */
    public String turnSecondsIntoTime(){
        long hours = roundTimeSecs / 3600;
        long minutes = (roundTimeSecs % 3600) / 60;
        long seconds = roundTimeSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Returns the player name as a string representation of the RoundStats object.
     *
     * @return The player name.
     */
    @Override
    public String toString() {
        return playerName;
    }
}
