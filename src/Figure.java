/* File: Figure.java
 * Authors: Rafikov Rinat
 * Class, which describes a figure.
 */

import java.awt.*;
import java.util.ArrayList;

public class Figure {

    // Instance variables
    private int[][] figureForm; // Defines the shape of the figure
    private Grid[] usedGrids;
    private int x; // Relative coordinates of the figure's top-left corner
    private int y; // Relative coordinates
    private Color color;
    private final TetrisField field;

/*
    Relative coordinate means that we specify the coordinates of the grid on the TetrisField, not on the JPanel.
    The shape of the grid is represented as a two-dimensional array. For example, if the array is
    {{1, 0},                                             ■  0
     {1, 0},    ===> then the resulting figure will be   ■  0
     {1, 1}}                                             ■  ■
 */

    /**
     * Constructor that creates a Figure object.
     *
     * @param figureForm The shape of the figure as a two-dimensional array,
     *                   where 1 represents filled cells and 0 represents empty cells.
     * @param x          The x-coordinate of the top-left corner of the figure.
     * @param y          The y-coordinate of the top-left corner of the figure.
     * @param field      The TetrisField object representing the game field.
     * @param color      The color of the figure.
     */
    public Figure(int[][] figureForm, int x, int y, TetrisField field, Color color) {
        this.figureForm = figureForm;
        this.x = x;
        this.y = y;
        this.field = field;
        this.color = color;
        this.usedGrids = findGrids();
    }

    /**
     * Finds the grids occupied by the figure.
     *
     * @return An array of Grid objects representing the occupied grids.
     */
    public Grid[] findGrids() {
        ArrayList<Grid> gridList = new ArrayList<>();
        for (int row = 0; row < figureForm.length; row++) {
            int[] currentRow = figureForm[row];
            for (int column = 0; column < currentRow.length; column++) {
                if (currentRow[column] == 1) {
                    Grid grid = new Grid(x + column, y + row, field.getGridSize(), color);
                    gridList.add(grid);
                }
            }
        }
        return gridList.toArray(Grid[]::new);
    }

    /**
     * Draws the figure on the field using the specified color.
     *
     * @param g     The Graphics parameter of the field on which the figure will be drawn.
     * @param color The color of the figure.
     */
    public void drawFigure(Graphics g, Color color) {
        for (Grid grid : usedGrids) {
            grid.fillGrid(g, color);
            grid.drawGrid(g, color.darker().darker());
        }
    }

    /**
     * Draws the figure on the field using its default color.
     *
     * @param g The Graphics parameter of the field on which the figure will be drawn.
     */
    public void drawFigure(Graphics g) {
        for (Grid grid : usedGrids) {
            grid.fillGrid(g);
            grid.drawGrid(g);
        }
    }

    /**
     * Checks if the figure collides with another figure based on the movement vector.
     *
     * @param figure         The Figure object to check collision with.
     * @param movementVector The movement vector of the figure.
     * @return True if there is a collision, False otherwise.
     */
    public boolean checkCollisionWith(Figure figure, Sides movementVector) {
        for (Grid figureGrid : usedGrids) {
            for (Grid givenFigureGrid : figure.getUsedGrids()) {
                if (figureGrid.checkCollisionWith(givenFigureGrid, movementVector))
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if the figure collides with a specific grid based on the movement vector.
     *
     * @param grid           The Grid object to check collision with.
     * @param movementVector The movement vector of the figure.
     * @return True if there is a collision, False otherwise.
     */
    public boolean checkCollisionWith(Grid grid, Sides movementVector) {
        for (Grid figureGrid : usedGrids) {
            if (figureGrid.checkCollisionWith(grid, movementVector))
                return true;
        }
        return false;
    }

    /**
     * Checks if the figure exceeds the boundaries of the field in the specified movement direction.
     *
     * @param movementDirection The movement direction to check.
     * @return True if the figure exceeds the boundaries, False otherwise.
     */
    public boolean checkBorders(Sides movementDirection) {
        int figureHeight = figureForm.length;
        int figureWidth = figureForm[0].length;
        switch (movementDirection) {
            case BOTTOM:
                if (getY() + figureHeight >= field.getRows()) {
                    return true;
                }
                return false;
            case LEFT:
                if (getX() <= 0) {
                    return true;
                }
                return false;
            case RIGHT:
                if (getX() + figureWidth >= field.getColumns()) {
                    return true;
                }
                return false;
            case TOP:
                if (getY() <= 0) {
                    return true;
                }
                return false;
            case CURRENT:
                if (getX() + figureWidth > field.getColumns() || getY() + figureHeight > field.getRows()) {
                    return true;
                }
                return false;
        }
        return false;
    }

    /**
     * Rotates the figure in the specified direction.
     *
     * @param direction The direction of rotation (LEFT or RIGHT).
     * @return A new two-dimensional array representing the rotated figure.
     */
    public int[][] rotateFigure(Sides direction) {
        int figureWidth = figureForm[0].length;
        int figureHeight = figureForm.length;
        int[][] rotatedArray = new int[figureWidth][figureHeight];

        if (direction == Sides.LEFT) {
            for (int i = 0; i < figureHeight; i++) {
                for (int k = 0; k < figureWidth; k++) {
                    rotatedArray[figureWidth - k - 1][i] = figureForm[i][k];
                }
            }
        } else if (direction == Sides.RIGHT) {
            for (int i = 0; i < figureHeight; i++) {
                for (int k = 0; k < figureWidth; k++) {
                    rotatedArray[k][figureHeight - i - 1] = figureForm[i][k];
                }
            }
        }

        return rotatedArray;
    }

    /**
     * Moves the figure in the specified direction by the given offset.
     *
     * @param movementDirection The movement direction.
     * @param offset            The amount of offset for the movement.
     */
    public void move(Sides movementDirection, int offset) {
        switch (movementDirection) {
            case TOP:
                y -= offset;
                break;
            case RIGHT:
                x += offset;
                break;
            case LEFT:
                x -= offset;
                break;
            case BOTTOM:
                y += offset;
                break;
        }

        for (Grid grid : usedGrids) {
            grid.moveGrid(movementDirection, offset);
        }
    }

    /**
     * Get the y-coordinate of the figure.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Get the x-coordinate of the figure.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Set the x-coordinate of the figure.
     *
     * @param x The x-coordinate to set.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the y-coordinate of the figure.
     *
     * @param y The y-coordinate to set.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the array of used grids by the figure.
     *
     * @return An array of Grid objects representing the used grids.
     */
    public Grid[] getUsedGrids() {
        return usedGrids;
    }

    /**
     * Set the array of used grids by the figure.
     *
     * @param usedGrids The array of Grid objects representing the used grids to set.
     */
    public void setUsedGrids(Grid[] usedGrids) {
        this.usedGrids = usedGrids;
    }

    /**
     * Set the figure form (shape) represented by a two-dimensional array.
     *
     * @param figureForm The two-dimensional array representing the figure form to set.
     */
    public void setFigureForm(int[][] figureForm) {
        this.figureForm = figureForm;
    }

    /**
     * Get the figure form (shape) represented by a two-dimensional array.
     *
     * @return The two-dimensional array representing the figure form.
     */
    public int[][] getFigureForm() {
        return figureForm;
    }

    /**
     * Get the TetrisField object associated with the figure.
     *
     * @return The TetrisField object.
     */
    public TetrisField getField() {
        return field;
    }
}