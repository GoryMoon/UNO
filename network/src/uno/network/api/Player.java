package uno.network.api;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

public class Player implements Serializable {

    private InetAddress address;
    private UUID id;

    public Player(InetAddress inetAddress, UUID id) {
        address = inetAddress;
        this.id = id;
    }

    public InetAddress getAddress() {
        return address;
    }

    public UUID getID() {
        return id;
    }
}
