package uno.network.api;

import java.io.Serializable;

public class Packet implements Serializable {

    public MessageType type;
    public Object data;

    public Packet(MessageType type, Object data) {
        this.type = type;
        this.data = data;
    }
}
