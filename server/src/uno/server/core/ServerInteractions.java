package uno.server.core;

import uno.logic.GameCore;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.server.NetworkServer;

import java.util.UUID;

public class ServerInteractions {

    private GameServer server;
    private GameCore core;
    private NetworkServer network;

    public ServerInteractions(GameServer gameServer, GameCore core, NetworkServer networkServer) {
        this.server = gameServer;
        this.core = core;
        this.network = networkServer;
    }

    public void handleMessage(String message) {
        
    }

    public void requestInputFromPlayer(UUID uuid, String requestType) {
        network.sendToPlayer(network.getPlayerFromUUID(uuid), new Packet(MessageType.MESSAGE, "request-" + requestType));
    }
}
