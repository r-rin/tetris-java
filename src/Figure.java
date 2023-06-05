/* File: Figure.java
 * Authors: Rafikov Rinat, ?
 * Клас, який описує фігуру.
 */

import java.awt.*;
import java.util.ArrayList;

public class Figure {

    //Змінні екземпляру
    private int[][] figureForm; //Визначає форму фігури
    private Grid[] usedGrids;
    private int x; //Відносні координати розташування, лівий верхній кут фігури
    private int y; //Відносні координати розташування
    private Color color;
    private TetrisField field;

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
     * @param field поле гри
     * @param color колір
     */
    public Figure(int[][] figureForm, int x, int y, TetrisField field, Color color){
        this.figureForm = figureForm;
        this.x = x;
        this.y = y;
        this.field = field;
        this.color = color;
        this.usedGrids = findGrids();
    }

    public Grid[] findGrids() {
        ArrayList<Grid> gridList = new ArrayList<Grid>();
        for(int row = 0; row < figureForm.length; row++){

            int[] currentRow = figureForm[row];
            for(int column = 0; column < currentRow.length; column++){

                if(currentRow[column] == 1){
                    Grid grid = new Grid(x + column, y + row, field.getGridSize());
                    gridList.add(grid);
                }
            }
        }
        return gridList.toArray(Grid[]::new);
    }

    /**
     * Метод, який малює фігуру на полі.
     * @param g графічний параметр поля, у якому буде малюватися фігура.
     * @param color колір фігури.
     */
    public void drawFigure(Graphics g, Color color) {
        for(Grid grid : usedGrids){
            grid.fillGrid(g, color);
            grid.drawGrid(g, color.darker().darker());
        }
    }

    public void drawFigure(Graphics g) {
        for(Grid grid : usedGrids){
            grid.fillGrid(g, color);
            grid.drawGrid(g, color.darker().darker());
        }
    }

    public boolean checkCollisionWith(Figure figure, Sides movementVector){
        for(Grid figureGrid : usedGrids){
            for(Grid givenFigureGrid : figure.getUsedGrids()){
                if (figureGrid.checkCollisionWith(givenFigureGrid, movementVector)) return true;
            }
        }
        return false;
    }

    public boolean checkBorders(Sides movementDirection){
        int figureHeight = figureForm.length;
        int figureWidth = figureForm[0].length;
        switch (movementDirection){
            case BOTTOM -> {
                if(getY() + figureHeight >= field.getRows()){
                    return true;
                }
                return false;
            }
            case LEFT -> {
                if(getX() <= 0){
                    return true;
                }
                return false;
            }
            case RIGHT -> {
                if(getX() + figureWidth >= field.getColumns()){
                    return true;
                }
                return false;
            }
            case TOP -> {
                if(getY() <= 0){
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void move(Sides movementDirection, int offset){
        switch (movementDirection){
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
        usedGrids = findGrids();
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Grid[] getUsedGrids() {
        return usedGrids;
    }

    public void setUsedGrids(Grid[] usedGrids) {
        this.usedGrids = usedGrids;
    }

    public void setFigureForm(int[][] figureForm) {
        this.figureForm = figureForm;
    }

    public int[][] getFigureForm() {
        return figureForm;
    }
}
