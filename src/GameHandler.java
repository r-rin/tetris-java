import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

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
            {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}, {0, 1, 0}},
            {{1, 1}, {1, 0}},
            {{1, 0, 1}, {0, 1, 0}, {1, 0, 1}},

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

    public GameHandler(TetrisField tetrisField) {
        this.field = tetrisField;
        this.keyEventHandler = new KeyEventHandler(this);
        scoreCounter = tetrisField.gameWindow.sideMenu.scoreCounter;
        field.addKeyListener(keyEventHandler);
    }

    public void runGame() throws InterruptedException {
        getSettings();
        startTime = Instant.now();
        while (!gameEnded){
            if(!onPause){
                field.repaint();
            } else {
                if(action == UserAction.PAUSE){
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

    private void checkFigureBottom() {
        if(checkGridCollision(activeFigure, Sides.BOTTOM) || activeFigure.checkBorders(Sides.BOTTOM)){
            gridsOnField.addAll(List.of(activeFigure.getUsedGrids()));
            activeFigure = null;
        }
    }

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
                scoreCounter.addPoints(gridsMap[currentRow].length*50);
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
            if(grid.getY() > -1 && grid.getX() > -1){
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

        int red = (rand.nextInt(256) + 128) % 256;
        int green = (rand.nextInt(256) + 128) % 256;
        int blue = (rand.nextInt(256) + 128) % 256;

        Color randomColor = new Color(red, green, blue);
        randomColor = randomColor.brighter();

        Collections.shuffle(arrayToShuffle);

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

    public void setFramesPerFall(int framesPerFall) {
        this.framesPerFall = framesPerFall;
    }

    public void setTimeBetweenFrames(int timeBetweenFrames) {
        this.timeBetweenFrames = timeBetweenFrames;
    }

    public void setLimitedVision(boolean limitedVision) {
        this.limitedVision = limitedVision;
    }

    public TetrisField getField() {
        return field;
    }
}
