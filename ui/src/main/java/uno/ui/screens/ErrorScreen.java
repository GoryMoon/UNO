package uno.ui.screens;

import uno.ui.Main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kungen on 2017-02-14.
 */
public class ErrorScreen implements IScreen{
    private JFrame frame;
    private Main main;

    public String error;

    public ErrorScreen() {

    }

    @Override
    public void show() {
        frame = new JFrame("UNO Error");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));
        frame.setPreferredSize(new Dimension(800, 500));

        JLabel background = new JLabel(new ImageIcon(Main.class.getResource("assets/backgrounds/background3.jpg")));
        background.setLayout(new FlowLayout());
        contentPane.add(background);

        JLabel errorLabel = new JLabel(error);
        background.add(errorLabel);

        JButton LeaveButton = new JButton("leave");

        LeaveButton.addActionListener(e -> {
            MainMenu menu = new MainMenu();
            main.setScreen(menu);
        });

        background.add(LeaveButton);

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
