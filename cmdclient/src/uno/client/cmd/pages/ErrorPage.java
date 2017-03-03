package uno.client.cmd.pages;

import uno.client.cmd.GameClient;

public class ErrorPage extends Page {

    private String error;
    private static final String message = "\n\n\n\n\n\n**********************************\nThe game encountered an error:\n\n\t%s\n\n1.Back to menu\n\nPick choice: ";

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public void printMessage(GameClient client) {
        System.out.println(String.format(message, error));
    }

    @Override
    void handleInput(GameClient client, int in) {
        if (in == 1) {
            client.changePage(PagesSingletons.getMainMenu());
            running = false;
        }
    }
}
