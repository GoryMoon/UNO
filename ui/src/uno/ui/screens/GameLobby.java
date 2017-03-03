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

    public GameLobby() {}

    @Override
    public void show() {
        frame = new JFrame("UNO Game Lobby");
        frame.setLayout(new BorderLayout());
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));
        frame.setPreferredSize(new Dimension(800, 500));

        JLabel background = new JLabel(new ImageIcon("resources/backgrounds/backgroundGL.png"));
        background.setLayout(new FlowLayout());
        contentPane.add(background);

        players = new JLabel("current players: 0");
        background.add(players);

        JButton leaveButton = new JButton("leave");

        playGameButton = new JButton("Play Game");
        playGameButton.setVisible(false);
        playGameButton.addActionListener(e -> {
            main.sendMessageToServer("start-game");
        });

        leaveButton.addActionListener(e -> {
            MainMenu menu = new MainMenu();
            main.setScreen(menu);
        });

        background.add(leaveButton);
        background.add(playGameButton);

        frame.pack();
        frame.setVisible(true);
    }

    public void setPlayers(int x) {
        if (x == 1) {
            playGameButton.setVisible(true);
        }
        players.setText("current players: " + x);

        frame.pack();
    }


    @Override
    public void hide() {
        frame.setVisible(false);
    }

    @Override
    public void back() {

    }

    @Override
    public void setMain(Main main) {
        this.main = main;
    }
}
