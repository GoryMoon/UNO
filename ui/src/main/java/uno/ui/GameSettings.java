package uno.ui;

import uno.ui.screens.IScreen;

import javax.swing.*;

/**
 * Not used in the final product, left in because of reminder to make in future
 * @author Betina Andersson &amp; Shahad Naji
 * @version 2017-03-03
 */
public class GameSettings implements IScreen {

    private Main main;
    JFrame frame;
    JButton button;

    public GameSettings() {
    }

    @Override
    public void show() {

    }
    @Override
    public void hide() {
        this.frame.setVisible(false);

    }

    @Override
    public void setMain(Main main) {
        this.main = main;
    }

}