/* File: KeyEventHandler.java
 * Authors: Rafikov Rinat
 * Class, that handles user input.
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventHandler implements KeyListener {

    GameHandler gameHandler;

    /**
     * This class handles the key events for the game. It listens for key presses and performs corresponding actions
     * based on the key pressed. It interacts with the provided GameHandler instance to update the game state.
     *
     * @param gameHandler gameHandler object.
     */
    public KeyEventHandler(GameHandler gameHandler){
        this.gameHandler = gameHandler;
    }

    /**
     * Invoked when a key is typed. This method is not used in the current implementation.
     *
     * @param e The KeyEvent object representing the key typed event.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Invoked when a key is pressed. This method checks the pressed key and performs corresponding actions in the game.
     * It updates the action and action direction of the GameHandler based on the key pressed.
     *
     * @param e The KeyEvent object representing the key pressed event.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(!gameHandler.gameEnded && !gameHandler.isActiveFigureNull()){
            if(gameHandler.getAction() == null && gameHandler.getActionDirection() == null){
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case (37), (65) -> {
                        gameHandler.setAction(UserAction.MOVE);
                        gameHandler.setActionDirection(Sides.LEFT);
                    }
                    case (39), (68) -> {
                        gameHandler.setAction(UserAction.MOVE);
                        gameHandler.setActionDirection(Sides.RIGHT);
                    }
                    case (40), (83) -> {
                        gameHandler.setAction(UserAction.MOVE);
                        gameHandler.setActionDirection(Sides.BOTTOM);
                    }
                    case (38), (87) -> {
                        if (gameHandler.canMoveUp || gameHandler.isControlsShuffle) {
                            gameHandler.setAction(UserAction.MOVE);
                            gameHandler.setActionDirection(Sides.TOP);
                        }
                    }
                    case (81) -> {
                        gameHandler.setAction(UserAction.ROTATE);
                        gameHandler.setActionDirection(Sides.LEFT);
                    }
                    case (69) -> {
                        gameHandler.setAction(UserAction.ROTATE);
                        gameHandler.setActionDirection(Sides.RIGHT);
                    }
                    case (80) -> {
                        gameHandler.setAction(UserAction.PAUSE);
                    }
                }
            }
        }
    }

    /**
     * Invoked when a key is released. This method is not used in the current implementation.
     *
     * @param e The KeyEvent object representing the key released event.
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
