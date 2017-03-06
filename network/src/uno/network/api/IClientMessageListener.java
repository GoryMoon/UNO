package uno.network.api;

/**
 * The listener interface for receiving events from the network thread
 * @author Gustaf Järgren
 * @version 06-03-2017
 */
public interface IClientMessageListener {

    /**
     * Called when a connection has been established
     */
    void onConnect();

    /**
     * Called when the connection to the server is disconnected
     * @param reason The reason for the disconnect
     */
    void onDisconnect(String reason);

    /**
     * Called when an error occurs on either the server side related to the client or on the client
     * @param type The type of error that occurred
     * @param message A message explaining the error
     */
    void onError(MessageType type, String message);

    /**
     * Called when a message is received from the server
     * @param type The type of the message that is received
     * @param message The message that was received
     */
    void onMessageReceived(MessageType type, Object message);

}
