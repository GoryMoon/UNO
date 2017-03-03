package uno.ui.screens;

import uno.ui.Main;

import javax.swing.*;
import java.awt.*;

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
        frame = new JFrame("Uno Main Menu");

        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));
        frame.setPreferredSize(new Dimension(800, 500));

        JLabel background = new JLabel(new ImageIcon(Main.class.getResource("assets/backgrounds/uno.png")));
        background.setLayout(new FlowLayout());
        contentPane.add(background);

        JButton createButton = new JButton("create game");
        createButton.setEnabled(false);
        JButton joinButton = new JButton("join game");
        JButton settingsButton = new JButton(new ImageIcon(Main.class.getResource("assets/setting.png")));
        settingsButton.setVisible(false);

        createButton.addActionListener(e -> {
            GameLobby lobby = new GameLobby();
            main.setScreen(lobby);
        });

        joinButton.addActionListener(e -> main.networkClient.connect());

        settingsButton.addActionListener(e -> {
            ErrorScreen error = new ErrorScreen();
            main.setScreen(error);
        });

        settingsButton.setOpaque(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);

        background.add(createButton);
        background.add(joinButton);
        background.add(settingsButton, BorderLayout.NORTH);



        frame.pack();
        frame.setLocationRelativeTo(null);
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
