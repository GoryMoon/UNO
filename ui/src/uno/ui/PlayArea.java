package uno.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;


public class PlayArea implements IScreen {
    private JFrame frame;
    private Main main;
    private JPanel panel1;
    private JButton drawCard;
    private JButton cardPile;
   // private JButton stampe = new JButton(new ImageIcon("resources/unobak.png"));

    @Override
    public void show() {
        frame = new JFrame("UNO uno.ui.PlayArea");
        frame.setLayout(new BorderLayout());
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(6, 6));
        frame.setPreferredSize(new Dimension(1000, 700));

        JLabel background = new JLabel(new ImageIcon("resources/backgrounds/background3.jpg"));
        background.setLayout(new BorderLayout());
        contentPane.add(background);

        panel1  = new JPanel ();
       background.add(panel1, BorderLayout.SOUTH);
        //panel1.setLayout(new FlowLayout());
        panel1.setLayout(new GridLayout(2, 8, 2, 2));

        JButton pause =  new JButton(new ImageIcon ("resources/setting.png") );
        pause.setOpaque(false);
        pause.setContentAreaFilled(false);
        pause.setBorderPainted(false);
        background.add(pause, BorderLayout.EAST);
        pause.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e ){
                SettingsOverlay paus = new SettingsOverlay();
                main.setScreen(paus);
            }
        });

        drawCard = new JButton(new ImageIcon("resources/unobak.png"));
        drawCard.setOpaque(false);
        drawCard.setContentAreaFilled(false);
        drawCard.setBorderPainted(false);

        background.add(drawCard, BorderLayout.WEST);
        drawCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton card2 = new JButton(new ImageIcon("resources/cards/wildred.png"));
                panel1.add(card2);
                frame.pack();  //måste forma om när man har ändrat.
            }
        });

        JButton card1 = new JButton(new ImageIcon("resources/cards/0blue.png"));
        panel1.add(card1);
        card1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardPile.add(card1);
               // card1.setVisible(false);
                System.out.println("card 1 is removed");

            }

        });

        String cardAdress = "resources/cards/";
        JButton drawCard2 = new JButton("drawCard2");
        background.add(drawCard2, BorderLayout.NORTH);
        drawCard2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double j = 0 + (Math.random() * 9-0);
                int i = (int) j;
                   if(i < 9) {
                       JButton card3 = new JButton(new ImageIcon(cardAdress + i + "yellow.png"));
                       panel1.add(card3);
                       frame.pack();
                       System.out.println(i);
                   }
             }
        });

        JButton unoKnapp = new JButton(new ImageIcon("resources/unoknapp.png"));
        panel1.add(unoKnapp);


        cardPile = new JButton();
        cardPile.setOpaque(false);
        cardPile.setContentAreaFilled(false);
        cardPile.setBorderPainted(false);
        //cardPile.add(stampe);
        background.add(cardPile, BorderLayout.CENTER);


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
