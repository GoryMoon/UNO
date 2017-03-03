package uno.network.server;

import uno.network.api.IServerMessageListener;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.api.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * The network handler for the server side<br/><br/>
 * Accepts connections from clients and saves them in separate threads<br/>
 * Takes a max amount of players, if it's full new players are ignored and receives a message that it's full<br/>
 * Handles all the connects and disconnects and if it's an expected disconnect or not<br/>
 * Uses a listener for the events that happens
 */
public class NetworkServer implements Runnable {

    private IServerMessageListener listener;
    private int port;
    private int maxPlayers;

    private boolean running = false;
    private Thread mainNetworkThread;
    private HashMap<Player, PlayerThread> playerThreads;
    private ArrayList<Player> players;
    private ServerSocket serverSocket;
    private Socket socket = null;

    /**
     * Setups the network server
     * @param listener The listener that should get the events
     * @param port The port to listen to
     * @param maxPlayers The max amount of players that can connect
     */
    public NetworkServer(IServerMessageListener listener, int port, int maxPlayers) {
        this.listener = listener;
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.playerThreads = new HashMap<>();
        this.players = new ArrayList<>();
        this.mainNetworkThread = new Thread(this, "GameProtocol Network Thread");
    }

    /**
     * Stops the network, stops listening for new players and disconnects all the currently connected players
     */
    public void stop() {
        running = false;
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Player, PlayerThread> entry: playerThreads.entrySet())
            entry.getValue().disconnectPlayer();
    }

    /**
     * Starts listening for new players and starts the thread for the network
     */
    public void start() {
        running = true;
        mainNetworkThread.start();
    }

    /**
     * Gets the player from provided UUID
     * @param uuid The UUID of the player that want's to be retrieved
     * @return If a player for the UUID exist that player if returned else null
     */
    public Player getPlayerFromUUID(UUID uuid) {
        return playerThreads.entrySet().stream().filter(entry -> entry.getKey().getID().equals(uuid)).findFirst().orElse(new AbstractMap.SimpleEntry<>(null, null)).getKey();
    }

    /**
     * Gets a list of all the currently connected players uuids
     * @return List of all players uuids
     */
    public ArrayList<UUID> getPlayerUUIDs() {
        ArrayList<UUID> uuids = new ArrayList<>();
        players.forEach(player -> uuids.add(player.getID()));
        return uuids;
    }

    /**
     * Sends a packet to all connected players
     * @param packet The packet to be sent
     */
    public void sendToAllPlayers(Packet packet) {
        for (Map.Entry<Player, PlayerThread> entry: playerThreads.entrySet())
            sendToPlayer(entry.getKey(), packet);
    }

    /**
     * Sends a packet to all connected players except the one provided
     * @param player The player not to receive the packet
     * @param packet The packet to be sent
     */
    public void sendToAllExcept(Player player, Packet packet) {
        for (Map.Entry<Player, PlayerThread> entry: playerThreads.entrySet()) {
            if (!player.equals(entry.getKey()))
                sendToPlayer(entry.getKey(), packet);
        }
    }

    /**
     * Sends a packet to a specific player
     * @param player The player to receive the packet
     * @param packet The packet to be sent
     */
    public void sendToPlayer(Player player, Packet packet) {
        if (playerThreads.containsKey(player))
            playerThreads.get(player).sendMessage(packet);
        else
            System.out.println("Player isn't connected");
    }

    /**
     * Internal relay for receiving the packet
     * @param player The player that sent the packet
     * @param packet The packet received
     */
    void messageFromPlayer(Player player, Packet packet) {
        listener.onMessageReceived(player, packet.type, packet.data);
    }

    /**
     * The threads' run implementation<br/>
     * Does all the heavy work with listening for new players and connecting them
     */
    @Override
    public void run() {
        if (!setupConnection()) {
            running = false;
        }

        while (running) {
            try {
                if (serverSocket != null)
                    socket = serverSocket.accept();
            } catch (SocketException e) {
                running = false;
                break;
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }
            if (playerThreads.size() < maxPlayers) {
                if (!listener.onPrePlayerConnect(socket)) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket = null;
                    continue;
                }
                UUID uuid = UUID.randomUUID();
                Player player = new Player(socket.getInetAddress(), uuid);
                PlayerThread thread = new PlayerThread(this, socket, player);
                playerThreads.put(player, thread);
                players.add(player);
                listener.onPlayerConnect(player);
                sendToAllExcept(player, new Packet(MessageType.PLAYER_JOINED, String.valueOf(player.getID())));
                sendToPlayer(player, new Packet(MessageType.CONNECTED, String.valueOf(player.getID())));
                sendToPlayer(player, new Packet(MessageType.PLAYER_INFO, player));
                thread.start();
            } else {
                try {
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(new Packet(MessageType.ERROR_FULL, "Game is full"));
                    out.close();
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Internal relay for when a player is disconnected
     * @param player The player that is disconnected
     * @param expected If the disconnect was expected or not, an unexpected disconnect could be that the client crashed
     */
    void playerLeft(Player player, boolean expected) {
        playerThreads.remove(player);
        listener.onPlayerDisconnect(player, expected);
        sendToAllPlayers(new Packet(MessageType.PLAYER_LEFT, String.valueOf(player.getID())));
    }


    /**
     * Setups the socket for listening
     * @return If the setup was successful or not
     */
    private boolean setupConnection() {
        try {
            serverSocket = new ServerSocket(port);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
