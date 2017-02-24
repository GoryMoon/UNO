package uno.server.core;

import uno.logic.GameCore;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.server.NetworkServer;

import java.util.UUID;

/**
 * The bridge between the network and the logic
 */
public class ServerInteractions {

    private GameServer server;
    private GameCore core;
    private NetworkServer network;

    public ServerInteractions(GameServer gameServer, GameCore core, NetworkServer networkServer) {
        this.server = gameServer;
        this.core = core;
        this.network = networkServer;
    }

    /**
     * Handles string messages received from the client
     * @param message
     */
    public void handleMessage(String message) {
        //if (core.isWaitingForInput) {

        //} else {

        //}
    }

    /**
     * Handles object messages received from the client
     * @param message
     */
    public void handleMessage(Object message) {

    }

    /**
     * Use the one in GameServer instead<br/><br/>
     * Used to request an input from the player
     * @param uuid The UUID of the player that the request is for
     * @param requestType String of what's requested, the message is an collaboration between the server and client
     */
    @Deprecated
    public void requestInputFromPlayer(UUID uuid, String requestType) {
        network.sendToPlayer(network.getPlayerFromUUID(uuid), new Packet(MessageType.MESSAGE, "request-" + requestType));
    }
}
