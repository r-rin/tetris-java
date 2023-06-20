/* File: GameWindow.java
 * Authors: Rafikov Rinat
 * Class, that creates game windows and adds basic user interface.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

class GameWindow extends JFrame {

    TetrisField tetrisField;
    HashMap<GameSettings, Object> settings = new HashMap<>();
    SideMenu sideMenu;
    JPanel wrapper;

    Leaderboard leaderboard = new Leaderboard(this);
    public GameWindow() {
        this.setMinimumSize(new Dimension(800, 500));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        setBasicSettings();

        initWindowPanels();

        this.pack();
        this.setVisible(true);
    }

    private void setBasicSettings() {
        settings.put(GameSettings.NROWS, 20);
        settings.put(GameSettings.NCOLUMNS, 20);
        settings.put(GameSettings.GRIDSIZE, 20);
        settings.put(GameSettings.FRAMEGAP, 100);
        settings.put(GameSettings.FRAMESFORFALL, 20);
        settings.put(GameSettings.CANMOVEUP, false);
        settings.put(GameSettings.DRAWGRIDNET, true);
        settings.put(GameSettings.LIMITEDVISION, false);
        settings.put(GameSettings.VISIONDISTANCE, 3.0);
        settings.put(GameSettings.CONTROLSSHUFFLE, false);
        settings.put(GameSettings.RANDOMMOVES, false);
        settings.put(GameSettings.RANDOMMOVEEACHNFRAME, 30);
        settings.put(GameSettings.DISABLEANYUPMOVES, true);
    }

    private void initWindowPanels() {
        sideMenu = new SideMenu(this);
        tetrisField = new TetrisField(settings, this);

        wrapper = new JPanel();
        wrapper.setBackground(Color.black);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.LINE_AXIS));
        wrapper.setBorder(new EmptyBorder(50, 50, 50, 50));

        wrapper.add(tetrisField);

        this.add(sideMenu, BorderLayout.CENTER);
        this.add(wrapper, BorderLayout.EAST);
    }

    public TetrisField getTetrisField() {
        return tetrisField;
    }
}