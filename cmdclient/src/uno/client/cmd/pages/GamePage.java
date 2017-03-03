package uno.client.cmd.pages;

import javafx.util.Pair;
import uno.client.cmd.Card;
import uno.client.cmd.GameClient;
import uno.client.cmd.sound.MediaPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class GamePage extends Page {

    final Object inputLock;
    boolean currentTurn = false;
    private GameClient client;

    private ArrayList<Pair<UUID, Pair<String, Boolean>>> players;
    private ArrayList<Pair<UUID, Integer>> cardAmount;
    private ArrayList<Card> cards;
    private UUID oldPlayer;
    private UUID currentPlayer;
    private boolean shouldPrint;
    private Card lastPlayedCard;

    private boolean requestingWild = false;

    private static final String HEAD = "\n\n\n\n\n\n**********************************\n";

    public GamePage() {
        inputLock = new Object();
    }

    @Override
    public void show(GameClient client) {

        synchronized (inputLock) {
            try {
                if (GameClient.DEBUG) System.out.println("Locked: " + requestingWild + "#" + currentTurn);
                while(!requestingWild && !currentTurn) {
                    if (currentPlayer != null && (!currentPlayer.equals(oldPlayer) || shouldPrint)) {
                        printPlayerInfo();
                        oldPlayer = currentPlayer;
                        shouldPrint = false;
                    }
                    inputLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (GameClient.DEBUG) System.out.println("Unlocked");
        super.show(client);
    }

    @Override
    public void printMessage(GameClient client) {
        if (requestingWild) {
            System.out.println(HEAD + "You need to pick a color for the placed wild card:\n1.Red\n2.Yellow\n3.Green\n4.Blue\n\nPick choice:");
        } else {
            printPlayerInfo();
            System.out.println((cards.size() + 1) + ".Uno\n" + (cards.size() + 2) + ".Draw Card\n\nPick choice:");
        }
    }

    private void printPlayerInfo() {
        System.out.println(HEAD);
        System.out.println("Players <Card Amounts>");
        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).getKey().equals(client.networkClient.getPlayer().getID())) {
                System.out.println(players.get(i).getValue().getKey() + " <" + cardAmount.get(i).getValue() + "> " + (players.get(i).getValue().getValue() ? "Uno": ""));
            }
        }
        System.out.println("\nCurrently played card:\n\t<" + lastPlayedCard + ">");
        System.out.println("\n\nYour cards:");
        for (int i = 0; i < cards.size(); i++) {
            System.out.println(i + 1 + ".<" + cards.get(i).toString()+ ">");
        }
    }

    @Override
    void handleInput(GameClient client, int in) {
        if (requestingWild) {
            if (in >= 1 && in <= 4) {
                String text = in == 1 ? "RED": in == 2 ? "YELLOW" : in == 3 ? "GREEN": "BLUE";
                client.sendMessageToServer("wild#" + text);
                requestingWild = false;
            }
        } else {
            if (in >= 1 && in <= cards.size() + 2) {
                if (in <= cards.size()) {
                    Card card = cards.get(in-1);
                    client.sendMessageToServer("play#" + cardToString(card));
                    currentTurn = false;
                } else if (in == cards.size() + 1) {
                    client.sendMessageToServer("uno");
                } else {
                    client.sendMessageToServer("draw-card");
                    currentTurn = false;
                }
            }
        }
    }

    private String cardToString(Card card) {
        return card.type + "#" + card.color + "#" + card.number;
    }

    public void stopGame() {
        synchronized (inputLock) {
            inputLock.notify();
        }
    }

    @Override
    protected boolean wantInput() {
        return currentTurn || requestingWild;
    }

    @Override
    public void setup(GameClient client) {
        this.client = client;
        players = new ArrayList<>();
        cardAmount = new ArrayList<>();
        cards = new ArrayList<>();
    }

    @Override
    public void handleMessage(Object o) {
        if (o instanceof ArrayList) {
            if (((ArrayList) o).get(0) instanceof Pair) {
                if (((Pair) ((ArrayList) o).get(0)).getValue() instanceof Pair)
                    players = (ArrayList<Pair<UUID, Pair<String, Boolean>>>) o;
                if (((Pair) ((ArrayList) o).get(0)).getValue() instanceof Integer)
                    cardAmount = (ArrayList<Pair<UUID, Integer>>) o;
            }

            if (((ArrayList) o).get(0) instanceof String) {
                cards = new ArrayList<>();
                if (((ArrayList) o).size() > 0) {
                    ((ArrayList) o).forEach(o1 -> cards.add(new Card(((String) o1).split("#"))));
                }
            }
        }

        if (o instanceof Pair) {
            if (((Pair) o).getKey().equals("new-round")) {
                oldPlayer = currentPlayer;
                currentPlayer = (UUID) ((Pair) o).getValue();
                currentTurn = currentPlayer.equals(client.networkClient.getPlayer().getID());
                if (currentTurn) client.addScheduledTask(new MediaPlayer("ding.wav"));
                if (GameClient.DEBUG) System.out.println(currentPlayer + ":" + currentTurn);
                shouldPrint = true;
            } else if (((Pair) o).getKey().equals("played-card")) {
                lastPlayedCard = new Card(((String)((Pair) o).getValue()).split("#"));
            }
        }

        if (o instanceof String) {
            if (((String) o).startsWith("request#")) {
                String req = ((String) o).split("#")[1];
                if (req.equals("wild")) {
                    requestingWild = true;
                    if (GameClient.DEBUG) System.out.println("requesting wild");
                }
            }
        }

        synchronized (inputLock) {
            inputLock.notify();
        }
    }
}
