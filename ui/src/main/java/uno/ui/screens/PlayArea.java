package uno.ui.screens;

import javafx.util.Pair;
import uno.ui.Card;
import uno.ui.Main;
import uno.ui.PauseOverlay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.UUID;


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
        setTurnText();
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

    private void setTurnText() {
        turnInfo.setText("Current turnInfo is: " + (currentTurn ? "You": getPlayer(currentPlayer).getValue().getKey()));
        frame.pack();
    }

    private void addCard(Card card) {
        JButton cardButton = new JButton(new ImageIcon(Main.class.getResource("assets/cards/" + card.toString() + ".png")));
        cardButton.addActionListener(new CardActionListener(card));
        cardPanel.add(cardButton);
    }

    private void setCards() {
        cardPanel.removeAll();
        cardPanel.repaint();
        cards.forEach(this::addCard);
        frame.pack();
    }

    public void handleMessage(Object o) {
        if (o instanceof ArrayList) {
            if (((ArrayList) o).get(0) instanceof Pair) {
                if (((Pair) ((ArrayList) o).get(0)).getValue() instanceof Pair)
                    players = (ArrayList<Pair<UUID, Pair<String, Boolean>>>) o;
                if (((Pair) ((ArrayList) o).get(0)).getValue() instanceof Integer)
                    cardAmount = (ArrayList<Pair<UUID, Integer>>) o;

                if (players != null && cardAmount != null) {
                    infoPanel.removeAll();
                    double half = Math.ceil(players.size() / 2D);
                    for(int i = 0; i < half; i++){
                        if (!players.get(i).getKey().equals(main.networkClient.getPlayer().getID())) {
                            infoPanel.add(new JLabel(players.get(i).getValue().getKey() + " <" + cardAmount.get(i).getValue() + ">" + (players.get(i).getValue().getValue() ? "Uno": "")));
                        }
                    }
                    infoPanel.add(unoButton);
                    for(int i = 0; i < players.size() - half; i++){
                        int j = (int) (half + i);
                        if (!players.get(j).getKey().equals(main.networkClient.getPlayer().getID())) {
                            infoPanel.add(new JLabel(players.get(j).getValue().getKey() + " <" + cardAmount.get(j).getValue() + ">" + (players.get(j).getValue().getValue() ? "Uno": "")));
                        }
                    }
                    infoPanel.repaint();
                    frame.pack();
                }
            }

            if (((ArrayList) o).get(0) instanceof String) {
                cards = new ArrayList<>();
                if (((ArrayList) o).size() > 0) {
                    ((ArrayList) o).forEach(o1 -> cards.add(new Card(((String) o1).split("#"))));
                    setCards();
                }
            }
        }

        if (o instanceof Pair) {
            if (((Pair) o).getKey().equals("new-round")) {
                currentPlayer = (UUID) ((Pair) o).getValue();
                currentTurn = currentPlayer.equals(main.networkClient.getPlayer().getID());
                setTurnText();
                frame.pack();
            } else if (((Pair) o).getKey().equals("played-card")) {
                Card lastPlayedCard = new Card(((String) ((Pair) o).getValue()).split("#"));
                cardPile.setIcon(new ImageIcon(Main.class.getResource("assets/cards/" + lastPlayedCard.toString() + ".png")));
            }
        }

        if (o instanceof String) {
            if (((String) o).startsWith("request#")) {
                String req = ((String) o).split("#")[1];
                if (req.equals("wild")) {
                    int pick = JOptionPane.showOptionDialog(frame, "Pick a color for the wild card", "Pick Color",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Blue", "Red", "Yellow", "Green"}, null);
                    String text = pick == 1 ? "RED": pick == 2 ? "YELLOW": pick == 3 ? "GREEN": "BLUE";
                    main.sendMessageToServer("wild#" + text);
                }
            }
        }
    }

    private static String cardToString(Card card) {
        return card.type + "#" + card.color + "#" + card.number;
    }

    class CardActionListener implements ActionListener {

        private Card card;

        public CardActionListener(Card card) {
            this.card = card;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentTurn)
                main.sendMessageToServer("play#" + cardToString(card));
        }
    }
}
