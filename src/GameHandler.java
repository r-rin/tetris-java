/* File: GameHandler.java
 * Authors: Rafikov Rinat
 * Class, that handles game logic.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

/**
 * The GameHandler class manages the game state and logic.
 */
public class GameHandler {

    boolean disableAnyUpMoves = true;
    boolean canMoveUp = true;
    boolean limitedVision = false;
    double visibleGridDistance = 3.0;

    boolean isControlsShuffle = false;
    boolean doRandomMove = false;
    int doRandomMoveEachNFrame = 30;
    int randMoveCounter = 0;

    List<Sides> arrayToShuffle = Arrays.asList(Sides.TOP, Sides.RIGHT, Sides.BOTTOM, Sides.LEFT);

    Instant startTime;
    Instant endTime;

    long totalPlaytime = 0;

    Instant pauseStartTime;
    Instant pauseEndTime;

    long secondsOnPause = 0;

    /**
     * Array containing the possible forms of the tetris figure.
     */
    private int[][][] possibleForms = {
            {{1, 1, 0}, {0, 1, 1}},
            {{1, 0}, {1, 0}, {1, 1}},
            {{1, 1}, {1, 1}},
            {{1, 1, 1, 1}},
            {{0, 1, 1}, {1, 1, 0}},
            {{1, 1}},
            {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}},
            {{1}},
            {{0, 1}, {1, 0}},
            {{0, 1}, {0, 1}, {1, 1}},
            {{0, 1, 0}, {1, 1, 1}},
            {{1, 1}, {0, 1}},
            {{1, 1}, {1, 0}},
    };

    private ArrayList<Grid> gridsOnField = new ArrayList<>();
    private Figure activeFigure = null;
    private final Random rand = new Random();

    private int framesPerFall = 50;
    private int timeBetweenFrames = 100;
    private int currentFrame = 0;

    private UserAction action = null;
    private Sides actionDirection = null;

    private TetrisField field;
    private ScoreCounter scoreCounter;
    private KeyEventHandler keyEventHandler;

    boolean gameEnded = true;

    private final Random random = new Random();

    private boolean onPause = false;

    /**
     * Constructs a GameHandler object.
     *
     * @param tetrisField The TetrisField object associated with the game.
     */
    public GameHandler(TetrisField tetrisField) {
        this.field = tetrisField;
        this.keyEventHandler = new KeyEventHandler(this);
        scoreCounter = tetrisField.gameWindow.sideMenu.scoreCounter;
        field.addKeyListener(keyEventHandler);
    }

    /**
     * Runs the game loop.
     *
     * @throws InterruptedException if the thread is interrupted.
     */
    public void runGame() throws InterruptedException {
        getSettings();
        startTime = Instant.now();
        while (!gameEnded) {
            if (!onPause) {
                field.repaint();
            } else {
                if (action == UserAction.PAUSE) {
                    onPause = false;
                    pauseEndTime = Instant.now();
                    long timeElapsed = Duration.between(pauseStartTime, pauseEndTime).toSeconds();
                    secondsOnPause += timeElapsed;
                }
                action = null;
            }
            Thread.sleep(timeBetweenFrames);
        }
        endTime = Instant.now();
        totalPlaytime = Duration.between(startTime, endTime).toSeconds() - secondsOnPause;
        field.gameWindow.leaderboard.addNewRoundResult(new RoundStats(field.gameWindow.leaderboard.currentName, scoreCounter.score, scoreCounter.multiplier, totalPlaytime, new HashMap<>(field.gameWindow.settings)));
    }

    /**
     * Retrieves the game settings from the TetrisField.
     */
    private void getSettings() {
        canMoveUp = (boolean) field.gameWindow.settings.get(GameSettings.CANMOVEUP);
        field.setGridVisible((boolean) field.gameWindow.settings.get(GameSettings.DRAWGRIDNET));
        limitedVision = (boolean) field.gameWindow.settings.get(GameSettings.LIMITEDVISION);
        visibleGridDistance = (double) field.gameWindow.settings.get(GameSettings.VISIONDISTANCE);
        isControlsShuffle = (boolean) field.gameWindow.settings.get(GameSettings.CONTROLSSHUFFLE);
        doRandomMove = (boolean) field.gameWindow.settings.get(GameSettings.RANDOMMOVES);
        doRandomMoveEachNFrame = (int) field.gameWindow.settings.get(GameSettings.RANDOMMOVEEACHNFRAME);
        disableAnyUpMoves = (boolean) field.gameWindow.settings.get(GameSettings.DISABLEANYUPMOVES);
    }

    /**
     * Draws the current frame of the game.
     *
     * @param g The graphics context.
     */
    public void drawFrame(Graphics g) {

        for (Grid grid : gridsOnField) {
            grid.fillGrid(g);
            grid.drawGrid(g);
        }

        if (!gameEnded) {
            drawCurrentFrame(g);
        }
    }

    /**
     * Draws the current frame of the game on the specified graphics context.
     * @param g The graphics context on which to draw the frame.
     */
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
            doUserAction(g);
            if(onPause) return;
            doFigureFall();
            if(doRandomMove) doRandomMove();
            activeFigure.drawFigure(g);
        }

        if(limitedVision){
            drawInvertedCircle((Graphics2D) g);
        }
        randMoveCounter += 1;
        currentFrame += 1;
        if(!gameEnded){
            scoreCounter.addPoints(1);
        }
    }

    /**
     * Performs a random move on the active figure, if the random move counter reaches the specified threshold.
     * The random move can be in any allowed direction: right, bottom, left, and optionally top.
     */
    private void doRandomMove() {
        if(randMoveCounter >= doRandomMoveEachNFrame){
            List<Sides> allowedMoves = new ArrayList<>();
            allowedMoves.add(Sides.RIGHT);
            allowedMoves.add(Sides.BOTTOM);
            allowedMoves.add(Sides.LEFT);
            if(!disableAnyUpMoves) allowedMoves.add(Sides.TOP);
            Sides randomSide = allowedMoves.get(random.nextInt(allowedMoves.size()));

            if(!checkGridCollision(activeFigure, randomSide) && !activeFigure.checkBorders(randomSide)){
                activeFigure.move(randomSide, 1);
                randMoveCounter = 0;
            }
        }
    }

    /**
     * Draws an inverted circle on the specified Graphics2D context.
     * The inverted circle is created by subtracting an inner circular shape from an outer rectangular shape.
     * The position and size of the inner circle are determined based on the active figure and the specified parameters.
     *
     * @param g The Graphics2D context on which to draw the inverted circle.
     */
    private void drawInvertedCircle(Graphics2D g) {
        Shape outerRectangle = new Rectangle(0, 0, field.getWidth(), field.getHeight());
        Area outerArea = new Area(outerRectangle);

        int circleX = 0;
        int circleY = 0;
        double circleRad = 0;
        if(activeFigure != null){
            int gridSize = field.getGridSize();
            circleRad = visibleGridDistance * 2 * gridSize;
            circleX = (int)((activeFigure.getFigureForm()[0].length / 2.0)*gridSize) + activeFigure.getX()*gridSize - (int) (circleRad/2);
            circleY = (int)((activeFigure.getFigureForm().length / 2.0)*gridSize) + activeFigure.getY()*gridSize - (int) (circleRad/2);
        }
        Shape innerCircle = new Ellipse2D.Double(circleX, circleY, circleRad, circleRad);
        Area circleArea = new Area(innerCircle);
        outerArea.subtract(circleArea);

        Graphics2D g2d = g;

        g2d.setColor(Color.BLACK);
        g2d.fill(outerArea);
    }

    /**
     * Checks if the active figure has reached the bottom of the game field.
     * If the active figure collides with the grid or reaches the bottom border, it is considered to have reached the bottom.
     * The used grids of the active figure are added to the grids on the field.
     */
    private void checkFigureBottom() {
        if(checkGridCollision(activeFigure, Sides.BOTTOM) || activeFigure.checkBorders(Sides.BOTTOM)){
            gridsOnField.addAll(List.of(activeFigure.getUsedGrids()));
            activeFigure = null;
        }
    }

    /**
     * Performs the user action based on the current action and action direction.
     * The supported user actions include:
     * - ROTATE: Rotates the active figure in the specified action direction.
     * - MOVE: Moves the active figure in the specified action direction.
     * - PAUSE: Pauses the game and displays a "PAUSE" message on the graphics context if limited vision is enabled.
     *
     * @param g The graphics context on which to perform the user action.
     */
    private void doUserAction(Graphics g) {
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
                if(isControlsShuffle) actionDirection = arrayToShuffle.get(actionDirection.getValue());
                if(!checkGridCollision(activeFigure, actionDirection) && !activeFigure.checkBorders(actionDirection)){
                    if(!(disableAnyUpMoves && actionDirection == Sides.TOP)){
                        activeFigure.move(actionDirection, 1);
                    }
                    if(actionDirection == Sides.BOTTOM){
                        scoreCounter.addPoints(5);
                    }
                }
            } else if (action == UserAction.PAUSE) {
                onPause = true;
                pauseStartTime = Instant.now();
                if(limitedVision){
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, field.getWidth(), field.getHeight());
                    FontMetrics metrics = g.getFontMetrics();
                    int x = (field.getWidth() - metrics.stringWidth("PAUSE")) / 2;
                    int y = ((field.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                    g.setColor(Color.WHITE);
                    g.drawString("PAUSE", x, y);
                }
            }
        }
        action = null;
        actionDirection = null;
    }

    /**
     * Checks for collision between the active figure and the grids on the field in a specific direction.
     *
     * @param activeFigure    the active figure to check collision with
     * @param actionDirection the direction in which the collision is checked (e.g., left, right, up, down)
     * @return {@code true} if there is a collision between the active figure and any of the grids on the field,
     *         {@code false} otherwise
     */
    private boolean checkGridCollision(Figure activeFigure, Sides actionDirection) {
        for(Grid grid : gridsOnField){
            if(activeFigure.checkCollisionWith(grid, actionDirection)) return true;
        }
        return false;
    }

    /**
     * Checks for filled rows in the grid and clears if there's any.
     *
     * @return true if one or more rows were cleared, false otherwise.
     */
    private boolean checkFilledRows() {
        Grid[][] gridsMap = buildGridMap();
        boolean isCleared = false;

        for(int currentRow = 0; currentRow < gridsMap.length; currentRow++){
            if(Arrays.stream(gridsMap[currentRow]).noneMatch(Objects::isNull)){
                removeAllGrids(currentRow);
                scoreCounter.addPoints(gridsMap[currentRow].length*50);
                moveGridsDown(gridsMap, currentRow);
                currentRow = 0;
                gridsMap = buildGridMap();
                isCleared = true;
            }
        }

        return isCleared;
    }

    /**
     * Builds a grid map based on the current positions of grids on the field.
     *
     * @return a 2D array representing the grid map.
     */
    private Grid[][] buildGridMap() {
        Grid[][] gridsMap = new Grid[field.getRows()][field.getColumns()];

        for(Grid grid : gridsOnField){
            if(grid.getY() > -1 && grid.getX() > -1){
                gridsMap[grid.getY()][grid.getX()] = grid;
            }
        }
        return gridsMap;
    }

    /**
     * Removes all grids from the specified row on the field.
     *
     * @param currentRow the row from which grids should be removed.
     */
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

    /**
     * Moves grids in the grid map down from the specified row to the bottom.
     *
     * @param gridsMap the 2D array representing the grid map.
     * @param toRow    the row until which the grids should be moved down.
     */
    private void moveGridsDown(Grid[][] gridsMap, int toRow) {
        for(int currentRow = 0; currentRow < toRow; currentRow++){
            for(Grid selectedGrid : gridsMap[currentRow]){
                if(selectedGrid != null){
                    selectedGrid.moveGrid(Sides.BOTTOM, 1);
                }
            }
        }
    }

    /**
     * Performs the falling action for the active figure if conditions are met.
     * The figure will move down by one step if there is no grid collision
     * and the figure has not reached the bottom border.
     */
    private void doFigureFall() {
        if(currentFrame >= framesPerFall){
            if(!checkGridCollision(activeFigure, Sides.BOTTOM) && !activeFigure.checkBorders(Sides.BOTTOM)){
                activeFigure.move(Sides.BOTTOM, 1);
                currentFrame = 0;
            }
        }
    }

    /**
     * Creates a new figure with a randomly selected form, position, and color.
     *
     * @return the newly created Figure object.
     */
    private Figure createNewFigure() {
        int[][] selectedForm = possibleForms[rand.nextInt(0, possibleForms.length)];
        int xPos = field.getColumns()/2 - selectedForm[0].length/2;
        int yPos = -(selectedForm.length)+1;

        int red = (rand.nextInt(256) + 128) % 256;
        int green = (rand.nextInt(256) + 128) % 256;
        int blue = (rand.nextInt(256) + 128) % 256;

        Color randomColor = new Color(red, green, blue);
        randomColor = randomColor.brighter();

        Collections.shuffle(arrayToShuffle);

        return new Figure(selectedForm, xPos, yPos, field, randomColor);
    }

    /**
     * Retrieves the user action associated with this instance.
     *
     * @return the UserAction object representing the user's action.
     */
    public UserAction getAction() {
        return action;
    }

    /**
     * Retrieves the direction of the user action.
     *
     * @return the Sides enum representing the direction of the action.
     */
    public Sides getActionDirection() {
        return actionDirection;
    }

    /**
     * Sets the direction of the user action.
     *
     * @param actionDirection the Sides enum representing the direction of the action.
     */
    public void setActionDirection(Sides actionDirection) {
        this.actionDirection = actionDirection;
    }

    /**
     * Sets the user action.
     *
     * @param action the UserAction object representing the user's action.
     */
    public void setAction(UserAction action) {
        this.action = action;
    }

    /**
     * Checks if the active figure is null.
     *
     * @return true if the active figure is null, false otherwise.
     */
    public boolean isActiveFigureNull() {
        return activeFigure == null;
    }

    /**
     * Restarts the game.
     *
     * @param button the JButton object associated with the restart action.
     * @throws InterruptedException if the execution is interrupted.
     */
    public void restart(JButton button) throws InterruptedException {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if(gameEnded){
                    button.setEnabled(false);
                    gameEnded = false;
                    currentFrame = 0;
                    randMoveCounter = 0;
                    gridsOnField = new ArrayList<>();
                    activeFigure = null;
                    field.requestFocus();
                    field.requestFocusInWindow();
                    scoreCounter.setScore(0);
                    runGame();
                }
                return null;
            }

            @Override
            protected void done() {
                super.done();
                button.setEnabled(true);
            }
        };

        worker.execute();
    }

    /**
     * Sets the number of frames per fall for the game.
     *
     * @param framesPerFall the number of frames per fall to be set.
     */
    public void setFramesPerFall(int framesPerFall) {
        this.framesPerFall = framesPerFall;
    }

    /**
     * Sets the time between frames for the game.
     *
     * @param timeBetweenFrames the time between frames to be set.
     */
    public void setTimeBetweenFrames(int timeBetweenFrames) {
        this.timeBetweenFrames = timeBetweenFrames;
    }

    /**
     * Sets the limited vision option for the game.
     *
     * @param limitedVision true to enable limited vision, false otherwise.
     */
    public void setLimitedVision(boolean limitedVision) {
        this.limitedVision = limitedVision;
    }

    /**
     * Retrieves the TetrisField object associated with this instance.
     *
     * @return the TetrisField object.
     */
    public TetrisField getField() {
        return field;
    }
}
