package uno.ui;

import uno.ui.IScreen;
import uno.ui.Main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;

public class SettingsOverlay {
    private Main main;
    private JFrame frame;
    private JButton resumeButton;
    private JButton settingButton;
    private JButton quitButton;

    public SettingsOverlay() {

    }

    public void show() {

        frame = new JFrame("Pause Game");
        frame.setLayout(new FlowLayout());
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);


        JLabel pauseLabel = new JLabel(new ImageIcon("resources/backgrounds/unoPause.jpg"));
        pauseLabel.setLayout(new FlowLayout());
        frame.add(pauseLabel);

        resumeButton = new JButton("Resume game");
        // resumeButton.setLayout(new FlowLayout());
        pauseLabel.add(resumeButton);
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e ){
                close();
            }
        });

        quitButton = new JButton("Quit game");
        //quitButton.setLayout(new FlowLayout());
        pauseLabel.add(quitButton);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //System.out.println("nej");
                MainMenu menu = new MainMenu();
                main.setScreen(menu);
                close();
            }
        });



        settingButton =  new JButton("Game settings");
        //settingButton.setLayout(new FlowLayout());
        pauseLabel.add(settingButton);
        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                //Go to settings window
            }
        });
        frame.pack();
        frame.setVisible(true);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    private void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

}
