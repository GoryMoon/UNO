package uno.logic;

import java.util.ArrayList;

public class GameCore {
	

	private Deck deck;
	private ArrayList<Player> players;
	private int currentPlayerIndex;
	private int[] skippedPlayers;
	
	public GameCore() {
		deck = new Deck();
		players = new ArrayList<Player>();
		
	}
	public void setupGame() {
		for(int i = 1; i <= 4; i++) {
			players.add(new Player(("p"+i),deck));
		}
	}
}
