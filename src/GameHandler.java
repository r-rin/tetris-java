import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameHandler {

    boolean canMoveUp = true;

    private int[][][] possibleForms = {{{1, 1, 0}, {0, 1, 1}},
            {{1, 0}, {1, 0}, {1, 1}},
            {{1, 1}, {1, 1}},
            {{1, 1, 1, 1}},
            {{0, 1, 0}, {1, 1, 1}}};
    private ArrayList<Grid> gridsOnField = new ArrayList<>();
    private Figure activeFigure = null;
    private Random rand = new Random();

    private int framesPerFall = 50;
    private double timeBetweenFrames = 0.1;
    private int currentFrame = 0;

    private UserAction action = null;
    private Sides actionDirection = null;

    private TetrisField field;
    private KeyEventHandler keyEventHandler;

    boolean gameEnded = true;

    public GameHandler(TetrisField tetrisField) {
        this.field = tetrisField;
        this.keyEventHandler = new KeyEventHandler(this);
        field.addKeyListener(keyEventHandler);
    }

    public void run() throws InterruptedException {
        gridsOnField = new ArrayList<>();
        activeFigure = null;
        gameEnded = false;
        while (!gameEnded){
            field.repaint();
            Thread.sleep((long) (timeBetweenFrames*1000));
        }
    }

    public void drawFrame(Graphics g) {

        for(Grid grid : gridsOnField){
            grid.fillGrid(g);
            grid.drawGrid(g);
        }

        if (!gameEnded){
            drawCurrentFrame(g);
        }
    }

    private void drawCurrentFrame(Graphics g) {
        if(activeFigure == null){
            if(checkFilledRows()) return;
            activeFigure = createNewFigure();
            activeFigure.drawFigure(g);
            if(checkGridCollision(activeFigure, Sides.CURRENT)){
                gridsOnField.addAll(List.of(activeFigure.findGrids()));
                gameEnded = true;
                activeFigure = null;
                return;
            }
        }
        checkFigureBottom();
        if(activeFigure != null){
            doUserAction();
            doFigureFall();
            activeFigure.drawFigure(g);
        }
        currentFrame += 1;
    }

    private void checkFigureBottom() {
        if(checkGridCollision(activeFigure, Sides.BOTTOM) || activeFigure.checkBorders(Sides.BOTTOM)){
            gridsOnField.addAll(List.of(activeFigure.getUsedGrids()));
            activeFigure = null;
        }
    }

    private void doUserAction() {
        if(action != null) {
            if(action == UserAction.ROTATE){
                int[][] previousForm = activeFigure.getFigureForm();

                activeFigure.setFigureForm(activeFigure.rotateFigure(actionDirection));
                activeFigure.setUsedGrids(activeFigure.findGrids());
                if(checkGridCollision(activeFigure, Sides.CURRENT) || activeFigure.checkBorders(Sides.CURRENT)){
                    activeFigure.setFigureForm(previousForm);
                    activeFigure.setUsedGrids(activeFigure.findGrids());
                }
            } else if (action == UserAction.MOVE) {
                if(!checkGridCollision(activeFigure, actionDirection) && !activeFigure.checkBorders(actionDirection)){
                    activeFigure.move(actionDirection, 1);
                }
            }
        }
        action = null;
        actionDirection = null;
    }

    private boolean checkGridCollision(Figure activeFigure, Sides actionDirection) {
        for(Grid grid : gridsOnField){
            if(activeFigure.checkCollisionWith(grid, actionDirection)) return true;
        }
        return false;
    }

    private boolean checkFilledRows() {
        Grid[][] gridsMap = buildGridMap();
        boolean isCleared = false;

        for(int currentRow = 0; currentRow < gridsMap.length; currentRow++){
            if(Arrays.stream(gridsMap[currentRow]).noneMatch(Objects::isNull)){
                removeAllGrids(currentRow);
                moveGridsDown(gridsMap, currentRow);
                currentRow = 0;
                gridsMap = buildGridMap();
                isCleared = true;
            }
        }

        return isCleared;
    }

    private Grid[][] buildGridMap() {
        Grid[][] gridsMap = new Grid[field.getRows()][field.getColumns()];

        for(Grid grid : gridsOnField){
            if(grid.getY() > -1){
                gridsMap[grid.getY()][grid.getX()] = grid;
            }
        }
        return gridsMap;
    }

    private void removeAllGrids(int currentRow) {
        ArrayList<Grid> removeList = new ArrayList<>();

        for(Grid grid : gridsOnField){
            if(grid.getY() == currentRow){
                removeList.add(grid);
            }
        }

        for(Grid grid : removeList){
            gridsOnField.remove(grid);
        }
    }

    private void moveGridsDown(Grid[][] gridsMap, int toRow) {
        for(int currentRow = 0; currentRow < toRow; currentRow++){
            for(Grid selectedGrid : gridsMap[currentRow]){
                if(selectedGrid != null){
                    selectedGrid.moveGrid(Sides.BOTTOM, 1);
                }
            }
        }
    }

    private void doFigureFall() {
        if(currentFrame >= framesPerFall){
            if(!checkGridCollision(activeFigure, Sides.BOTTOM) && !activeFigure.checkBorders(Sides.BOTTOM)){
                activeFigure.move(Sides.BOTTOM, 1);
                currentFrame = 0;
            }
        }
    }

    private Figure createNewFigure() {
        int[][] selectedForm = possibleForms[rand.nextInt(0, possibleForms.length)];
        int xPos = field.getColumns()/2 - selectedForm[0].length/2;
        int yPos = -(selectedForm.length)+1;
        Color randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

        return new Figure(selectedForm, xPos, yPos, field, randomColor);
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

    public void restart() throws InterruptedException {
        run();
    }
}
