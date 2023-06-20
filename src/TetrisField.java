/* File: TetrisField.java
 * Authors: Rafikov Rinat
 * Class, that creates playing field.
 */

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.HashMap;

public class TetrisField extends JPanel {

    //Налаштування
    private boolean isGridVisible = true;

    //Змінні екземпляру, які зберігають налаштування поля
    private int columns;
    private int rows;
    private int gridSize;

    GameHandler gameHandler;
    GameWindow gameWindow;

    /**
     * Порожній конструктор
     */
    public TetrisField() {}

    /**
     * Конструктор, який створює ігрове поле за заданими даними
     * @param columns кількість стовпців
     * @param rows кількість рядочків
     * @param gridSize розмір сторони клітинки поля
     */
    public TetrisField(int columns, int rows, int gridSize) {
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
        this.setBackground(Color.BLACK);
        this.columns = columns;
        this.gridSize = gridSize;
        this.rows = rows;

        int panelWidth = columns * gridSize + 3;
        int panelHeight = rows * gridSize + 3;

        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setMaximumSize(new Dimension(panelWidth, panelHeight));

        this.setBorder(new BevelBorder(1));

        gameHandler = new GameHandler(this);
    }

    public TetrisField(HashMap<GameSettings, Object> settings, GameWindow gameWindow) {
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
        this.setBackground(Color.DARK_GRAY);
        this.gameWindow = gameWindow;
        this.columns = (int) settings.get(GameSettings.NCOLUMNS);
        this.gridSize = (int) settings.get(GameSettings.GRIDSIZE);
        this.rows = (int) settings.get(GameSettings.NROWS);

        int panelWidth = columns * gridSize + 2;
        int panelHeight = rows * gridSize + 2;

        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setMaximumSize(new Dimension(panelWidth, panelHeight));

        this.setBorder(new BevelBorder(1));

        gameHandler = new GameHandler(this);

        gameHandler.setFramesPerFall((int) settings.get(GameSettings.FRAMESFORFALL));
        gameHandler.setTimeBetweenFrames((int) settings.get(GameSettings.FRAMEGAP));
    }

    /**
     * Метод, який малює обʼєкти поля, викликається при додаванні поля та repaint().
     * @param g графічний параметр
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if(isGridVisible){
            drawGridField(g, Color.DARK_GRAY);
        }
        gameHandler.drawFrame(g);
    }

    /**
     * Метод, який малює сітку.
     * @param g графічний параметр.
     */
    public void drawGridField(Graphics g, Color color) {
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                Grid grid = new Grid(column, row, gridSize, null);
                grid.drawGrid(g, color);
            }
        }
    }

    public void setGridVisible(boolean gridVisible) {
        isGridVisible = gridVisible;
    }

    public int getColumns() {
        return columns;
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getRows() {
        return rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }
}
