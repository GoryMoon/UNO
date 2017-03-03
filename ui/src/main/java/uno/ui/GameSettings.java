package uno.ui;

import uno.ui.screens.IScreen;

import javax.swing.*;

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