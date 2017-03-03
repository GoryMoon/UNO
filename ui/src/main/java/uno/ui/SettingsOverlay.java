package uno.ui;

import uno.ui.screens.IScreen;
import uno.ui.screens.MainMenu;
import uno.ui.screens.PlayArea;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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

        JLabel pauseLabel = new JLabel(new ImageIcon(Main.class.getResource("assets/backgrounds/unoPause.jpg")));
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
        settingButton.setVisible(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    private void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

}
