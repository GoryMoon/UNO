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

public class PlayerThread extends Thread {

    private NetworkServer server;
    private Socket socket;
    private Player player;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = true;

    public PlayerThread(NetworkServer networkServer, Socket socket, Player player) {
        this.server = networkServer;
        this.socket = socket;
        this.player = player;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            running = false;
        }
    }

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
                e.printStackTrace();
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
                e.printStackTrace();
            }
    }

    public void disconnectPlayer() {
        sendMessage(new Packet(MessageType.SERVER_CLOSED, "Server closed"));
        closeConnection();
    }

    private void closeConnection() {
        running = false;
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Packet packet) {
        if (socket.isConnected() && out != null) {
            try {
                out.writeObject(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
