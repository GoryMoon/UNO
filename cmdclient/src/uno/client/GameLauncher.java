package uno.client;

import uno.client.cmd.GameClient;

public class GameLauncher {

    public static void main(String[] args) {
        String hostname;
        int port = 55333;

        if (args.length > 0) {
            hostname = args[0];

            if (args.length > 1) {
                port = Integer.parseInt(args[1]);

                if (args.length > 2 && args[2].equals("debug")) {
                    GameClient.DEBUG = true;
                }
            }
        } else {
            System.out.println("Usage: java -jar client.jar <hostname> [port=55333] [debug]");
            return;
        }

        new GameClient(hostname, port);
    }

}
