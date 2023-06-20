import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventHandler implements KeyListener {

    GameHandler gameHandler;

    public KeyEventHandler(GameHandler gameHandler){
        this.gameHandler = gameHandler;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

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

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
