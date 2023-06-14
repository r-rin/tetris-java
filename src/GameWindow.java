/* File: GameWindow.java
 * Authors: Rafikov Rinat, ?
 * Клас, який створює ігрове вікно та додає базові елементи(бічне меню, ігрове поле).
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;

class GameWindow extends JFrame {

    TetrisField tetrisField;
    HashMap<GameSettings, Object> settings = new HashMap<>();
    SideMenu sideMenu;
    JPanel wrapper;
    public GameWindow() {
        this.setMinimumSize(new Dimension(800, 500));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setResizable(true);
        this.setLocationRelativeTo(null);

        setBasicSettings();

        initWindowPanels();

        this.pack();
        this.setVisible(true);
    }

    private void setBasicSettings() {
        settings.put(GameSettings.NROWS, 16);
        settings.put(GameSettings.NCOLUMNS, 8);
        settings.put(GameSettings.GRIDSIZE, 35);
        settings.put(GameSettings.FRAMEGAP, 0.1);
        settings.put(GameSettings.FRAMESFORFALL, 25);
        settings.put(GameSettings.CANMOVEUP, false);
    }

    private void initWindowPanels() {

        //Налаштування розміру ігрового поля
        //TODO: Перетворити на кнопки, слайдери у бічному меню.

        sideMenu = new SideMenu(this);
        tetrisField = new TetrisField(settings);

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