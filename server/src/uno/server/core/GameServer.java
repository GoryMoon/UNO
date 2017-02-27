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
import java.util.UUID;

/**
 * The core class for the server, starts and handles everything
 */
public class GameServer implements IServerMessageListener {

    private GameCore core;
    private NetworkServer networkServer;
    private Player hostPlayer;
    private int currentPlayerCount;
    private ServerInteractions interactions;

    /**
     * Setups the server depending on the parameters put in
     */
    public GameServer(int port, int maxPlayers) {
        networkServer = new NetworkServer(this, port, maxPlayers);
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
            if (core != null && interactions != null) {
                interactions.handleMessage((String) data);
            }

            if (data.equals("start-game") && core == null && currentPlayerCount >= 2) {
                core = new GameCore(this);
                core.setupGame(currentPlayerCount, networkServer.getPlayerUUIDs());
                sendToAllPlayersExcept(player, "game-started");
                interactions = new ServerInteractions(this, core, networkServer);
            }
        } else {
            if (core != null && interactions != null) {
                interactions.handleMessage(data);
            }
        }
    }

    /**
     * @return The interactions object used by this server
     */
    public ServerInteractions getInteractions() {
        return interactions;
    }

    /**
     * Ease of use method for sending a message to all players
     * @param message The message to be sent
     */
    public void sendToAllPlayers(String message) {
        networkServer.sendToAllPlayers(new Packet(MessageType.MESSAGE, message));
    }

    /**
     * Ease of use method for sending a message to all players except the one provided
     * @param player The player no to send to
     * @param message The message to be sent
     */
    public void sendToAllPlayersExcept(Player player, String message) {
        networkServer.sendToAllExcept(player, new Packet(MessageType.MESSAGE, message));
    }

    /**
     * Used to request an input from the player
     * @param uuid The UUID of the player that the request is for
     * @param requestType String of what's requested, the message is an collaboration between the server and client
     */
    public void requestInputFromPlayer(UUID uuid, String requestType) {
        networkServer.sendToPlayer(networkServer.getPlayerFromUUID(uuid), new Packet(MessageType.MESSAGE, "request-" + requestType));
    }
}
