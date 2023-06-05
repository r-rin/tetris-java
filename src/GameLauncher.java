class GameLauncher {
    public static void main(String[] args) throws InterruptedException {
        GameWindow gameWindow = new GameWindow();
        gameWindow.tetrisField.gameHandler.run();
    }
}