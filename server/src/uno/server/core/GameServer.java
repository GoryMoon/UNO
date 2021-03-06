package uno.server.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * @author Gustaf Järgren
 * @version 06-03-2017
 */
public class GameServer implements IServerMessageListener {

    private GameCore core;
    private NetworkServer networkServer;
    private Player hostPlayer;
    private int currentPlayerCount;
    private ServerInteractions interactions;
    private int port;
    private int maxPlayers;

    public static Logger logger = LogManager.getLogger("UnoServer");

    /**
     * Starts server with default settings<br>
     * Port: 55333<br>
     * MaxPlayers: 4
     */
    public GameServer() {
        this(55333, 4);
    }

    /**
     * Setups the server depending on the parameters put in
     * @param port The port to listen to
     * @param maxPlayers The max amount players
     */
    public GameServer(int port, int maxPlayers) {
        this.port = port;
        this.maxPlayers = maxPlayers;
        logger.info("Starting Uno Server");

        startNetwork();
    }

    /**
     * Initiates and starts the network thread
     */
    private void startNetwork() {
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
                logger.error(e, e);
                e.printStackTrace();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onPlayerConnect(Player player) {
        logger.info("Player connected: " + player.getAddress() + ": " + player.getID() + " #" + ++currentPlayerCount);
        if (hostPlayer == null) {
            logger.info("Started new game lobby!");
            hostPlayer = player;
        }
        sendToAllPlayers("currentplayer#" + currentPlayerCount);
    }

    @Override
    public void onPlayerDisconnect(Player player, boolean expected) {
        logger.info("Player disconnected: " + player.getAddress() + ": " + player.getID() + " #" + --currentPlayerCount);
        if (player == hostPlayer || currentPlayerCount < 2) {
            hostPlayer = null;
            logger.info("Stopping game!");
            networkServer.reset();
            currentPlayerCount = 0;
            core = null;
        }
    }

    @Override
    public void onMessageReceived(Player player, MessageType type, Object data) {
        if (data instanceof String) {
            if (core != null && interactions != null) {
                interactions.handleMessage(player, (String) data);
            }

            if (data.equals("start-game") && core == null && currentPlayerCount >= 2) {
                core = new GameCore(this);
                core.setupGame(currentPlayerCount, networkServer.getPlayerUUIDs(), true);
                sendToAllPlayers("game-started");
                interactions = new ServerInteractions(this, core, networkServer);
                logger.info("Game Started");
            }
        }
    }

    /**
     * Gets the interactions instance for the current game
     * @return The interactions object used by this server
     */
    public ServerInteractions getInteractions() {
        return interactions;
    }

    /**
     * Sends a message to a specific player
     * @param uuid The uuid of the player to send to
     * @param message The message to send
     */
    public void sendToPlayer(UUID uuid, Object message) {
        networkServer.sendToPlayer(networkServer.getPlayerFromUUID(uuid), new Packet(MessageType.MESSAGE, message));
    }

    /**
     * Ease of use method for sending a message to all players
     * @param message The message to be sent
     */
    public void sendToAllPlayers(Object message) {
        networkServer.sendToAllPlayers(new Packet(MessageType.MESSAGE, message));
    }

    /**
     * Ease of use method for sending a message to all players except the one provided
     * @param uuid The uuid of the player no to send to
     * @param message The message to be sent
     */
    public void sendToAllPlayersExcept(UUID uuid, Object message) {
        networkServer.sendToAllExcept(networkServer.getPlayerFromUUID(uuid), new Packet(MessageType.MESSAGE, message));
    }

    /**
     * Used to request an input from the player
     * @param uuid The UUID of the player that the request is for
     * @param requestType String of what's requested, the message is an collaboration between the server and client
     */
    public void requestInputFromPlayer(UUID uuid, String requestType) {
        logger.info("Requesting input from " + uuid.toString() + " with type: " + requestType);
        networkServer.sendToPlayer(networkServer.getPlayerFromUUID(uuid), new Packet(MessageType.MESSAGE, "request#" + requestType));
    }
}
