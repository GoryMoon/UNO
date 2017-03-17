package uno.ui.screens;

import uno.network.api.Pair;
import uno.ui.Card;
import uno.ui.Main;
import uno.ui.PauseOverlay;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Displays the area where the game is played<br>
 * @author Betina Andersson &amp; Shahad Naji
 * @version  2017-03-17
 */
public class PlayArea implements IScreen {

    private JFrame frame;
    private Main main;
    private JPanel cardPanel;
    private JPanel infoPanel;
    private JLabel cardPile;
    private JLabel turnInfo;
    private JToggleButton unoButton;
    private ArrayList<Pair<UUID, Pair<String, Boolean>>> players;
    private ArrayList<Pair<UUID, Integer>> cardAmount;
    private ArrayList<Card> cards;
    private UUID currentPlayer;
    private boolean currentTurn;

    @Override
    public void show() {
        frame = new JFrame("UNO Play");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));
        frame.setPreferredSize(new Dimension(1000, 700));
        frame.setLocationRelativeTo(null);

        JLabel background = new JLabel(new ImageIcon(Main.class.getResource("assets/backgrounds/background3.jpg")));
        background.setLayout(new BorderLayout());
        contentPane.add(background);

        cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(2, 8, 2, 2));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        turnInfo = new JLabel();
        updateTurnText();
        bottomPanel.add(turnInfo, Component.CENTER_ALIGNMENT);
        bottomPanel.add(cardPanel);
        background.add(bottomPanel, BorderLayout.SOUTH);

        JButton pause =  new JButton(new ImageIcon (Main.class.getResource("assets/setting.png")));
        pause.setOpaque(false);
        pause.setContentAreaFilled(false);
        pause.setBorderPainted(false);
        background.add(pause, BorderLayout.EAST);
        pause.addActionListener(e -> {
            PauseOverlay so = new PauseOverlay();
            so.setMain(main);
            so.show();
        });

        JButton drawCard = new JButton(new ImageIcon(Main.class.getResource("assets/unobak.png")));
        drawCard.setOpaque(false);
        drawCard.setContentAreaFilled(false);
        drawCard.setBorderPainted(false);
        background.add(drawCard, BorderLayout.WEST);

        drawCard.addActionListener(e -> {
            if (currentTurn)
                main.sendMessageToServer("draw-card");
            currentTurn = false;
         });

        unoButton = new JToggleButton(new ImageIcon(Main.class.getResource("assets/unoknapp.png")));
        unoButton.addActionListener(e -> {
            main.sendMessageToServer("uno");
        });

        infoPanel = new JPanel(new FlowLayout());
        infoPanel.setOpaque(false);
        background.add(infoPanel, BorderLayout.NORTH);

        cardPile = new JLabel();
        cardPile.setOpaque(false);
        background.add(cardPile, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void hide() {
        players = null;
        cardAmount = null;
        cards = null;
        frame.setVisible(false);
    }

    @Override
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     * Gets the player related to the provided uuid
     * @param uuid The uuid of the player that is requested
     * @return A pair containing the player info if it exists or pair with the name "No one"
     */
    private Pair<UUID, Pair<String, Boolean>> getPlayer(UUID uuid) {
        if (players != null && currentPlayer != null) {
            for (Pair<UUID, Pair<String, Boolean>> pair: players) {
                if (pair.getKey().equals(uuid)) {
                    return pair;
                }
            }
        }
        return new Pair<>(null, new Pair<>("No one", false));
    }

    /**
     * Updates the turn text about who's turn it is
     */
    private void updateTurnText() {
        turnInfo.setText("Current turn is: " + (currentTurn ? "You": getPlayer(currentPlayer).getValue().getKey()));
        frame.pack();
    }

    /**
     * Adds a card to the panel showing the cards, adds an action if pressed to play the card
     * @param card The card to add to the panel
     */
    private void addCard(Card card) {
        JButton cardButton = new JButton(new ImageIcon(Main.class.getResource("assets/cards/" + card.toString() + ".png")));
        cardButton.addActionListener(e -> {
            if (currentTurn)
                main.sendMessageToServer("play#" + cardToString(card));
        });
        cardPanel.add(cardButton);
    }

    /**
     * Updates the cards shown on the screen by removing all and adding them back
     */
    private void updateCards() {
        cardPanel.removeAll();
        cardPanel.repaint();
        cards.forEach(this::addCard);
        frame.pack();
    }

    /**
     * Handles all the messages received during a game
     * @param o The message that is received
     */
    public void handleMessage(Object o) {
        if (o instanceof ArrayList) {
            if (((ArrayList) o).get(0) instanceof Pair) {
                handlePlayerInfoMessage((ArrayList) o);
            }

            if (((ArrayList) o).get(0) instanceof String) {
                handleCardInfoMessage((ArrayList<String>) o);
            }
        }

        if (o instanceof Pair) {
            handlePairMessages((Pair) o);
        }

        if (o instanceof String && ((String) o).startsWith("request#")) {
            handleRequestMessage((String) o);
        }
    }

    /**
     * Handles messages that contain the info about what cards the player have<br>
     * The cards are provided in their string representations with a # between each individual info
     * @param list The list of cards that the player have
     */
    private void handleCardInfoMessage(ArrayList<String> list) {
        cards = new ArrayList<>();
        if (list.size() > 0) {
            list.forEach(o1 -> cards.add(new Card(o1.split("#"))));
            updateCards();
        }
    }

    /**
     * Saves the lists of info about the players and updates the on screen info
     * @param list The message list received form the server
     */
    private void handlePlayerInfoMessage(ArrayList list) {
        if (((Pair) list.get(0)).getValue() instanceof Pair)
            players = (ArrayList<Pair<UUID, Pair<String, Boolean>>>) list;
        if (((Pair) list.get(0)).getValue() instanceof Integer)
            cardAmount = (ArrayList<Pair<UUID, Integer>>) list;
        updatePlayerInfo();
    }

    /**
     * Updates the names, card amounts and if the player has said Uno at the top of the scree<br>
     * Updates only if the info about both players and card amount has been received<br>
     * Adds half of the players on one side of the Uno button and the other half on the other side
     */
    private void updatePlayerInfo() {
        if (players != null && cardAmount != null) {
            infoPanel.removeAll();

            double half = Math.ceil(players.size() / 2D);
            for(int i = 0; i < half; i++){
                if (!players.get(i).getKey().equals(main.networkClient.getPlayer().getID())) {
                    infoPanel.add(new JLabel(players.get(i).getValue().getKey() + " <" + cardAmount.get(i).getValue() + ">" + (players.get(i).getValue().getValue() ? " Uno": "")));
                }
            }

            infoPanel.add(unoButton);

            for(int i = 0; i < players.size() - half; i++){
                int j = (int) (half + i);
                if (!players.get(j).getKey().equals(main.networkClient.getPlayer().getID())) {
                    infoPanel.add(new JLabel(players.get(j).getValue().getKey() + " <" + cardAmount.get(j).getValue() + ">" + (players.get(j).getValue().getValue() ? " Uno": "")));
                }
            }

            infoPanel.repaint();
            frame.pack();
        }
    }

    /**
     * Handles messages that has the base object as a pair instance<br>
     * The messages that uses that are when a new round is starting and when info about a played card is received
     * @param pair The pair containing the message
     */
    private void handlePairMessages(Pair pair) {
        if (pair.getKey().equals("new-round")) {
            currentPlayer = (UUID) pair.getValue();
            currentTurn = currentPlayer.equals(main.networkClient.getPlayer().getID());
            updateTurnText();
            frame.pack();
        } else if (pair.getKey().equals("played-card")) {
            Card lastPlayedCard = new Card(((String) pair.getValue()).split("#"));
            cardPile.setIcon(new ImageIcon(Main.class.getResource("assets/cards/" + lastPlayedCard.toString() + ".png")));
        }
    }

    /**
     * Handles when a request for user input is needed from the server
     * @param message The request message received from the server
     */
    private void handleRequestMessage(String message) {
        String req = message.split("#")[1];
        if (req.equals("wild")) {
            int pick = JOptionPane.showOptionDialog(frame, "Pick a color for the wild card", "Pick Color",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Blue", "Red", "Yellow", "Green"}, null);
            String text = pick == 1 ? "RED": pick == 2 ? "YELLOW": pick == 3 ? "GREEN": "BLUE";
            main.sendMessageToServer("wild#" + text);
        }
    }

    /**
     * Takes the card and returns a string representation for sending it over the network
     * @param card The card to convert
     * @return The string representation of the card
     */
    private static String cardToString(Card card) {
        return card.type + "#" + card.color + "#" + card.number;
    }

}
