package uno.network.api;

import java.io.Serializable;

/**
 * The different types of messages that are sent<br>
 * Most of them are only used internally and never seen anywhere else
 * @author Gustaf Järgren
 * @version 06-03-2017
 */
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
    ERROR_RUNNING,
    ERROR_REACH
}
