package uno.ui;

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
        frame = new JFrame("Uno uno.ui.Main Manu");

        frame.setLayout(new BorderLayout());
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));
        frame.setPreferredSize(new Dimension(800, 500));

        JLabel background = new JLabel(new ImageIcon("resources/backgrounds/background3.jpg"));
        background.setLayout(new FlowLayout());
        contentPane.add(background);

        JButton CreateButton = new JButton("create game");
        JButton JoinButton = new JButton("join game");
        JButton SettingsButton = new JButton(new ImageIcon("resources/setting.png"));

        CreateButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            GameLobby lobby = new GameLobby();
            main.setScreen(lobby);
        }
    });

        JoinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameLobby lobby = new GameLobby();
                main.setScreen(lobby);
            }
        });

        SettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ErrorScreen error = new ErrorScreen();
                main.setScreen(error);
            }
        });

        SettingsButton.setOpaque(false);
        SettingsButton.setContentAreaFilled(false);
        SettingsButton.setBorderPainted(false);

        background.add(CreateButton);
        background.add(JoinButton);
        background.add(SettingsButton, BorderLayout.NORTH);



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
