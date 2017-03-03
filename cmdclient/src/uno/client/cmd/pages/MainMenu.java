package uno.client.cmd.pages;

import uno.client.cmd.GameClient;

/**
 * The main menu page of the game
 */
public class MainMenu extends Page {

    @Override
    public void printMessage(GameClient client) {
        System.out.println("\n\n\n\n\n\n**********************************\nUNO CMD\n\n1.Join Game\n2.Exit\n\nPick choice: ");
    }

    @Override
    void handleInput(GameClient client, int in) {
        if (in == 1) {
            client.networkClient.connect();
            running = false;
        } else if (in == 2) {
            running = false;
        }
    }

}
