package uno.ui;

import uno.ui.screens.IScreen;
import uno.ui.screens.MainMenu;
import uno.ui.screens.PlayArea;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SettingsOverlay implements IScreen {
    private Main main;
    private JFrame frame;
    private JButton resumeButton;
    private JButton settingButton;
    private JButton quitButton;

    public SettingsOverlay() {

    }

    @Override
    public void show() {

        frame = new JFrame("Pause Game");
        frame.setLayout(new FlowLayout());
      //  frame.setGlassPane();
       // frame.setUndecorated(true);
     //   frame.setAlwaysOnTop(true);

        JLabel pauseLabel = new JLabel(new ImageIcon("resources/backgrounds/unoPause.jpg"));
        pauseLabel.setLayout(new FlowLayout());
        frame.add(pauseLabel);

        resumeButton = new JButton("Resume game");
        // resumeButton.setLayout(new FlowLayout());
        pauseLabel.add(resumeButton);
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e ){
                PlayArea area = new PlayArea();
                main.setScreen(area);
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
    @Override
    public void hide() {
        this.frame.setVisible(false);
    }
    @Override
    public void setMain(Main main) {
        this.main = main;
    }
    @Override
    public void back() {

    }

    }
