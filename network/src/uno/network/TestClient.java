package uno.network;

import uno.network.api.IClientMessageListener;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.client.NetworkClient;

import java.util.Scanner;

/**
 * A test client for the network, not used in the game
 * @author Gustaf Järgren
 * @version 06-03-2017
 */
public class TestClient implements IClientMessageListener {

    private Scanner scanner;

    /**
     * Creates a new test client for the network
     * @param host The host address to connect to
     * @param port The port to connect to
     */
    public TestClient(String host, String port) {
        NetworkClient client = new NetworkClient(this, host, Integer.parseInt(port));
        scanner = new Scanner(System.in);
        String in;
        loop: while (true) {
            in = scanner.nextLine();
            switch (in.substring(0, in.contains(" ") ? in.indexOf(' '): in.length())) {
                case "start":
                    client.connect();
                    break;
                case "send":
                    if (in.length() > 4) {
                        client.sendToServer(new Packet(MessageType.MESSAGE, in.substring(5)));
                        System.out.println("Message sent!");
                    }
                    break;
                case "stop":
                    client.disconnect();
                    break loop;
                default:
                    System.out.println("Unknown command!");
            }
        }
    }

    /**
     * Main entry point into the test client<br>
     * Arguments needed are: [host] [port]
     * @param args The arguments used to point the client to the server
     */
    public static void main(String[] args) {
        new TestClient(args[0], args[1]);
    }

    @Override
    public void onConnect() {
        System.out.println("Connected to server!");
    }

    @Override
    public void onDisconnect(String reason) {
        System.out.println("Disconnected from server: " + reason);
    }

    @Override
    public void onError(MessageType type, String message) {
        System.out.println("Error from server (" + type + "): " + message);
    }

    @Override
    public void onMessageReceived(MessageType type, Object message) {
        System.out.println("Message from server with type (" + type.toString() + "): " + message);
    }
}
