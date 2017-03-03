package uno.ui.screens;

import javafx.util.Pair;
import uno.ui.Main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;


public class GameFinished implements IScreen {
    JFrame frame;
    JButton backButton;
    private Main main;
    private ArrayList<Pair<String, Integer>> winList;
    private JPanel winListPanel;

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
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel gameLabel = new JLabel(new ImageIcon(Main.class.getResource("assets/backgrounds/finished.jpg")));
        gameLabel.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.add(gameLabel);
        frame.setPreferredSize(new Dimension(800, 500));

        winListPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                Rectangle r = g.getClipBounds();
                g.fillRect(r.x, r.y, r.width, r.height);
                super.paintComponent(g);
            }
        };
        winListPanel.setBackground(new Color(255, 114, 49, 119));
        winListPanel.setLayout(new BoxLayout(winListPanel, BoxLayout.Y_AXIS));
        winListPanel.setOpaque(false);

        backButton = new JButton("Back to menu");
        winListPanel.add(backButton);
        backButton.addActionListener(e -> main.setScreen(ScreenInstances.getMainMenu()));

        gameLabel.add(winListPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setWinList(ArrayList<Pair<String, Integer>> winList) {
        this.winList = winList;
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

    public void setMain(Main main) {
        this.main = main;
    }

    public void back() {

    }

}

