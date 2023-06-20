/* File: TetrisField.java
 * Authors: Rafikov Rinat
 * Class, that creates playing field.
 */

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.HashMap;

public class TetrisField extends JPanel {

    private boolean isGridVisible = true;
    private int columns;
    private int rows;
    private int gridSize;

    GameHandler gameHandler;
    GameWindow gameWindow;

    /**
     * Empty constructor for TetrisField.
     */
    public TetrisField() {}

    /**
     * Constructs a TetrisField with the specified number of columns, rows, and grid size.
     *
     * @param columns   The number of columns in the TetrisField.
     * @param rows      The number of rows in the TetrisField.
     * @param gridSize  The size of each grid cell in the TetrisField.
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

    /**
     * Constructs a TetrisField based on the provided settings and associated with the given GameWindow.
     *
     * @param settings    The settings for the TetrisField.
     * @param gameWindow  The GameWindow associated with the TetrisField.
     */
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
     * Paints the objects on the TetrisField. This method is called when the field is added and when repaint() is invoked.
     *
     * @param g The graphics parameter.
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
     * Draws the grid on the TetrisField.
     *
     * @param g     The graphics parameter.
     * @param color The color of the grid lines.
     */
    public void drawGridField(Graphics g, Color color) {
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                Grid grid = new Grid(column, row, gridSize, null);
                grid.drawGrid(g, color);
            }
        }
    }

    /**
     * Sets the visibility of the grid on the TetrisField.
     *
     * @param gridVisible  True to show the grid, false to hide it.
     */
    public void setGridVisible(boolean gridVisible) {
        isGridVisible = gridVisible;
    }

    /**
     * Returns the number of columns in the TetrisField.
     *
     * @return The number of columns.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Returns the size of each grid cell in the TetrisField.
     *
     * @return The grid size.
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * Returns the number of rows in the TetrisField.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Sets the number of columns in the TetrisField.
     *
     * @param columns The number of columns to set.
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Sets the number of rows in the TetrisField.
     *
     * @param rows The number of rows to set.
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Sets the size of each grid cell in the TetrisField.
     *
     * @param gridSize The grid size to set.
     */
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }
}
