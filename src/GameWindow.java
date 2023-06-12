/* File: GameWindow.java
 * Authors: Rafikov Rinat, ?
 * Клас, який створює ігрове вікно та додає базові елементи(бічне меню, ігрове поле).
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class GameWindow extends JFrame {

    TetrisField tetrisField;
    SideMenu sideMenu;
    public GameWindow() {
        this.setMinimumSize(new Dimension(800, 500));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setResizable(true);
        this.setLocationRelativeTo(null);

        initWindowPanels();

        this.pack();
        this.setVisible(true);
    }

    private void initWindowPanels() {

        //Налаштування розміру ігрового поля
        //TODO: Перетворити на кнопки, слайдери у бічному меню.

        int columns = 10;
        int rows = 15;
        int gridSize = 30;

        sideMenu = new SideMenu(this);
        tetrisField = new TetrisField(columns, rows, gridSize);

        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.black);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.LINE_AXIS));
        wrapper.setBorder(new EmptyBorder(0, 50, 0, 50));

        wrapper.add(tetrisField);

        this.add(sideMenu, BorderLayout.CENTER);
        this.add(wrapper, BorderLayout.EAST);
    }

    public TetrisField getTetrisField() {
        return tetrisField;
    }
}