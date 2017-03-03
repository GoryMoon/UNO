package uno.client.cmd.pages;

public class PagesSingletons {

    private static MainMenu mainMenu;
    private static GameLobby gameLobby;
    private static ErrorPage errorPage;
    private static GamePage gamePage;
    private static GameStats gameStats;

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

    public static ErrorPage getErrorPage() {
        if (errorPage == null)
            errorPage = new ErrorPage();
        return errorPage;
    }

    public static GamePage getGamePage() {
        if (gamePage == null)
            gamePage = new GamePage();
        return gamePage;
    }

    public static GameStats getGameStats() {
        if (gameStats == null)
            gameStats = new GameStats();
        return gameStats;
    }
}
