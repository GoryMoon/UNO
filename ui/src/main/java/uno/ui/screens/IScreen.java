package uno.ui.screens;

import uno.ui.Main;

/**
 * Created by Kungen on 2017-02-14.
 */
public interface IScreen {

    void show();
    void hide();
    void back();
    void setMain(Main main);

}
