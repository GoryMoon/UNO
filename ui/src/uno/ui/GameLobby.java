package uno.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kungen on 2017-02-16.
 */
public class GameLobby implements IScreen {
    private Main main;
    private JFrame frame;

    public GameLobby() {}

    @Override
    public void show() {
        frame = new JFrame("UNO Game Lobby");
        frame.setLayout(new BorderLayout());
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));

        JLabel background = new JLabel(new ImageIcon("resources/backgrounds/backgroundGL.png"));
        background.setLayout(new FlowLayout());
        contentPane.add(background);

        JButton LeaveButton = new JButton("leave");

        JButton PlayGameButton = new JButton("Play Game");
        PlayGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlayArea play = new PlayArea();
                main.setScreen(play);
            }
        });

        LeaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu menu = new MainMenu();
                main.setScreen(menu);
            }
        });

        background.add(LeaveButton);
        background.add(PlayGameButton);

        frame.pack();
        frame.setVisible(true);
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
