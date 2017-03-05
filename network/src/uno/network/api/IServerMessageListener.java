package uno.network.api;

import java.net.Socket;

/**
 * The listener interface for receiving events from the network thread
 */
public interface IServerMessageListener {

    /**
     * Called before the connection to the player is saved but after the check if there is room for the client
     * @param socket The socket that tries to connect to the server
     * @return True if the connection is allowed to connect or false if not
     */
    boolean onPrePlayerConnect(Socket socket);

    /**
     * Called when a player has been connected to the sever and before all the connection messages are sent
     * @param player The player that connected
     */
    void onPlayerConnect(Player player);

    /**
     * Called when a player is disconnected for any reason<br>
     * If the disconnect was unexpected the player could have crashed or lost the connection<br>
     * An expected disconnect only happens if the player sends a message about it beforehand
     * @param player The player that was disconnected
     * @param expected If the disconnect was expected or not
     */
    void onPlayerDisconnect(Player player, boolean expected);

    /**
     * Called when a message from a player is received
     * @param player The player that sent the message
     * @param type The type of the message
     * @param data The data that was sent with the message
     */
    void onMessageReceived(Player player, MessageType type, Object data);
}
