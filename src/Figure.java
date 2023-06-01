/* File: Figure.java
 * Authors: Rafikov Rinat, ?
 * Клас, який описує фігуру.
 */

import java.awt.*;

public class Figure {

    //Змінні екземпляру
    private int[][] figureForm; //Визначає форму фігури
    private int x; //Відносні координати розташування, лівий верхній кут фігури
    private int y; //Відносні координати розташування
    private int gridSize; //Розмір клітинки

    /*
        Відносна координата означає те, що ми вказуємо не координати у JPanel, а координати клітинки поля.
        Форма клітинки це двовимірний масив, наприклад якщо масив це
        {{1, 0},                                            ■  0
         {1, 0},    ===> то отримана фігура буде такою      ■  0    , нуль означає, що клітинка не фарбується.
         {1, 1}}                                            ■  ■
     */

    /**
     * Конструктор, який створює обʼєкт фігури.
     * @param figureForm форма фігури, у вигляді двовимірного масиву, де 1 - зафарбовані клітинки, 0 - порожні.
     * @param x х-координата верхнього лівого кутку фігури
     * @param y у-координата верхнього лівого кутку фігури
     * @param gridSize розмір клітинки.
     */
    public Figure(int[][] figureForm, int x, int y, int gridSize){
        this.figureForm = figureForm;
        this.x = x;
        this.y = y;
        this.gridSize = gridSize;
    }

    /**
     * Метод, який малює фігуру на полі.
     * @param g графічний параметр поля, у якому буде малюватися фігура.
     * @param color колір фігури.
     */
    public void drawFigure(Graphics g, Color color) {
        for(int row = 0; row < figureForm.length; row++){
            int[] currentRow = figureForm[row];

            for(int column = 0; column < currentRow.length; column++){

                if(currentRow[column] == 1){
                    Grid grid = new Grid(x + column, y + row, gridSize);
                    grid.fillGrid(g, color);
                    grid.drawGrid(g, color.darker().darker());
                }
            }
        }
    }


    public void setFigureForm(int[][] figureForm) {
        this.figureForm = figureForm;
    }

    public int[][] getFigureForm() {
        return figureForm;
    }
}
