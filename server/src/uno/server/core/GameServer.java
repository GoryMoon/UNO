package uno.server.core;

import uno.logic.GameCore;
import uno.network.api.IServerMessageListener;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.api.Player;
import uno.network.server.NetworkServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameServer implements IServerMessageListener {

    private GameCore core;
    private NetworkServer networkServer;
    private Player hostPlayer;
    private int currentPlayerCount;

    private ServerInteractions interactions;

    public GameServer() {
        networkServer = new NetworkServer(this, 56333, 4);
        networkServer.start();
    }

    @Override
    public boolean onPrePlayerConnect(Socket socket) {
        if (core != null) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(new Packet(MessageType.ERROR_RUNNING, "Game already running"));
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onPlayerConnect(Player player) {
        if (hostPlayer == null)
            hostPlayer = player;
        currentPlayerCount++;
    }

    @Override
    public void onPlayerDisconnect(Player player, boolean expected) {
        if (player == hostPlayer) {
            networkServer.stop();
            networkServer.start();
            currentPlayerCount = 0;
            core = null;
        } else {
            currentPlayerCount--;
        }
    }

    @Override
    public void onMessageReceived(Player player, MessageType type, Object data) {
        if (data instanceof String) {
            if (data.equals("start-game") && core == null && currentPlayerCount >= 2) {
                core = new GameCore();
                core.setupGame(currentPlayerCount);
                sendToAllPlayersExcept(player, "game-started");
                interactions = new ServerInteractions(this, core, networkServer);
            }

            if (core != null && interactions != null) {
                interactions.handleMessage((String) data);
            }
        }
    }

    public void sendToAllPlayers(String message) {
        networkServer.sendToAllPlayers(new Packet(MessageType.MESSAGE, message));
    }

    public void sendToAllPlayersExcept(Player player, String message) {
        networkServer.sendToAllExcept(player, new Packet(MessageType.MESSAGE, message));
    }
}
