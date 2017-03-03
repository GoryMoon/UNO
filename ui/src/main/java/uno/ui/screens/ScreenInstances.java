package uno.ui.screens;

/**
 * Created by Kungen on 2017-03-03.
 */
public class ScreenInstances {

    private static MainMenu mainMenu;
    private static GameLobby gameLobby;
    private static ErrorScreen errorScreen;
    private static PlayArea playArea;
    private static GameFinished gameFinished;

    public static MainMenu getMainMenu() {
        if (mainMenu == null)
            mainMenu = new MainMenu();
        return mainMenu;
    }

    public static GameLobby getGameLobby() {
        if (gameLobby == null)
            gameLobby = new GameLobby();
        return gameLobby;
    }

    public static ErrorScreen getErrorScreen() {
        if (errorScreen == null)
            errorScreen = new ErrorScreen();
        return errorScreen;
    }

    public static PlayArea getPlayArea() {
        if (playArea == null)
            playArea = new PlayArea();
        return playArea;
    }

    public static GameFinished getGameFinished() {
        if (gameFinished == null)
            gameFinished = new GameFinished();
        return gameFinished;
    }
}
