/* File: TetrisField.java
 * Authors: Rafikov Rinat, ?
 * Клас, який створює та малює ігрове поле
 */

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class TetrisField extends JPanel {

    //Змінні екземпляру, які зберігають налаштування поля
    private int columns;
    private int rows;
    private int gridSize;

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

        this.columns = columns;
        this.gridSize = gridSize;
        this.rows = rows;

        int panelWidth = columns * gridSize;
        int panelHeight = rows * gridSize;

        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setMaximumSize(new Dimension(panelWidth, panelHeight));

        this.setBorder(new BevelBorder(1));
    }

    /**
     * Метод, який малює обʼєкти поля, викликається при додаванні поля та repaint().
     * @param g графічний параметр
     */
    @Override
    protected void paintComponent(Graphics g) {

        //drawGridfield(g);
        Figure line1 = new Figure(new int[][]{{1, 1, 1, 1}}, 2, 2, gridSize);
        line1.drawFigure(g, Color.orange);

        Figure line2 = new Figure(new int[][]{{1}, {1}, {0}, {1}, {1}}, 1, 0, gridSize);
        line2.drawFigure(g, Color.RED);

        System.out.println(line2.checkCollisionWith(line1, Sides.BOTTOM));
    }

    /**
     * Метод, який малює сітку.
     * @param g графічний параметр.
     */
    public void drawGridField(Graphics g, Color color) {
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                Grid grid = new Grid(column, row, gridSize);
                grid.drawGrid(g, color);
            }
        }
    }
}
