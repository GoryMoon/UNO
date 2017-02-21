package uno.ui;

import uno.ui.IScreen;
import uno.ui.Main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;

public class SettingsOverlay implements IScreen {
    private Main main;
    JFrame frame;
    JButton resumeButton;
    JButton settingButton;
    JButton quitButton;

    public SettingsOverlay() {

    }
    @Override
    public void show() {
        makeFrame();
    }
    @Override
    public void hide() {
        this.frame.setVisible(false);
    }


    public void makeFrame() {

        frame = new JFrame("Pause Game");
        frame.setLayout(new FlowLayout());

        JLabel pauseLabel = new JLabel(new ImageIcon("unoPause.jpg"));
        pauseLabel.setLayout(new FlowLayout());
        frame.add(pauseLabel);

        resumeButton = new JButton("Resume game");
       // resumeButton.setLayout(new FlowLayout());
        pauseLabel.add(resumeButton);
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e ){
                //Go to playArea window
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
    public void setMain(Main main) {
        this.main = main;
    }
    @Override
    public void back() {

    }

    }
