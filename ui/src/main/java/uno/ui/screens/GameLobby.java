package uno.ui.screens;

import uno.ui.Main;

import javax.swing.*;
import java.awt.*;

/**
 * This class displays the amount of players who joined the game and lets the first to join start the game
 * @author Betina Andersson &amp; Shahad Naji
 * @version 2017-03-03
 */
public class GameLobby implements IScreen {

    private JFrame frame;
    private Main main;
    private JLabel players;
    private JButton playGameButton;
    private boolean host;
    private int currentPlayers;

    @Override
    public void show() {
        frame = new JFrame("UNO Game Lobby");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(6, 6));
        frame.setPreferredSize(new Dimension(800, 500));

        JLabel background = new JLabel(new ImageIcon(Main.class.getResource("assets/backgrounds/backgroundGL.png")));
        background.setLayout(new FlowLayout());

        players = new JLabel("Current players: " + currentPlayers);

        playGameButton = new JButton("Start Game");
        if (!host)
            playGameButton.setVisible(false);
        playGameButton.addActionListener(e -> {
            main.sendMessageToServer("start-game");
        });

        JButton leaveButton = new JButton("Leave");
        leaveButton.addActionListener(e -> {
            main.networkClient.disconnect();
        });

        frame.add(background);
        background.add(players);
        background.add(leaveButton);
        background.add(playGameButton);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void hide() {
        frame.setVisible(false);
        host = false;
    }

    @Override
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     * Sets the amount of players text displayed in the window<br>
     * If the window isn't setup a local variable is set for it to use when setting up.
     * @param count The amount of players to show
     */
    public void setPlayers(int count) {
        if (count == 1) {
            host = true;
            if (playGameButton != null)
                playGameButton.setVisible(true);
        }

        if (players != null)
            players.setText("Current players: " + count);
        currentPlayers = count;

        if (frame != null)
            frame.pack();
    }
}
