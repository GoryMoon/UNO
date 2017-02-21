package uno.network;

import uno.network.api.IServerMessageListener;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.api.Player;
import uno.network.server.NetworkServer;

import java.net.Socket;
import java.util.Scanner;

public class TestServer implements IServerMessageListener {

    private NetworkServer networkServer;
    private Scanner scanner;

    public TestServer(String port, String players) {
        networkServer = new NetworkServer(this, Integer.parseInt(port), Integer.parseInt(players));
        scanner = new Scanner(System.in);
        String in;
        loop: while (true) {
            in = scanner.nextLine();
            switch (in.substring(0, in.contains(" ") ? in.indexOf(' '): in.length())) {
                case "start":
                    networkServer.start();
                    break;
                case "send":
                    if (in.length() > 4) {
                        networkServer.sendToAllPlayers(new Packet(MessageType.MESSAGE, in.substring(5)));
                        System.out.println("Message sent!");
                    }
                    break;
                case "stop":
                    networkServer.stop();
                    break loop;
                default:
                    System.out.println("Unknown command!");
            }
        }
    }

    public static void main(String[] args) {
        new TestServer(args[0], args[1]);
    }

    @Override
    public boolean onPrePlayerConnect(Socket socket) {
        return true;
    }

    @Override
    public void onPlayerConnect(Player player) {
        System.out.println("Player connected to the server: " + player.getAddress() + ": " + player.getID());
    }

    @Override
    public void onPlayerDisconnect(Player player, boolean expected) {
        System.out.println("Player disconnected from the server: " + player.getAddress() + ": " + player.getID() + ", Expected: " + expected);
    }

    @Override
    public void onMessageReceived(Player player, MessageType type, Object    message) {
        System.out.println("Message from player (" + player.getAddress() + ": " + player.getID() + ") with type (" + type.toString() + "): " + message);
        networkServer.sendToAllExcept(player, new Packet(type, message));
    }
}
