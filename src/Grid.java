/* File: Grid.java
 * Authors: Rafikov Rinat
 * Class, which describes a grid/square.
 */

import java.awt.*;

/**
 * Represents a grid in a grid-based system.
 */
public class Grid {
    private int x; // Relative x-coordinate of the grid's location (top-left corner)
    private int y; // Relative y-coordinate of the grid's location
    private int size; // Size of the grid
    private boolean visible = true; // Indicates if the grid is visible
    private boolean canCollide = true; // Indicates if the grid can collide with other grids
    private Color color; // Color of the grid

    /**
     * Constructs a grid with the specified coordinates, size, and color.
     *
     * @param x     the relative x-coordinate of the grid's location
     * @param y     the relative y-coordinate of the grid's location
     * @param size  the size of the grid
     * @param color the color of the grid
     */
    public Grid(int x, int y, int size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    /**
     * Constructs a grid with the specified coordinates.
     *
     * @param x the relative x-coordinate of the grid's location
     * @param y the relative y-coordinate of the grid's location
     */
    public Grid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Draws the outline of the grid on the provided graphics object with the specified color.
     *
     * @param g     the graphics object on which to draw the grid
     * @param color the color of the grid
     */
    public void drawGrid(Graphics g, Color color) {
        if ((x >= 0 || y >= 0) && visible) {
            g.setColor(color);
            g.drawRect(x * size, y * size, size, size);
        }
    }

    /**
     * Draws the outline of the grid on the provided graphics object using the grid's color.
     *
     * @param g the graphics object on which to draw the grid
     */
    public void drawGrid(Graphics g) {
        if ((x >= 0 || y >= 0) && visible) {
            g.setColor(this.color.darker().darker());
            g.drawRect(x * size, y * size, size, size);
        }
    }

    /**
     * Fills the grid with the specified color on the provided graphics object.
     *
     * @param g     the graphics object on which to fill the grid
     * @param color the color to fill the grid with
     */
    public void fillGrid(Graphics g, Color color) {
        if ((x >= 0 || y >= 0) && visible) {
            g.setColor(color);
            g.fillRect(x * size, y * size, size, size);
        }
    }

    /**
     * Fills the grid with the grid's color on the provided graphics object.
     *
     * @param g the graphics object on which to fill the grid
     */
    public void fillGrid(Graphics g) {
        if ((x >= 0 || y >= 0) && visible) {
            g.setColor(this.color);
            g.fillRect(x * size, y * size, size, size);
        }
    }

    /**
     * Moves the grid in the specified direction by the given offset.
     *
     * @param direction the direction in which to move the grid
     * @param offset    the offset by which to move the grid
     */
    public void moveGrid(Sides direction, int offset) {
        switch (direction) {
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
    }

    /**
     * Checks if the grid collides with another grid in the specified movement vector.
     *
     * @param grid           the grid to check for collision
     * @param movementVector the movement vector to check for collision
     * @return true if the grids collide, false otherwise
     */
    public boolean checkCollisionWith(Grid grid, Sides movementVector) {
        if (grid.canCollide() && canCollide) {
            switch (movementVector) {
                case LEFT:
                    if (y == grid.getY()) {
                        return x - 1 == grid.getX();
                    }
                    break;
                case BOTTOM:
                    if (x == grid.getX()) {
                        return y + 1 == grid.getY();
                    }
                    break;
                case RIGHT:
                    if (y == grid.getY()) {
                        return x + 1 == grid.getX();
                    }
                    break;
                case TOP:
                    if (x == grid.getX()) {
                        return y - 1 == grid.getY();
                    }
                    break;
                case CURRENT:
                    return x == grid.getX() && y == grid.getY();
            }
        }
        return false;
    }

    /**
     * Returns the size of the grid.
     *
     * @return the size of the grid
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the relative x-coordinate of the grid's location.
     *
     * @return the relative x-coordinate of the grid's location
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the relative y-coordinate of the grid's location.
     *
     * @return the relative y-coordinate of the grid's location
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the visibility of the grid.
     *
     * @param visible true to make the grid visible, false otherwise
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Checks if the grid is visible.
     *
     * @return true if the grid is visible, false otherwise
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets whether the grid can collide with other grids.
     *
     * @param canCollide true if the grid can collide, false otherwise
     */
    public void setCanCollide(boolean canCollide) {
        this.canCollide = canCollide;
    }

    /**
     * Checks if the grid can collide with other grids.
     *
     * @return true if the grid can collide, false otherwise
     */
    public boolean canCollide() {
        return canCollide;
    }

    /**
     * Returns the color of the grid.
     *
     * @return the color of the grid
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the grid.
     *
     * @param color the color of the grid
     */
    public void setColor(Color color) {
        this.color = color;
    }
}

