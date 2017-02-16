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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameServer implements Runnable {

    private IServerMessageListener listener;
    private int port;
    private int maxPlayers;

    private boolean running = false;
    private Thread mainNetworkThread;
    private HashMap<Player, PlayerThread> playerThreads;
    private ServerSocket serverSocket;
    private Socket socket = null;

    public GameServer(IServerMessageListener listener, int port, int maxPlayers) {
        this.listener = listener;
        this.port = port;
        this.maxPlayers = maxPlayers;
        this.playerThreads = new HashMap<>();
        this.mainNetworkThread = new Thread(this, "GameProtocol Network Thread");
    }

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
        System.out.println("GameProtocol Network Thread stopped!");
    }

    public void start() {
        running = true;
        mainNetworkThread.start();
    }

    public Player getPlayerFromUUID(UUID uuid) {
        return playerThreads.entrySet().stream().filter(entry -> entry.getKey().getID().equals(uuid)).findFirst().get().getKey();
    }

    public void sendToAllPlayers(Packet packet) {
        for (Map.Entry<Player, PlayerThread> entry: playerThreads.entrySet())
            sendToPlayer(entry.getKey(), packet);
    }

    public void sendToAllExcept(Player player, Packet packet) {
        for (Map.Entry<Player, PlayerThread> entry: playerThreads.entrySet()) {
            if (!player.equals(entry.getKey()))
                sendToPlayer(entry.getKey(), packet);
        }
    }

    public void sendToPlayer(Player player, Packet packet) {
        if (playerThreads.containsKey(player))
            playerThreads.get(player).sendMessage(packet);
        else
            System.out.println("Player isn't connected");
    }

    public void messageFromPlayer(Player player, Packet packet) {
        listener.onMessageReceived(player, packet.type, packet.data);
    }

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
                UUID uuid = UUID.randomUUID();
                Player player = new Player(socket.getInetAddress(), uuid);
                PlayerThread thread = new PlayerThread(this, socket, player);
                listener.onPlayerConnect(player);
                sendToAllPlayers(new Packet(MessageType.PLAYER_JOINED, String.valueOf(player.getID())));
                playerThreads.put(player, thread);
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

    public void playerLeft(Player player, boolean expected) {
        playerThreads.remove(player);
        listener.onPlayerDisconnect(player, expected);
        sendToAllPlayers(new Packet(MessageType.PLAYER_LEFT, String.valueOf(player.getID())));
    }


    private boolean setupConnection() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("GameProtocol Network Thread started!");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
