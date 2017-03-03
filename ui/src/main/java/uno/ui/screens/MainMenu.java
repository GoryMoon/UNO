package uno.ui.screens;

import uno.ui.Main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kungen on 2017-02-14.
 */
public class MainMenu implements IScreen {

    private JFrame frame;
    private Main main;

    @Override
    public void show() {
        frame = new JFrame("Uno Main Menu");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(6, 6));
        frame.setPreferredSize(new Dimension(800, 500));

        JLabel background = new JLabel(new ImageIcon(Main.class.getResource("assets/backgrounds/uno.png")));
        background.setLayout(new FlowLayout());

        JButton createButton = new JButton("Create game");
        createButton.setEnabled(false); //Disabled
        createButton.setToolTipText("Disabled, join game auto makes first to host");
        createButton.addActionListener(e -> {
            GameLobby lobby = new GameLobby();
            main.setScreen(lobby);
        });

        JButton joinButton = new JButton("Join game");
        joinButton.addActionListener(e -> main.networkClient.connect());

        JButton settingsButton = new JButton(new ImageIcon(Main.class.getResource("assets/setting.png")));
        settingsButton.setVisible(false); //Disabled
        settingsButton.setOpaque(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);
        settingsButton.addActionListener(e -> {
            ScreenInstances.getErrorScreen().error = "Not implemented";
            main.setScreen(ScreenInstances.getErrorScreen());
        });

        frame.add(background);
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
    public void setMain(Main main) {
        this.main = main;
    }
}
