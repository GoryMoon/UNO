package uno.client.cmd;

import javafx.util.Pair;
import uno.client.cmd.pages.ErrorPage;
import uno.client.cmd.pages.Page;
import uno.client.cmd.pages.PagesSingletons;
import uno.network.api.IClientMessageListener;
import uno.network.api.MessageType;
import uno.network.api.Packet;
import uno.network.client.NetworkClient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public class GameClient implements IClientMessageListener {

    public NetworkClient networkClient;
    private boolean running = true;
    public Scanner scanner;
    private final Queue<Runnable> scheduledTasks = new ArrayDeque<>();
    private Thread unoThread = Thread.currentThread();
    private Thread pageThread;
    private Page currentPage;

    public static boolean DEBUG = false;

    public GameClient(String hostname, int port) {
        networkClient = new NetworkClient(this, hostname, port);
        scanner = new Scanner(System.in);
        run();
    }

    private void run() {
        pageThread = new Thread("Page Thread") {
            @Override
            public void run() {
                changePage(PagesSingletons.getMainMenu());
                while (running) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {}
                    if (currentPage.running) {
                        currentPage.show(GameClient.this);
                    }
                }
            }
        };
        addScheduledTask(() -> pageThread.start());

        while(running) {
            handleTasks();
        }

    }

    private synchronized void handleTasks() {
        synchronized (this.scheduledTasks)
        {
            while (this.scheduledTasks.isEmpty()) {
                try {
                    scheduledTasks.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (!this.scheduledTasks.isEmpty())
            {
                this.scheduledTasks.poll().run();
            }
        }
    }

    public int nextInt() {
        while(!scanner.hasNextInt()) {
            scanner.next();
            System.out.println("Please only enter numbers");
        }
        return scanner.nextInt();
    }

    public void changePage(Page newPage) {
        if (currentPage != null)
            currentPage.hide();
        currentPage = newPage;
        currentPage.setup(this);
        currentPage.running = true;
    }

    public boolean isCallingFromUNOThread() {
        return Thread.currentThread() == this.unoThread;
    }

    public void addScheduledTask(Runnable runnable) {
        if (isCallingFromUNOThread()) {
            runnable.run();
        } else {
            synchronized (scheduledTasks) {
                scheduledTasks.add(runnable);
                scheduledTasks.notify();
            }
        }
    }

    @Override
    public void onConnect() {
        changePage(PagesSingletons.getGameLobby());
    }

    @Override
    public void onDisconnect(String reason) {
        System.out.println(reason);
        changePage(PagesSingletons.getMainMenu());
    }

    @Override
    public void onError(MessageType type, String message) {
        ErrorPage page = PagesSingletons.getErrorPage();
        page.setError(message);
        changePage(page);
    }

    @Override
    public void onMessageReceived(MessageType type, Object message) {
        if (GameClient.DEBUG) System.out.println(message);
        if (message instanceof String) {
            if (type.equals(MessageType.PLAYER_JOINED)) {
                currentPage.printMessage(this);
            }

            if (type.equals(MessageType.PLAYER_LEFT)) {
                currentPage.printMessage(this);
            }

            if (((String) message).startsWith("currentplayer")) {
                PagesSingletons.getGameLobby().currentPlayers = Integer.parseInt(((String) message).substring("currentplayer#".length()));
                return;
            }

            if (message.equals("game-started")) {
                changePage(PagesSingletons.getGamePage());
                return;
            }

            if (message.equals("game-over")) {
                PagesSingletons.getGamePage().running = false;
            }
        }

        if (message instanceof Pair) {
            if (((Pair) message).getKey().equals("win-list")) {
                ArrayList<Pair<String, Integer>> winList = (ArrayList<Pair<String, Integer>>) ((Pair) message).getValue();
                PagesSingletons.getGameStats().winList = winList;
                PagesSingletons.getGamePage().stopGame();
                changePage(PagesSingletons.getGameStats());
            }
        }

        currentPage.handleMessage(message);
    }

    public void sendMessageToServer(Object message) {
        networkClient.sendToServer(new Packet(MessageType.MESSAGE, message));
    }
}
