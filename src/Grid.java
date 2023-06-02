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

    /*
        !Пояснення про відносні координати у класі Figure!
     */

    /**
     * Конструктор, який створює клітинку.
     * @param x відносна координата х
     * @param y відносна координата у
     * @param size розмір клітинки
     */
    public Grid(int x, int y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
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
        g.setColor(color);
        g.drawRect(x * size, y * size, size, size);
    }

    /**
     * Метод, який замальовує вміст клітинки на наданих координатам.
     * @param g графічний параметр, який визначає на якому полі буде намальовано клітинку.
     * @param color колір клітинки.
     */
    public void fillGrid(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(x * size, y * size, size, size);
    }

    public boolean checkCollisionWith(Grid grid, Sides movementVector){
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
}
