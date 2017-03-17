package uno.ui;

import uno.ui.screens.ScreenInstances;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * A popup window that shows options to quit or resume game.
 * @author Betina Andersson &amp; Shahad Naji
 * @version 2017-03-03
 */
public class PauseOverlay {

    private JFrame frame;
    private Main main;

    /**
     * Shows the popup in the middle of the screen
     */
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

    /**
     * Sets the Main instance reference to be accessed in this class
     * @param main The main instance to relate this to
     */
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     * Closes the popup window
     */
    private void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

}
