package uno.ui;

import javafx.util.Pair;
import uno.network.api.IClientMessageListener;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.client.NetworkClient;
import uno.ui.screens.ErrorScreen;
import uno.ui.screens.IScreen;
import uno.ui.screens.PlayArea;
import uno.ui.screens.ScreenInstances;
import uno.ui.sound.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by Kungen on 2017-02-12.
 */
public class Main implements IClientMessageListener {

    private IScreen currentScreen;
    public NetworkClient networkClient;

    public Main() {
        networkClient = new NetworkClient(this, "gorymoon.se", 55333);
        setScreen(ScreenInstances.getMainMenu());
        (new Thread(new MediaPlayer("sound/EpicSaxGuy.mp3"))).start();
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

    public void sendMessageToServer(Object message) {
        networkClient.sendToServer(new Packet(MessageType.MESSAGE, message));
    }
}
