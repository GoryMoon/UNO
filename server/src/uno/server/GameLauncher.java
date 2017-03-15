package uno.server;

import uno.server.core.GameServer;

/**
 * The launcher for the server<br>
 * Responsible for handling all the parameters to start the server
 * @author Gustaf Järgren
 * @version 06-03-2017
 */
public class GameLauncher {

    /**
     * The main entry point of the server
     * @param args The arguments provided when started
     */
    public static void main(String[] args) {
        //TODO parse args
        new GameServer(55333, 4);
    }

}
