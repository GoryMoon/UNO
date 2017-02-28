package uno.logic;
import	java.util.*;
/**
 * 
 * @author nakhle
 * @version
 */

public class Player {

	private ArrayList<Card> cards;
	private String name;
	private Deck deck;
	private GameCore gameCore;
	private boolean uno;
	private UUID uuid;
	
	public Player(String name, Deck deck, GameCore gameCore, UUID uuid) {
		this.name = name;
		cards = new ArrayList<Card>();
		this.deck = deck;
		this.gameCore = gameCore;
		uno = false;
		this.uuid = uuid;
	}
	/**
	 * Draws seven cards when the game starts
	 */
	public void setup() {
		for(int i = 0; i < 7; i++) {
			drawCard();
		}
	}
	/**
	 * Draws a card and adds it to the player's hand
	 */
	public void drawCard()  {
		cards.add(deck.draw());
	}
	
	/**
	 * The player can choose to draw a card instead of playing one when out of options, 
	 * if the card is playable it is immediately played and the turn ends in either case
	 */
	public void endDraw() {
		Card card = deck.draw();
		Card temp = gameCore.deck.getPlayedCards().get(0);
		if((card.color == temp.color) ||  ((card.type == temp.type) && card.number == temp.number) || (card.type == Type.WILD) || (card.type == Type.WILD_DRAW)){
			gameCore.executeCard(card);
		}
		
		else {
			cards.add(card);
			gameCore.endTurn();
		}
		
	}
	
	/**
	 * 
	 * @return cards Returns the cards that are in the player's hand
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	/**
	 * 
	 * @return uno A flag that checks if the player called Uno or not
	 */
	public boolean unoStatus() {
		return uno;
	}
	
	/**
	 * Sets the Uno flag to true or false, true meaning that you've called Uno
	 * @param status True or false depending on what you want to set the flag too
	 */
	public void setUno(boolean status) {
		uno = status;
	}
	
	/**
	 * 
	 * @return name Returns the player's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return uuid Returns the player's unique identifier
	 */
	public UUID getUuid() {
		return uuid;
	}
}
