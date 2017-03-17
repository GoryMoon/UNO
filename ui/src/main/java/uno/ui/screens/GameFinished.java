package uno.ui.screens;

import uno.network.api.Pair;
import uno.ui.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Displays the order that the players got in when the game is over
 * @author Betina Andersson &amp; Shahad Naji
 * @version 2017-03-03
 */
public class GameFinished implements IScreen {

    private JFrame frame;
    private Main main;
    private JPanel winListPanel;

    @Override
    public void show() {
        frame = new JFrame("Game finished");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 500));

        JLabel background = new JLabel(new ImageIcon(Main.class.getResource("assets/backgrounds/finished.jpg")));
        background.setLayout(new FlowLayout(FlowLayout.CENTER));

        //Made transparent
        winListPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                Rectangle r = g.getClipBounds();
                g.fillRect(r.x, r.y, r.width, r.height);
                super.paintComponent(g);
            }
        };
        winListPanel.setBackground(new Color(255, 114, 49, 156));
        winListPanel.setLayout(new BoxLayout(winListPanel, BoxLayout.Y_AXIS));
        winListPanel.setOpaque(false);

        JButton backButton = new JButton("Back to menu");
        backButton.addActionListener(e -> main.setScreen(ScreenInstances.getMainMenu()));

        frame.add(background);
        background.add(winListPanel);
        winListPanel.add(backButton);

        frame.pack();
        frame.setLocationRelativeTo(null);
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

    /**
     * Creates labels with the players names, positions and cards left at end of game to display
     * @param winList is an list of the players names and the number of cards left
     */
    public void setWinList(ArrayList<Pair<String, Integer>> winList) {
        winListPanel.add(new JLabel(""));
        winListPanel.add(new JLabel(""));
        JLabel info = new JLabel("Position   Player Name          Cards Left");
        info.setFont(new Font(info.getFont().getName(), info.getFont().getStyle(), 18));
        winListPanel.add(info);
        for (int i = 0; i < winList.size(); i++) {
            JLabel entry = new JLabel("#" + (i + 1) + "            " + winList.get(i).getKey() + "                " + winList.get(i).getValue());
            entry.setFont(new Font(info.getFont().getName(), info.getFont().getStyle(), 18));
            winListPanel.add(entry);
        }
        winListPanel.repaint();
        frame.pack();
    }
}

