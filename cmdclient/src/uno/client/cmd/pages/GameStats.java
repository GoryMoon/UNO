package uno.client.cmd.pages;

import javafx.util.Pair;
import uno.client.cmd.GameClient;

import java.util.ArrayList;

public class GameStats extends Page {

    public ArrayList<Pair<String, Integer>> winList;

    @Override
    public void printMessage(GameClient client) {
        System.out.println("\n\n\n\n\n\n**********************************\nGame over\n# Playername (cards left)");
        for (int i = 0; i < winList.size(); i++) {
            System.out.println(i + ". " + winList.get(i).getKey() + "(" + winList.get(i).getValue() + ")");
        }
        System.out.println("\n\n1.Exit to menu\nPick choice:");
    }

    @Override
    void handleInput(GameClient client, int in) {
        if (in == 1) {
            client.changePage(PagesSingletons.getMainMenu());
            running = false;
        }
    }
}
