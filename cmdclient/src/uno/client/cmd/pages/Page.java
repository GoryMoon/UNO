package uno.client.cmd.pages;

import uno.client.cmd.GameClient;

/**
 * An abstract page for showing info and taking input from the user
 */
public abstract class Page {


    public boolean running = true;

    /**
     * Runs the page<br/>
     * Prints message and takes an input if wanted and handles it
     * @param client The client that shows the page
     */
    public void show(GameClient client) {
        printMessage(client);

        if (wantInput()) {
            int in;
            try {
                in = client.nextInt();
            } catch (IndexOutOfBoundsException e) {
                running = false;
                return;
            }
            handleInput(client, in);
        }
    }

    /**
     * Stops showing the page
     */
    public void hide() {
        running = false;
    }

    /**
     * @return true if input want's to be inputted, false if no input is wanted
     */
    protected boolean wantInput() {
        return true;
    }

    /**
     * A pass through of a received message
     * @param o The message received
     */
    public void handleMessage(Object o) {}

    /**
     *
     * @param client
     */
    public void setup(GameClient client) {}

    /**
     * Should print the message to show for the page
     * @param client The client that is showing the page
     */
    public abstract void printMessage(GameClient client);

    /**
     * Handles the input form the user if it's wanted
     * @param client The client showing the page
     * @param in The input from the user
     */
    abstract void handleInput(GameClient client, int in);
}
