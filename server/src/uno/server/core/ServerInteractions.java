package uno.server.core;

import javafx.util.Pair;
import uno.logic.Card;
import uno.logic.Color;
import uno.logic.GameCore;
import uno.logic.Type;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.api.Player;
import uno.network.server.NetworkServer;

import java.util.ArrayList;
import java.util.Comparator;
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
        sendNextTurn();
    }

    /**
     * Handles string messages received from the client
     * @param message
     */
    public void handleMessage(Player player, String message) {
        if (core.getWaitingForInput()) {
            if (message.startsWith("wild#")) {
                Color c = Color.valueOf(message.split("#")[1]);
                GameServer.logger.info("Received wild color picked: " + c);
                core.wild(c);
                sendNextTurn();
            }
        } else {
            if (message.equals("cheat")) {
                server.sendToAllPlayers("game-over");
                ArrayList<Pair<String, Integer>> winList = new ArrayList<>();
                core.getWinList().forEach(player1 -> winList.add(new Pair<>(player1.getName(), player1.getCards().size())));
                server.sendToAllPlayers(new Pair<String, ArrayList>("win-list", winList));
                return;
            }
            if (message.startsWith("play#")) {
                String[] s = message.split("#");
                Card card = new Card(Color.valueOf(s[2].toUpperCase()), Type.valueOf(s[1].toUpperCase()), Integer.parseInt(s[3]));
                GameServer.logger.info("Player (" + player.getID() + ") is trying to play (" + card + ")");
                ArrayList<Card> cards = core.getPlayers().get(core.getCurrentPlayerIndex()).getCards();
                for (Card c: cards) {
                    if (c.equals(card)) {
                        core.executeCard(c);
                        GameServer.logger.info("Executed card " + c);
                        if (!core.getWaitingForInput() && !core.getWinCondition())
                            sendNextTurn();
                        if (core.getWinCondition()) {
                            server.sendToAllPlayers("game-over");
                            ArrayList<Pair<String, Integer>> winList = new ArrayList<>();
                            core.getWinList().forEach(player1 -> winList.add(new Pair<>(player1.getName(), player1.getCards().size())));
                            server.sendToAllPlayers(new Pair<String, ArrayList>("win-list", winList));
                        }
                        break;
                    }
                }
            } else if (message.equals("draw-card")) {
                Card oldCard = core.getDeck().getPlayedCards().get(0);
                core.getPlayers().get(core.getCurrentPlayerIndex()).endDraw();
                Card newCard = core.getDeck().getPlayedCards().get(0);
                if (oldCard.equals(newCard)) {
                    ArrayList<Card> cards = core.getPlayers().get(core.getCurrentPlayerIndex()).getCards();
                    GameServer.logger.info(core.getPlayers().get(core.getCurrentPlayerIndex()).getName() + " drew a card and kept is (" + cards.get(cards.size()-1) + ")");
                } else {
                    GameServer.logger.info(core.getPlayers().get(core.getCurrentPlayerIndex()).getName() + " drew a card and played is (" + newCard + ")");
                }
                sendNextTurn();
            } else if (message.equals("uno")) {
                if (player.getID().equals(core.getPlayers().get(core.getCurrentPlayerIndex()).getUuid())) {
                    GameServer.logger.info(core.getPlayers().get(core.getCurrentPlayerIndex()).getName() + " said uno");
                    core.getPlayers().get(core.getCurrentPlayerIndex()).setUno(true);
                    ArrayList<Pair<UUID, Pair<String, Boolean>>> names = new ArrayList<>();
                    core.getPlayers().forEach(player1 -> names.add(new Pair<>(player1.getUuid(), new Pair<>(player1.getName(), player1.unoStatus()))));
                    server.sendToAllPlayers(names);
                }
            }
        }
    }

    /**
     * Handles object messages received from the client
     * @param message
     */
    public void handleMessage(Object message) {

    }

    private void sendNextTurn() {
        ArrayList<uno.logic.Player> players = core.getPlayers();
        ArrayList<Pair<UUID, Pair<String, Boolean>>> names = new ArrayList<>();
        uno.logic.Player p = players.get(core.getCurrentPlayerIndex());

        players.forEach(player -> names.add(new Pair<>(player.getUuid(), new Pair<>(player.getName(), player.unoStatus()))));
        server.sendToAllPlayers(names);

        ArrayList<Pair<UUID, Integer>> cardAmounts = new ArrayList<>();
        players.forEach(player -> cardAmounts.add(new Pair<>(player.getUuid(), player.getCards().size())));
        server.sendToAllPlayers(cardAmounts);

        for (uno.logic.Player p2: players) {
            ArrayList<String> cards = new ArrayList<>();
            p2.getCards().forEach(card -> cards.add(cardToString(card)));
            server.sendToPlayer(p2.getUuid(), cards);
        }

        Card topCard = core.getDeck().getPlayedCards().get(0);
        server.sendToAllPlayers(new Pair<>("played-card", cardToString(topCard)));

        server.sendToAllPlayers(new Pair<>("new-round", p.getUuid()));
    }

    private String cardToString(Card card) {
        return card.getType().toString().toLowerCase() + "#" + card.getColor().toString().toLowerCase() + "#" + card.getNumber();
    }

    /**
     * Use the one in GameServer instead<br/><br/>
     * Used to request an input from the player
     * @param uuid The UUID of the player that the request is for
     * @param requestType String of what's requested, the message is an collaboration between the server and client
     */
    @Deprecated
    public void requestInputFromPlayer(UUID uuid, String requestType) {
        server.requestInputFromPlayer(uuid, requestType);
    }
}
