package uno.ui; /**
 * Created by Kungen on 2017-02-12.
 */

public class Main {

    private IScreen currentScreen;

    public Main() {
        MainMenu menu = new MainMenu();
        //uno.ui.ErrorScreen error = new uno.ui.ErrorScreen();
        setScreen(menu);
        //setScreen(error);
    }

    public void setScreen(IScreen screen) {
        if (currentScreen != null)
            currentScreen.hide();
        currentScreen = screen;
        currentScreen.setMain(this);
        currentScreen.show();
    }

    public static void main (String[] args) {
        new Main();

    }
}
