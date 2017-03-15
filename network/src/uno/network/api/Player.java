package uno.network.api;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

/**
 * The player object that is used to keep track of each player thread
 * @author Gustaf Järgren
 * @version 06-03-2017
 */
public class Player implements Serializable {

    private InetAddress address;
    private UUID id;

    /**
     * Creates a new Player instance
     * @param inetAddress The InetAddress of the client
     * @param id The UUID that for the client
     */
    public Player(InetAddress inetAddress, UUID id) {
        address = inetAddress;
        this.id = id;
    }

    /**
     * Gets the address of the player
     * @return The InetAddress associated to this player
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * Gets the UUID of the player
     * @return The UUID associated to this player
     */
    public UUID getID() {
        return id;
    }
}
