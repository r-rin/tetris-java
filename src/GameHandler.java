import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameHandler {

    boolean canMoveUp = true;

    private int[][][] possibleForms = {{{1, 1, 0}, {0, 1, 1}},
            {{1, 0}, {1, 0}, {1, 1}},
            {{1, 1}, {1, 1}},
            {{1, 1, 1, 1}},
            {{0, 1, 0}, {1, 1, 1}}};
    private ArrayList<Figure> figures = new ArrayList<>();
    private Figure activeFigure = null;
    private Random rand = new Random();

    private int framesPerFall = 50;
    private double timeBetweenFrames = 0.01;
    private int currentFrame = 0;

    private UserAction action = null;
    private Sides actionDirection = null;

    private TetrisField field;
    private KeyEventHandler keyEventHandler;

    boolean gameEnded = false;

    public GameHandler(TetrisField tetrisField) {
        this.field = tetrisField;
        this.keyEventHandler = new KeyEventHandler(this);
        field.addKeyListener(keyEventHandler);
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

        if (!gameEnded){
            drawActiveFigure(g);
        }
    }

    private void drawActiveFigure(Graphics g) {

        if(activeFigure == null){
            activeFigure = createNewFigure();
            activeFigure.drawFigure(g);
            if(checkCollisionWithAllFigures(activeFigure, Sides.CURRENT)){
                gameEnded = true;
                figures.add(activeFigure);
                activeFigure = null;
            }
        }

        doUserAction();

        checkBottomCollision(g);

        if(activeFigure != null){
            doActiveFigureFall();
            activeFigure.drawFigure(g);
        }
    }

    private void doActiveFigureFall() {
        if(currentFrame == framesPerFall){
            if(!checkCollisionWithAllFigures(activeFigure, Sides.BOTTOM)){
                activeFigure.move(Sides.BOTTOM, 1);
                currentFrame = 0;
            }
        } else {
            currentFrame += 1;
        }
    }

    private Figure createNewFigure() {
        int[][] selectedForm = possibleForms[rand.nextInt(0, possibleForms.length)];
        int xPos = field.getColumns()/2 - selectedForm[0].length/2;
        int yPos = -(selectedForm.length)+1;
        Color randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

        return new Figure(selectedForm, xPos, yPos, field, randomColor);
    }

    private void checkBottomCollision(Graphics g) {
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
                    activeFigure.setFigureForm(activeFigure.rotateFigure(actionDirection));
                    if(checkCollisionWithAllFigures(activeFigure, Sides.CURRENT) || activeFigure.checkBorders(Sides.CURRENT)){
                        activeFigure.setFigureForm(previousForm);
                    } else {
                        activeFigure.setUsedGrids(activeFigure.findGrids());
                    }
                } else if (actionDirection == Sides.RIGHT) {
                    activeFigure.setFigureForm(activeFigure.rotateFigure(actionDirection));
                    if(checkCollisionWithAllFigures(activeFigure, Sides.CURRENT) || activeFigure.checkBorders(Sides.CURRENT)){
                        activeFigure.setFigureForm(previousForm);
                    } else {
                        activeFigure.setUsedGrids(activeFigure.findGrids());
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

    public UserAction getAction() {
        return action;
    }

    public Sides getActionDirection() {
        return actionDirection;
    }

    public void setActionDirection(Sides actionDirection) {
        this.actionDirection = actionDirection;
    }

    public void setAction(UserAction action) {
        this.action = action;
    }

    public boolean isActiveFigureNull() {
        return activeFigure == null;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }
}
