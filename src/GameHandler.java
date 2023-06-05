import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameHandler {

    int[][][] possibleForms = {{{1, 1, 0}, {0, 1, 1}},
            {{1, 0}, {1, 0}, {1, 1}},
            {{1, 1}, {1, 1}},
            {{1, 1, 1, 1}},
            {{0, 1, 0}, {1, 1, 1}}};
    ArrayList<Figure> figures = new ArrayList<>();
    Figure activeFigure = null;
    Random rand = new Random();

    int framesPerFall = 2;
    double timeBetweenFrames = 0.7;
    int currentFrame = 0;

    UserAction action = null;
    Sides actionDirection = null;

    TetrisField field;

    boolean gameEnded = false;

    public GameHandler(TetrisField tetrisField) {
        this.field = tetrisField;
    }

    public void run() throws InterruptedException {
        while (!gameEnded){
            field.repaint();
            Thread.sleep((long) (timeBetweenFrames*1000));
        }
    }

    public void drawFrame(Graphics g) {

        for(Figure figure : figures){
            figure.drawFigure(g);
        }

        if(activeFigure == null){
            int[][] selectedForm = possibleForms[rand.nextInt(0, possibleForms.length)];
            int xPos = field.getColumns()/2 - selectedForm[0].length/2;
            int yPos = -(selectedForm.length)+1;
            Color randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

            activeFigure = new Figure(selectedForm, xPos, yPos, field, randomColor);
            if(checkCollisionWithAllFigures(activeFigure, Sides.CURRENT)) gameEnded = true;
            activeFigure.drawFigure(g);
        } else {
            checkBottom(g);
            if(activeFigure != null){
                doUserAction();
                if(currentFrame == framesPerFall){
                    if(!checkCollisionWithAllFigures(activeFigure, Sides.BOTTOM)){
                        activeFigure.move(Sides.BOTTOM, 1);
                    }
                } else {
                    currentFrame += 1;
                }
                activeFigure.drawFigure(g);
            }
        }
    }

    private void checkBottom(Graphics g) {
        if(checkCollisionWithAllFigures(activeFigure, Sides.BOTTOM) || activeFigure.checkBorders(Sides.BOTTOM)){
            figures.add(activeFigure);
            activeFigure.drawFigure(g);
            activeFigure = null;
        }
    }

    private void doUserAction() {
        if(action != null) {
            if(action == UserAction.ROTATE){

                int[][] previousForm = activeFigure.getFigureForm();

                if(actionDirection == Sides.LEFT){
                    //Обертання проти годинникової стрілки
                    if(checkCollisionWithAllFigures(activeFigure, Sides.CURRENT)){
                        activeFigure.setFigureForm(previousForm);
                    }
                } else if (actionDirection == Sides.RIGHT) {
                    //Обертання за годинниковою стрілкою
                    if(checkCollisionWithAllFigures(activeFigure, Sides.CURRENT)){
                        activeFigure.setFigureForm(previousForm);
                    }
                }
            } else if (action == UserAction.MOVE) {
                if(!checkCollisionWithAllFigures(activeFigure, actionDirection) && !activeFigure.checkBorders(actionDirection)){
                    activeFigure.move(actionDirection, 1);
                    activeFigure.setUsedGrids(activeFigure.findGrids());
                }
            }
        }
        action = null;
        actionDirection = null;
    }

    private boolean checkCollisionWithAllFigures(Figure activeFigure, Sides direction) {
        for(Figure figure : figures){
            if(activeFigure.checkCollisionWith(figure, direction)) return true;
        }
        return false;
    }
}
