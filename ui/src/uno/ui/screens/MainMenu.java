package uno.ui.screens;

import uno.ui.Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Created by Kungen on 2017-02-14.
 */
public class MainMenu implements IScreen {
    private JFrame frame;
    private ImageIcon imageicon;

    private Main main;

    public MainMenu() {
    }

    @Override
    public void show() {
        frame = new JFrame("Uno uno.ui.Main Menu");

        frame.setLayout(new BorderLayout());
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));
        frame.setPreferredSize(new Dimension(800, 500));

        JLabel background = new JLabel(new ImageIcon("resources/backgrounds/uno.png"));
        background.setLayout(new FlowLayout());
        contentPane.add(background);

        JButton createButton = new JButton("create game");
        createButton.setEnabled(false);
        JButton joinButton = new JButton("join game");
        JButton settingsButton = new JButton(new ImageIcon("resources/setting.png"));

        createButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            GameLobby lobby = new GameLobby();
            main.setScreen(lobby);
        }
    });

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.networkClient.connect();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ErrorScreen error = new ErrorScreen();
                main.setScreen(error);
            }
        });

        settingsButton.setOpaque(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);

        background.add(createButton);
        background.add(joinButton);
        background.add(settingsButton, BorderLayout.NORTH);



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
