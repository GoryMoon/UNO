package uno.server;

import uno.server.core.GameServer;

/**
 * The launcher for the server<br>
 * Responsible for handling all the parameters to start the server
 */
public class GameLauncher {

    //TODO parse args
    public static void main(String[] args) {
        new GameServer(55333, 4);
    }

}
