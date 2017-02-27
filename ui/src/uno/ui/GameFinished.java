package uno.ui;

import uno.ui.IScreen;
import uno.ui.Main;
import uno.ui.MainMenu;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;


public class GameFinished implements IScreen {
    JFrame frame;
    JButton backButton;
    private Main main;

    public GameFinished() {

    }

    public void show() {
        makeFrame();
    }

    public void hide() {
        this.frame.setVisible(false);
    }

    private void makeFrame() {

        frame = new JFrame("Game finished");
        frame.setLayout(new BorderLayout());

        JLabel gameLabel = new JLabel(new ImageIcon("finished.jpg"));
        gameLabel.setLayout(new FlowLayout());
        frame.add(gameLabel);
        frame.setPreferredSize(new Dimension(800, 500));

        backButton = new JButton("Back to menu");
        gameLabel.add(backButton);
        backButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu menu = new MainMenu();
                main.setScreen(menu);
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void back() {

    }

}

