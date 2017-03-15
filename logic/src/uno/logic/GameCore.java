package uno.logic;

import uno.server.core.GameServer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

/**
 * This class initializes the game by creating a deck, all the players who shall play the game and draws the first card<br>
 * When a player chooses a card to play this class makes sure it gets played and also keeps track of all special effects the cards may have<br>
 * Also keeps track on whose turn it is and who the next player is depending on the direction of the turn order (clockwise/counterclockwise)
 * 
 * @author Daniel Rydén &amp; Fressia Merino
 * @version 2017-03-03
 */

public class GameCore {
	
	Deck deck;
	private ArrayList<Player> players;
	private int currentPlayerIndex;
	private HashMap<Integer, UUID> skippedPlayers;
	private boolean clockwise;
	private boolean waitingForInput;
    private GameServer gameServer;
    private boolean winCondition;
	
    /**
     * Takes in the game server class so it can use some game server methods like requesting the players for input
     * @param gameServer The class for the game server
     */
	public GameCore(GameServer gameServer) {
		deck = new Deck();
		players = new ArrayList<>();
		skippedPlayers = new HashMap<>();
		clockwise = true;
		waitingForInput = false;
		this.gameServer = gameServer;
		winCondition = false;
	}
	
	/** 
	 * Creates a deck, the number of players who are playing and draws the first card
     * @param playerCount Number of players
     * @param uuids The collection of UUIDs that are to be assigned to the players
     * @param shuffle
     */
	public void setupGame(int playerCount, ArrayList<UUID> uuids, boolean shuffle) {
		deck.setupDeck();
		if (shuffle) deck.shuffle();

		for (int i = 0; i < playerCount; i++) {
			Player player = new Player(("Player "+ (i+1)) ,deck, this, uuids.get(i));
			player.setup();
			players.add(player);
		}
		currentPlayerIndex = 0;
		firstDraw();
		if (currentPlayerIndex == 0 || !clockwise) {
			endTurn();
		}
	}

	/** 
	 * When the game starts a card is drawn so the first player can start it's turn, the first draw also may have a special effect
	 */
	public void firstDraw() {
		deck.getPlayedCards().add(0, deck.draw());
		Card firstCard = deck.getPlayedCards().get(0);
		switch (firstCard.type) {
			case DRAW:
				drawEffect(2);
				break;
			case NUMBER:
				break;
			case REVERSE:
				currentPlayerIndex = 1;
				reverseEffect();
				break;
			case SKIP:
				skipEffect();
				break;
			case WILD:
				endTurn();
				waitingForInput = true;
				gameServer.requestInputFromPlayer(players.get(getNextPlayer()).getUuid(), "wild");
				break;
			case WILD_DRAW:
				deck.mergeDecks();
				firstDraw();
				break;
		}
	}
	
	/**
	 * Plays the card that the player has chosen, if the card is allowed it gets added to the playedCards collection and removed from the player's hand, else nothing happens.<br>
	 * Also checks if the card has a special effect and plays it
	 * @param card The card object that is to be played
	 */
	public void executeCard(Card card) {
		
		Card temp = deck.getPlayedCards().get(0);
		if ((card.color == temp.color) ||  ((card.type == temp.type) && card.number == temp.number) || (card.type == Type.WILD) || (card.type == Type.WILD_DRAW)) {
			deck.getPlayedCards().add(0, card);
			players.get(currentPlayerIndex).getCards().remove(card);
			switch(card.type){
				case DRAW:
					drawEffect(2);
					break;
				case NUMBER:
					break;
				case REVERSE:
					reverseEffect();
					break;
				case SKIP:
					skipEffect();
					break;
				case WILD:
					waitingForInput = true;
					gameServer.requestInputFromPlayer(players.get(currentPlayerIndex).getUuid(), "wild");
					break;
				case WILD_DRAW:
					waitingForInput = true;
					gameServer.requestInputFromPlayer(players.get(currentPlayerIndex).getUuid(), "wild");
					drawEffect(4);
					break;
			}

			if ((players.get(currentPlayerIndex).getCards().size() == 1) && !players.get(currentPlayerIndex).getUnoStatus()) {
				players.get(currentPlayerIndex).drawCard();
				players.get(currentPlayerIndex).drawCard();
			} else if ((players.get(currentPlayerIndex).getCards().size() != 1) && players.get(currentPlayerIndex).getUnoStatus()) {
				players.get(currentPlayerIndex).drawCard();
				players.get(currentPlayerIndex).drawCard();
				players.get(currentPlayerIndex).drawCard();
				players.get(currentPlayerIndex).drawCard();
			}
			
			players.get(currentPlayerIndex).setUno(false);
			setWinCondition();
			
			if(!waitingForInput && !winCondition) {
				endTurn();
			}
		} else{
			return;
		}
		
	}

	/** 
	 * Forces the next player to draw 2 or 4 cards depending on which number you send and also adds the next player in the skipped list
	 * @param count The number of cards to be drawn
	 */
	public void drawEffect(int count) {
		for (int i = 0; i < count; i++){
			players.get(getNextPlayer()).drawCard();
		}
		skipEffect();
	}
	
	/**
	 * Reverses the turn order, if there're only two players this acts like a skip card
	 */
	public void reverseEffect() {
		if (players.size() == 2) {
			skipEffect();
		}
		else clockwise = !clockwise;
	}

	/**
	 * Adds the next player in the skipped list, their next turn is skipped
	 */
	public void skipEffect() {
		skippedPlayers.put(getNextPlayer(), players.get(getNextPlayer()).getUuid());
	}
	
	/**
	 * Changes the color of a wild card to a color of the players choice
	 * @param color The new color to be assigned
	 */
	@SuppressWarnings("incomplete-switch")
	public void wild(Color color) {
		Card card = deck.getPlayedCards().get(0);
		waitingForInput = false;
		switch (color) {
			case RED:
				card.color = Color.RED;
				break;
			case BLUE:
				card.color = Color.BLUE;
				break;
			case YELLOW:
				card.color = Color.YELLOW;
				break;
			case GREEN:
				card.color = Color.GREEN;
				break;
		}
		endTurn();
	}
	
	/**
	 * Check who is the next player depending on if the current turn order is clockwise or not and returns the index number for who's next
	 * @return currentPlayerIndex Index which keeps track of who is the current player
	 */
	public int getNextPlayer() {
		if(clockwise) {
			if(currentPlayerIndex+1 >= players.size()) {
				return 0;
			}
			return currentPlayerIndex+1;
		}
		else {
			if(currentPlayerIndex-1 < 0){
				return players.size()-1;
			}
			return currentPlayerIndex-1;
		}
	}
	
	/**
     * Gets the players
	 * @return players The players who are playing the game
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
     * Gets the order the turns rotates
	 * @return clockwise The current turn order, clockwise or counter-clockwise
	 */
	public boolean getTurnOrder() {
		return clockwise;
	}
	
	/**
     * Gets the skipped players
	 * @return skippedPlayers An index about which players are skipped by skip/draw effects
	 */
	public HashMap<Integer, UUID> getSkippedPlayers() {
		return skippedPlayers;
	}
	
	/**
	 * Changes the current player to the the next one in turn who is not in the skippedPlayer index
	 */
	public void endTurn() {
		while (skippedPlayers.containsKey(getNextPlayer())) {
			skippedPlayers.remove(getNextPlayer());
			currentPlayerIndex = getNextPlayer();	
		}
		currentPlayerIndex = getNextPlayer();
	}

	/**
     * Gets the current players index
	 * @return currentPlayerIndex The index which keeps track on who's turn it is
	 */
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
	
	/**
     * Gets if the game is waiting for input
	 * @return waitingForInput	A flag that the server checks so it knows that it shouldn't continue until it gets input from the player
	 */
	public boolean getWaitingForInput() {
		return waitingForInput;
	}

	/**
     * Gets the deck used in the game
	 * @return deck	Returns the deck the current game is using
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * 	If a player has zero cards the game sets a win condition flag for the server to look for
	 */
	public void setWinCondition() {
		if(players.get(currentPlayerIndex).getCards().size() == 0) {
			winCondition = true;
		}
	}
	
	/**
     * Gets if the game is won or not
	 * @return The win condition flag 
	 */
	public boolean getWinCondition() {
		return winCondition;
	}
	
	/**
	 * Sorts all the players by the number of cards they have when the game ends
	 * @return winList	The sorted list of players
	 */
	public ArrayList<Player> getWinList() {
		ArrayList<Player> winList = new ArrayList<>();
		winList.addAll(players);
		winList.sort(new Comparator<Player>(){
			@Override
			public int compare(Player p1, Player p2) {
				return p1.getCards().size() < p2.getCards().size() ? -1: p1.getCards().size() > p2.getCards().size() ? 1: 0;
			}
		});
		return winList;
	}
}
