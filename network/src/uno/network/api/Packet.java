package uno.network.api;

import java.io.Serializable;

/**
 * The object that is sent between the server and client<br>
 * Holds information about the data that is sent<br>
 * @author Gustaf Järgren
 * @version 06-03-2017
 */
public class Packet implements Serializable {

    public MessageType type;
    public Object data;

    /**
     * Initializes the packet
     * @param type The message type that this packet is
     * @param data The data that is to be sent
     */
    public Packet(MessageType type, Object data) {
        this.type = type;
        this.data = data;
    }
}
