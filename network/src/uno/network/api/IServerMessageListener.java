package uno.network.api;

import java.net.Socket;

public interface IServerMessageListener {

    boolean onPrePlayerConnect(Socket socket);
    void onPlayerConnect(Player player);
    void onPlayerDisconnect(Player player, boolean expected);

    void onMessageReceived(Player player, MessageType type, Object data);
}
