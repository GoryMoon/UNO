package uno.client.cmd.pages;

import uno.client.cmd.GameClient;

public class GameLobby extends Page {

    public int currentPlayers;
    public boolean host;

    @Override
    public void printMessage(GameClient client) {
        if (currentPlayers == 1) {
            host = true;
        }

        System.out.println(String.format("\n\n\n\n\n\n**********************************\nGame Lobby\n\nCurrently connected players: %d\n\n%s", currentPlayers, host ? "1.Start Game\n\nPick choice: ": "Waiting for host to start"));
    }

    @Override
    protected boolean wantInput() {
        return host;
    }

    @Override
    void handleInput(GameClient client, int in) {
        if (host && in == 1) {
            client.sendMessageToServer("start-game");
            running = false;
        }
    }
}
