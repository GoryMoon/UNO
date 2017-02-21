package uno.network.client;

import uno.network.api.IClientMessageListener;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.api.Player;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

public class NetworkClient implements Runnable {

    private String hostname;
    private int port;
    private IClientMessageListener listener;
    private Thread networkThread;
    private boolean running;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean stopped;
    private Player player;

    public NetworkClient(IClientMessageListener listener, String hostname, int port) {
        this.listener = listener;
        this.hostname = hostname;
        this.port = port;
    }

    public Player getPlayer() {
        return player;
    }

    public void connect() {
        running = true;
        stopped = false;
        Random rand = new Random();
        networkThread = new Thread(this, "Client network thread" + rand.nextInt(2000));
        networkThread.start();
    }

    public void disconnect() {
        sendToServer(new Packet(MessageType.DISCONNECT, ""));
        closeConnection();
        stopped = true;
    }

    private void closeConnection() {
        running = false;
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
            in = null;
            out = null;
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer(Packet packet) {
        try {
            if (out != null)
                out.writeObject(packet);
            else
                System.out.println("Not connected to server!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (!setupConnection()) {
            listener.onError(MessageType.ERROR_REACH, "Can't reach server");
            running = false;
        }

        while (!Thread.interrupted() && running) {
            Object o = null;
            try {
                o = in.readObject();
            } catch (SocketException | EOFException e) {
                closeConnection();
                if (!stopped) {
                    String message = "Server closed unexpectedly";
                    listener.onError(MessageType.ERROR_CLOSED, message);
                    listener.onDisconnect(message);
                } else {
                    listener.onDisconnect("Disconnected from server");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (o instanceof Packet) {
                Packet p = (Packet) o;
                if (p.type == MessageType.ERROR_FULL) {
                    listener.onError(p.type, String.valueOf(p.data));
                    stopped = true;
                } else if (p.type == MessageType.CONNECTED) {
                    listener.onConnect();
                } else if (p.type == MessageType.SERVER_CLOSED) {
                    listener.onDisconnect(String.valueOf(p.data));
                    closeConnection();
                } else if (p.type == MessageType.PLAYER_INFO) {
                    player = (Player) p.data;
                } else {
                    listener.onMessageReceived(p.type, p.data);
                }
            }
        }
    }

    private boolean setupConnection() {
        try {
            socket = new Socket(hostname, port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("Client network started!");
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
