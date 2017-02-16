package uno.network.api;

import java.io.Serializable;

public enum MessageType implements Serializable {
    PLAYER_JOINED,
    PLAYER_LEFT,
    PLAYER_INFO,
    CONNECTED,
    DISCONNECT,
    MESSAGE,
    SERVER_CLOSED,
    ERROR_FULL,
    ERROR_CLOSED,
    ERROR_REACH
}
