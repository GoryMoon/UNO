package uno.server.core;

import uno.logic.Card;
import uno.logic.Color;
import uno.logic.GameCore;
import uno.logic.Type;
import uno.network.api.Pair;
import uno.network.api.Player;
import uno.network.server.NetworkServer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The bridge between the network and the logic
 * @author Gustaf Järgren
 * @version 06-03-2017
 */
public class ServerInteractions {

    private GameServer server;
    private GameCore core;
    private NetworkServer network;

    /**
     * Setups a new interaction to the server
     * @param gameServer The GameServer that is running this
     * @param core The core that is running for this
     * @param networkServer The network that's running for this
     */
    public ServerInteractions(GameServer gameServer, GameCore core, NetworkServer networkServer) {
        this.server = gameServer;
        this.core = core;
        this.network = networkServer;
        sendNextTurn();
    }

    /**
     * Handles string messages received from the client<br>
     * Makes sure that only the current player can send messages<br>
     * @param player The players that sent the message
     * @param message The message received
     */
    public void handleMessage(Player player, String message) {
        //Only the current player can do anything, this makes sure of that
        if (player.getID().equals(core.getPlayers().get(core.getCurrentPlayerIndex()).getUuid())) {

            if (core.getWaitingForInput()) {
                if (message.startsWith("wild#")) {
                    handleWildMessage(message);
                }
            } else {
                if (message.startsWith("play#")) {
                    handlePlayMessage(player, message);
                } else if (message.equals("draw-card")) {
                    handleDrawCardMessage();
                } else if (message.equals("uno")) {
                    handleUnoMessage();
                }
            }
        }
    }

    /**
     * Gets the color of the message and sets it to the current played wild card
     * @param message The message containing the color that was picked
     */
    private void handleWildMessage(String message) {
        Color c = Color.valueOf(message.split("#")[1]);
        GameServer.logger.info("Received wild color picked: " + c);
        core.wild(c);
        sendNextTurn();
    }

    /**
     * Checks if the current player have the provided card and then executes it.
     * If the played card doesn't require an input or the game wasn't won the next turn info is sent.
     * @param player The player that sent the message
     * @param message The message containing the played card
     */
    private void handlePlayMessage(Player player, String message) {
        String[] s = message.split("#");
        Card card = new Card(Color.valueOf(s[2].toUpperCase()), Type.valueOf(s[1].toUpperCase()), Integer.parseInt(s[3]));

        GameServer.logger.info("Player (" + player.getID() + ") is trying to play (" + card + ")");
        Card c = getMatchingCardFromPlayer(card);
        if (c != null) {
            core.executeCard(c);
            GameServer.logger.info("Player (" + player.getID() + ") played card (" + c + ")");

            if (!core.getWaitingForInput()) {
                if (core.getWinCondition())
                    sendWin();
                else
                    sendNextTurn();
            } else {
                sendCardInfo(core.getPlayers());
            }
        }
    }

    /**
     * Checks if the player have the card that was played and return the reference of that card
     * @param card The card to look for at the player
     * @return It the player have the card it's returned else null is returned
     */
    private Card getMatchingCardFromPlayer(Card card) {
        ArrayList<Card> cards = core.getPlayers().get(core.getCurrentPlayerIndex()).getCards();
        for (Card c : cards) {
            if (c.equals(card)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Draws a card for the current player and changes to the next turn<br>
     * Logs the outcome
     */
    private void handleDrawCardMessage() {

        //Used to determine of the card was picked up or not for logging
        Card oldCard = core.getDeck().getPlayedCards().get(0);
        Card newCard;

        core.getPlayers().get(core.getCurrentPlayerIndex()).endDraw();
        newCard = core.getDeck().getPlayedCards().get(0);

        //Used only for logging
        if (oldCard.equals(newCard)) {
            ArrayList<Card> cards = core.getPlayers().get(core.getCurrentPlayerIndex()).getCards();
            GameServer.logger.info(core.getPlayers().get(core.getCurrentPlayerIndex()).getName() + " drew a card and kept it (" + cards.get(cards.size() - 1) + ")");
        } else {
            GameServer.logger.info(core.getPlayers().get(core.getCurrentPlayerIndex()).getName() + " drew a card and played it (" + newCard + ")");
        }

        sendNextTurn();
    }

    /**
     * Set the 'uno' flag on the current player to true<br>
     * Sends the new info about the uno status to all players
     */
    private void handleUnoMessage() {
        GameServer.logger.info(core.getPlayers().get(core.getCurrentPlayerIndex()).getName() + " said uno");
        core.getPlayers().get(core.getCurrentPlayerIndex()).setUno(true);

        ArrayList<Pair<UUID, Pair<String, Boolean>>> names = new ArrayList<>();
        core.getPlayers().forEach(player1 -> names.add(new Pair<>(player1.getUuid(), new Pair<>(player1.getName(), player1.getUnoStatus()))));
        server.sendToAllPlayers(names);
    }

    /**
     * Sends info that the game is over to all players.<br>
     * It also sends the list of what place each player ended up on.
     */
    private void sendWin() {
        server.sendToAllPlayers("game-over");

        ArrayList<Pair<String, Integer>> winList = new ArrayList<>();
        core.getWinList().forEach(player1 -> winList.add(new Pair<>(player1.getName(), player1.getCards().size())));
        server.sendToAllPlayers(new Pair<String, ArrayList>("win-list", winList));
    }

    /**
     * Send data for a new turn to all players, that including:<br>
     * Players uno status, amount of cards, each player get's their cards,<br>
     * the currently played cards and that it's a new reound
     */
    private void sendNextTurn() {
        ArrayList<uno.logic.Player> players = core.getPlayers();
        sendPlayerInfo(players);
        sendCardInfo(players);

        server.sendToAllPlayers(new Pair<>("new-round", players.get(core.getCurrentPlayerIndex()).getUuid()));
    }

    /**
     * Sends the info amout the players, including:<br>
     * Their uuid, name and uno status
     * @param players List of players to send
     */
    private void sendPlayerInfo(ArrayList<uno.logic.Player> players) {
        ArrayList<Pair<UUID, Pair<String, Boolean>>> names = new ArrayList<>();
        players.forEach(player -> names.add(new Pair<>(player.getUuid(), new Pair<>(player.getName(), player.getUnoStatus()))));
        server.sendToAllPlayers(names);
    }

    /**
     * Sends the info about cards to players, including:<br>
     * The players card amount, each players cards and the current played card.
     * @param players The players to send info about
     */
    private void sendCardInfo(ArrayList<uno.logic.Player> players) {
        ArrayList<Pair<UUID, Integer>> cardAmounts = new ArrayList<>();
        players.forEach(player -> cardAmounts.add(new Pair<>(player.getUuid(), player.getCards().size())));
        server.sendToAllPlayers(cardAmounts);

        for (uno.logic.Player p2: players) {
            ArrayList<String> cards = new ArrayList<>();
            p2.getCards().forEach(card -> cards.add(cardToString(card)));
            server.sendToPlayer(p2.getUuid(), cards);
            GameServer.logger.info("Sending cards to " + p2.getName() + ":" + cards);
        }

        Card topCard = core.getDeck().getPlayedCards().get(0);
        server.sendToAllPlayers(new Pair<>("played-card", cardToString(topCard)));
    }

    /**
     * Converts a {@link Card} to a string to send over the network
     * @param card The card to convert
     * @return A string representation of the card
     */
    private String cardToString(Card card) {
        return card.getType().toString().toLowerCase() + "#" + card.getColor().toString().toLowerCase() + "#" + card.getNumber();
    }
}
