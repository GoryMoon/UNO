package uno.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PlayArea implements IScreen {
    private JFrame frame;
    private Main main;
    private JPanel panel1;
    private JButton drawCard;

    @Override
    public void show() {
        frame = new JFrame("UNO uno.ui.PlayArea");
        frame.setLayout(new BorderLayout());
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));

        JLabel background = new JLabel(new ImageIcon("resources/backgrounds/background3.jpg"));
        background.setLayout(new BorderLayout());
        contentPane.add(background);

        panel1  = new JPanel ();
        background.add(panel1, BorderLayout.SOUTH);
        int amountofcards = 2;
        panel1.setLayout(new GridLayout(1, amountofcards));


        panel1.setPreferredSize(new Dimension(amountofcards*10, 150));


        drawCard = new JButton("draw card");
        background.add(drawCard);
        drawCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton card2 = new JButton(new ImageIcon("resources/cards/2green.png"));
                panel1.add(card2);
                frame.pack();  //måste forma om när man har ändrat.
            }
        });

        JButton card1 = new JButton(new ImageIcon("resources/cards/0blue.png"));
        panel1.add(card1);

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
