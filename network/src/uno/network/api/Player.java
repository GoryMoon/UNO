package uno.network.api;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

/**
 * The player object that is used to keep track of each player thread
 */
public class Player implements Serializable {

    private InetAddress address;
    private UUID id;

    /**
     *
     * @param inetAddress The InetAddress of the client
     * @param id The UUID that for the client
     */
    public Player(InetAddress inetAddress, UUID id) {
        address = inetAddress;
        this.id = id;
    }

    /**
     * @return The InetAddress associated to this player
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * @return THe UUID associated to this player
     */
    public UUID getID() {
        return id;
    }
}
