package uno.logic;

import java.util.ArrayList;
import java.util.UUID;

import uno.server.core.GameServer;

/**
 * 
 * @author nakhle
 * @version
 */

public class GameCore {
	

	Deck deck;
	private ArrayList<Player> players;
	private int currentPlayerIndex;
	private ArrayList<Integer> skippedPlayers;
	private boolean clockwise;
	private boolean waitingForInput;
    private GameServer gameServer;
	
	public GameCore(GameServer gameServer) {
		deck = new Deck();
		players = new ArrayList<Player>();
		skippedPlayers = new ArrayList<Integer>();
		clockwise = true;
		waitingForInput = false;
		this.gameServer = gameServer;
	}
	
	/** 
	 * Creates a deck, the number of players who are playing, shuffles the deck and draws the first card
	 * @param playerCount Number of players
	 * @param uuids The collection of UUIDs that are to be assigned to the players
	 */
	public void setupGame(int playerCount, ArrayList<UUID> uuids) {
		deck.setupDeck();
		for(int i = 1; i <= playerCount; i++) {
			Player player = new Player(("Player "+i),deck, this, uuids.get(i));
			player.setup();
			players.add(player);
		}
		//deck.shuffle();
		currentPlayerIndex = 0;
		firstDraw();
		//if(currentPlayerIndex == 0 || !clockwise) {
		//	endTurn();
		//}
	}
	
	
	/** 
	 * When the game starts a card is drawn for the other to play against, the first draw also has a special effect
	 */
	public void firstDraw() {
		deck.getPlayedCards().add(0, deck.draw());
		Card firstCard = deck.getPlayedCards().get(0);
		switch(firstCard.type) {
			case DRAW: drawEffect(2); break;
			case NUMBER: break;
			case REVERSE: currentPlayerIndex = 1; reverseEffect(); break;
			case SKIP: skipEffect(); break;
			case WILD: endTurn(); break;
			case WILD_DRAW: deck.mergeDecks(); firstDraw(); break;
		
		}
	}
	
	/**
	 * Plays the card that the player have chosen, if it's allowed it gets added to the playedCards collection, else nothing happens
	 * Also checks if the card has a special effect and plays it
	 * @param card The card object that is to be played
	 */
	public void executeCard(Card card) {
		
		Card temp = deck.getPlayedCards().get(0);
		if((card.color == temp.color) ||  ((card.type == temp.type) && card.number == temp.number) || (card.type == Type.WILD) || (card.type == Type.WILD_DRAW)) {
			deck.getPlayedCards().add(0, card);
			switch(card.type){
				case DRAW: drawEffect(2); break;
				case NUMBER: break;
				case REVERSE: reverseEffect();
				case SKIP: skipEffect(); break;
				case WILD: waitingForInput = true;  break; 
				case WILD_DRAW: waitingForInput = true; drawEffect(4); break;
				
			
			}
			if((players.get(currentPlayerIndex).getCards().size() == 1) && !players.get(currentPlayerIndex).unoStatus()) {
				players.get(currentPlayerIndex).drawCard();
				players.get(currentPlayerIndex).drawCard();
			}
			else if((players.get(currentPlayerIndex).getCards().size() != 1) && players.get(currentPlayerIndex).unoStatus()) {
				players.get(currentPlayerIndex).drawCard();
				players.get(currentPlayerIndex).drawCard();
				players.get(currentPlayerIndex).drawCard();
				players.get(currentPlayerIndex).drawCard();
			}
			
			if(!waitingForInput) {
			//endTurn();
			}
		}
		else{
			return;
		}
		
	}
	
	
	/**
	 * 
	 * Forces the next player to draw 2 or 4 cards depending on which number you send and also adds the next player in the skipped list
	 * count The number of cards to be drawn
	 */
	public void drawEffect(int count) {
		for(int i = 0; i < count; i++){
			players.get(getNextPlayer()).drawCard();
		}
		skipEffect();
	}
	
	
	/**
	 * Reverses the turn order
	 */
	public void reverseEffect() {
		if(players.size() == 2) {
			skipEffect();
		}
		else clockwise = !clockwise;
	}
	
	
	/**
	 * Adds the next player in the skipped list
	 */
	public void skipEffect() {
		skippedPlayers.add(getNextPlayer());
	}
	
	/**
	 * Changes the color of a wild card to a color of the players choice
	 * @param card The card to be affected
	 * @param color The new color to be assigned
	 */
	@SuppressWarnings("incomplete-switch")
	public void wild(Card card, Color color) {
		switch(color){
			case RED: card.color = Color.RED; break;
			case BLUE: card.color = Color.BLUE; break;
			case YELLOW: card.color = Color.YELLOW; break;
			case GREEN: card.color = Color.GREEN; break;
		}
	}
	
	/**
	 * 
	 * Check who is the next player depending on if the current turn order is clockwise or not and returns the index number for who's next
	 */
	public int getNextPlayer() {
		if(clockwise) {
			if(currentPlayerIndex+1 > players.size()) {
				return 0;
			}
			return currentPlayerIndex+1;
		}
		else {
			if(currentPlayerIndex-1 < 0){
				return players.size();
			}
			return currentPlayerIndex-1;
		}
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public boolean getTurnOrder() {
		return clockwise;
	}
	public ArrayList<Integer> getSkippedPlayers() {
		return skippedPlayers;
	}
	
	public void runGame() {
		boolean turn = true;
		while(turn) {
			
		}
	}
	
	public void endTurn() {
		while(skippedPlayers.contains(getNextPlayer())) {
			skippedPlayers.remove(getNextPlayer());
			currentPlayerIndex = getNextPlayer();	
		}
		currentPlayerIndex = getNextPlayer();
	}
}
