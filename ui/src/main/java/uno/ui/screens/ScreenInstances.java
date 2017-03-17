package uno.ui.screens;

/**
 * Holds all the screens, makes it easy to get a screen and makes sure only one screen of each exists<br>
 * Could setup special screens if needed in a certain way after creation.
 * @author Betina Andersson &amp; Shahad Naji
 * @version 2017-03-03
 */
public class ScreenInstances {

    private static MainMenu mainMenu;
    private static GameLobby gameLobby;
    private static ErrorScreen errorScreen;
    private static PlayArea playArea;
    private static GameFinished gameFinished;

    /**
     * Returns a reference to the MainMenu screen, creates one if none exists
     * @return A reference to the MainMenu screen
     */
    public static MainMenu getMainMenu() {
        if (mainMenu == null)
            mainMenu = new MainMenu();
        return mainMenu;
    }

    /**
     * Returns a reference to the GameLobby screen, creates one if none exists
     * @return A reference to the GameLobby screen
     */
    public static GameLobby getGameLobby() {
        if (gameLobby == null)
            gameLobby = new GameLobby();
        return gameLobby;
    }

    /**
     * Returns a reference to the ErrorScreen screen, creates one if none exists
     * @return A reference to the ErrorScreen screen
     */
    public static ErrorScreen getErrorScreen() {
        if (errorScreen == null)
            errorScreen = new ErrorScreen();
        return errorScreen;
    }

    /**
     * Returns a reference to the PlayArea screen, creates one if none exists
     * @return A reference to the PlayArea screen
     */
    public static PlayArea getPlayArea() {
        if (playArea == null)
            playArea = new PlayArea();
        return playArea;
    }

    /**
     * Returns a reference to the GameFinished screen, creates one if none exists
     * @return A reference to the GameFinished screen
     */
    public static GameFinished getGameFinished() {
        if (gameFinished == null)
            gameFinished = new GameFinished();
        return gameFinished;
    }
}
