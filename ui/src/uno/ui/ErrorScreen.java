package uno.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Created by Kungen on 2017-02-14.
 */
public class ErrorScreen implements IScreen{
    private JFrame frame;
    private Main main;

    public ErrorScreen() {

    }

    @Override
    public void show() {
        frame = new JFrame("UNO uno.ui.ErrorScreen");
        frame.setLayout(new BorderLayout());
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));

        JLabel background = new JLabel(new ImageIcon("resources/backgrounds/background3.jpg"));
        background.setLayout(new FlowLayout());
        contentPane.add(background);

        JButton LeaveButton = new JButton("leave");

        LeaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu menu = new MainMenu();
                main.setScreen(menu);
            }
        });

        background.add(LeaveButton);

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
