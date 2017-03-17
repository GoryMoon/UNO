package uno.ui.screens;

import uno.ui.Main;

/**
 * Base interface for different windows, used to simplify changing windows and<br>
 * have a standard in how screens are shown
 * @author Betina Andersson &amp; Shahad Naji
 * @version 2017-03-03
 */
public interface IScreen {

    /**
     * Starts showing the screen, only called when a window is changed to.<br>
     * Setup the window and start showing it here.
     */
    void show();

    /**
     * Hides the window from showing.<br>
     * The window should either be closed or hidden here as a new screen is about to be shown.<br>
     */
    void hide();

    /**
     * Provides a reference to the Main instance that is showing the screen.
     * @param main The Main instance showing the screen
     */
    void setMain(Main main);

}
