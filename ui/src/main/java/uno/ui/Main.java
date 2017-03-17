package uno.ui;

import uno.network.api.IClientMessageListener;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.api.Pair;
import uno.network.client.NetworkClient;
import uno.ui.screens.ErrorScreen;
import uno.ui.screens.IScreen;
import uno.ui.screens.PlayArea;
import uno.ui.screens.ScreenInstances;
import uno.ui.sound.MediaPlayer;

import java.util.ArrayList;

/**
 * Main handles messages to and from the server and sets the screen
 * @author Gustaf Järgren
 * @version 2017-03-03
 */
public class Main implements IClientMessageListener {

    private IScreen currentScreen;
    public NetworkClient networkClient;

    public Main() {
        networkClient = new NetworkClient(this, "gorymoon.se", 55333);
        setScreen(ScreenInstances.getMainMenu());
        (new Thread(new MediaPlayer("sound/EpicSaxGuy.mp3"))).start();
    }

    /**
     * Sets the screen to the provided screen
     * @param screen The screen to change to
     */
    public void setScreen(IScreen screen) {
        if (currentScreen != null)
            currentScreen.hide();
        currentScreen = screen;
        currentScreen.setMain(this);
        currentScreen.show();
    }

    /**
     * The main entry point into the ui, setups a new Main instance
     * @param args The arguments provided for the program
     */
    public static void main (String[] args) {
        new Main();

    }

    @Override
    public void onConnect() {
        setScreen(ScreenInstances.getGameLobby());
    }

    @Override
    public void onDisconnect(String s) {
        if (!(currentScreen instanceof ErrorScreen))
            setScreen(ScreenInstances.getMainMenu());
    }

    @Override
    public void onError(MessageType messageType, String s) {
        System.out.println(s);
        ScreenInstances.getErrorScreen().error = s;
        setScreen(ScreenInstances.getErrorScreen());
    }

    @Override
    public void onMessageReceived(MessageType messageType, Object o) {
        System.out.println(o);
        if (o instanceof String) {
            if (((String) o).startsWith("currentplayer")) {
                ScreenInstances.getGameLobby().setPlayers(Integer.parseInt(((String) o).substring("currentplayer#".length())));
            }

            if (o.equals("game-started")) {
                setScreen(ScreenInstances.getPlayArea());
                return;
            }

            if (o.equals("game-over")) {
                setScreen(ScreenInstances.getGameFinished());
            }
        }

        if (o instanceof Pair) {
            if (((Pair) o).getKey().equals("win-list")) {
                ScreenInstances.getGameFinished().setWinList((ArrayList<Pair<String, Integer>>) ((Pair) o).getValue());
                networkClient.disconnect();
            }
        }

        if (currentScreen instanceof PlayArea) {
            ((PlayArea) currentScreen).handleMessage(o);
        }
    }

    /**
     * Sends a message to the server
     * @param message The message to send to the server
     */
    public void sendMessageToServer(Object message) {
        networkClient.sendToServer(new Packet(MessageType.MESSAGE, message));
    }
}
