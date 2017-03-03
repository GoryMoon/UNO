package uno.ui.screens;

import uno.ui.Main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kungen on 2017-02-16.
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

    public void setPlayers(int count) {
        if (count == 1) {
            host = true;
            if (playGameButton != null)
                playGameButton.setVisible(true);
        }

        if (players != null)
            players.setText("current players: " + count);
        else
            currentPlayers = count;

        if (frame != null)
            frame.pack();
    }
}
