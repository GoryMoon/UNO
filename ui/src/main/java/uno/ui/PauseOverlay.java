package uno.ui;

import uno.ui.screens.ScreenInstances;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class PauseOverlay {

    private JFrame frame;
    private Main main;

    public void show() {
        frame = new JFrame("Pause Game");
        frame.setLayout(new FlowLayout());
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);

        JLabel pauseLabel = new JLabel(new ImageIcon(Main.class.getResource("assets/backgrounds/unoPause.jpg")));
        pauseLabel.setLayout(new FlowLayout());

        JButton resumeButton = new JButton("Resume game");
        resumeButton.addActionListener(e -> close());

        JButton quitButton = new JButton("Quit game");
        quitButton.addActionListener(e -> {
            main.setScreen(ScreenInstances.getMainMenu());
            main.networkClient.disconnect();
            close();
        });

        JButton settingButton = new JButton("Game settings");
        settingButton.addActionListener(e -> {
            //Go to settings window
        });
        settingButton.setVisible(false); //Disabled

        frame.add(pauseLabel);
        pauseLabel.add(resumeButton);
        pauseLabel.add(quitButton);
        pauseLabel.add(settingButton);

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
