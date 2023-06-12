/* File: Grid.java
 * Authors: Rafikov Rinat, ?
 * Клас, який описує клітинку ігрового поля.
 */

import java.awt.*;

public class Grid {

    //Змінні екземпляру
    private int x; //Відносні координати розташування, лівий верхній кут фігури
    private int y; //Відносні координати розташування
    private int size; //Розмір клітинок
    private boolean visible = true;
    private boolean canCollide = true;
    private Color color;

    /*
        !Пояснення про відносні координати у класі Figure!
     */

    /**
     * Конструктор, який створює клітинку.
     * @param x відносна координата х
     * @param y відносна координата у
     * @param size розмір клітинки
     */
    public Grid(int x, int y, int size, Color color){
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    /**
     * Конструктор, який створює клітинку.
     * @param x відносна координата х
     * @param y відносна координата у
     */
    public Grid(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Метод, який малює контур клітинки на наданих координатам.
     * @param g графічний параметр, який визначає на якому полі буде намальовано клітинку.
     * @param color колір клітинки.
     */
    public void drawGrid(Graphics g, Color color) {
        if((x >= 0 || y >= 0) && visible){
            g.setColor(color);
            g.drawRect(x * size, y * size, size, size);
        }
    }

    public void drawGrid(Graphics g) {
        if((x >= 0 || y >= 0) && visible){
            g.setColor(this.color.darker().darker());
            g.drawRect(x * size, y * size, size, size);
        }
    }

    /**
     * Метод, який замальовує вміст клітинки на наданих координатам.
     * @param g графічний параметр, який визначає на якому полі буде намальовано клітинку.
     * @param color колір клітинки.
     */
    public void fillGrid(Graphics g, Color color) {
        if((x >= 0 || y >= 0) && visible) {
            g.setColor(color);
            g.fillRect(x * size, y * size, size, size);
        }
    }

    public void fillGrid(Graphics g) {
        if((x >= 0 || y >= 0) && visible) {
            g.setColor(this.color);
            g.fillRect(x * size, y * size, size, size);
        }
    }

    public void moveGrid(Sides direction, int offset){
        switch (direction){
            case TOP -> {
                y -= offset;
            }
            case RIGHT -> {
                x += offset;
            }
            case LEFT -> {
                x -= offset;
            }
            case BOTTOM -> {
                y += offset;
            }
        }
    }

    public boolean checkCollisionWith(Grid grid, Sides movementVector){
        if(grid.canCollide() && canCollide){
            switch (movementVector) {
                case LEFT -> {
                    if (y == grid.getY()) {
                        return x - 1 == grid.getX();
                    }
                }
                case BOTTOM -> {
                    if (x == grid.getX()) {
                        return y + 1 == grid.getY();
                    }
                }
                case RIGHT -> {
                    if (y == grid.getY()) {
                        return x + 1 == grid.getX();
                    }
                }
                case TOP -> {
                    if (x == grid.getX()) {
                        return y - 1 == grid.getY();
                    }
                }
                case CURRENT -> {
                    return x == grid.getX() && y == grid.getY();
                }
            }
        }
        return false;
    }
    public int getSize() {
        return size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setCanCollide(boolean canCollide) {
        this.canCollide = canCollide;
    }

    public boolean canCollide() {
        return canCollide;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
