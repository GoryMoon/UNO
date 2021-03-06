package uno.network.server;

import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.api.Player;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * The thread responsible for communications between a client and the server
 * @author Gustaf Järgren
 * @version 06-03-2017
 */
public class PlayerThread extends Thread {

    private NetworkServer server;
    private Socket socket;
    private Player player;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = true;

    /**
     * Setups the connection streams for the socket that is already setup an connected
     * @param networkServer The network server object that this is connected to
     * @param socket The socket that was created when the client connected
     * @param player The player object that is associated to this thread
     */
    public PlayerThread(NetworkServer networkServer, Socket socket, Player player) {
        super();
        this.server = networkServer;
        this.socket = socket;
        this.player = player;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            NetworkServer.logger.error("Bad connection (" + player.getAddress() + ":" + socket.getPort() + ")(not using proper client?), aborting connection", e);
            closeConnection();
        }
    }

    /**
     * The threads' run implementation<br>
     * Handles the messages received from the client and any disconnects
     */
    @Override
    public void run() {
        Object o = null;
        while (!Thread.interrupted() && running) {
            try {
                o = in.readObject();
            } catch (SocketException | EOFException e) {
                if (running)
                    server.playerLeft(player, false);
                closeConnection();
            } catch (IOException | ClassNotFoundException e) {
                NetworkServer.logger.error(e, e);
            }

            if (o instanceof Packet) {
                Packet p = (Packet) o;
                if (p.type == MessageType.DISCONNECT) {
                    server.playerLeft(player, true);
                    closeConnection();
                } else {
                    server.messageFromPlayer(player, (Packet) o);
                }
                o = null;
            }
        }
        if (socket != null)
            try {
                socket.close();
            } catch (IOException e) {
                NetworkServer.logger.error(e, e);
            }
    }

    /**
     * Internal relay for disconnecting an individual client
     */
    void disconnectPlayer() {
        sendMessage(new Packet(MessageType.SERVER_CLOSED, "Server closed"));
        closeConnection();
    }

    /**
     * Closes the connection with the client and stops the listening of messages
     */
    private void closeConnection() {
        running = false;
        try {
            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException ignored) {}
    }

    /**
     * Sends the packet to the client associated with this thread
     * @param packet The packet to be sent
     */
    public void sendMessage(Packet packet) {
        if (running && socket.isConnected() && out != null) {
            try {
                out.writeObject(packet);
            } catch (IOException e) {
                NetworkServer.logger.error(e, e);
                server.playerLeft(player, false);
                closeConnection();
            }
        }
    }

    /**
     * Gets the status if the thread is running or not
     * @return Returns if the thread is still running or not
     */
    public boolean isRunning() {
        return running;
    }
}
