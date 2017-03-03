package uno.ui.screens;

import uno.ui.Main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kungen on 2017-02-16.
 */
public class GameLobby implements IScreen {
    private Main main;
    private JFrame frame;
    private JLabel players;
    private JButton playGameButton;
    private boolean host;
    private int currentPlayers;

    public GameLobby() {}

    @Override
    public void show() {
        frame = new JFrame("UNO Game Lobby");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));
        frame.setPreferredSize(new Dimension(800, 500));

        JLabel background = new JLabel(new ImageIcon(Main.class.getResource("assets/backgrounds/backgroundGL.png")));
        background.setLayout(new FlowLayout());
        contentPane.add(background);

        players = new JLabel("current players: " + currentPlayers);
        background.add(players);

        JButton leaveButton = new JButton("leave");

        playGameButton = new JButton("Play Game");
        if (!host)
            playGameButton.setVisible(false);
        playGameButton.addActionListener(e -> {
            main.sendMessageToServer("start-game");
        });

        leaveButton.addActionListener(e -> {
            main.networkClient.disconnect();
        });

        background.add(leaveButton);
        background.add(playGameButton);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setPlayers(int x) {
        if (x == 1) {
            host = true;
            if (playGameButton != null)
                playGameButton.setVisible(true);
        }

        if (players != null)
            players.setText("current players: " + x);
        else
            currentPlayers = x;

        if (frame != null)
            frame.pack();
    }


    @Override
    public void hide() {
        frame.setVisible(false);
        host = false;
    }

    @Override
    public void back() {

    }

    @Override
    public void setMain(Main main) {
        this.main = main;
    }
}
