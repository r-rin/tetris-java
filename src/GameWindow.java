/* File: GameWindow.java
 * Authors: Rafikov Rinat, ?
 * Клас, який створює ігрове вікно та додає базові елементи(бічне меню, ігрове поле).
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class GameWindow extends JFrame {

    public GameWindow() {
        this.setSize(new Dimension(800, 500));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        initWindowPanels();

        this.setVisible(true);
    }

    private void initWindowPanels() {

        //Налаштування розміру ігрового поля
        //TODO: Перетворити на кнопки, слайдери у бічному меню.
        int columns = 18;
        int rows = 6;
        int gridSize = 30;

        SideMenu sideMenu = new SideMenu();
        TetrisField tetrisField = new TetrisField(columns, rows, gridSize);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.LINE_AXIS));
        wrapper.setBorder(new EmptyBorder(0, 50, 0, 50));

        wrapper.add(tetrisField);

        this.add(sideMenu, BorderLayout.CENTER);
        this.add(wrapper, BorderLayout.EAST);
    }
}