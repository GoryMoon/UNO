package uno.logic;

import java.util.ArrayList;
import java.util.UUID;
/**
 * Has all info about the players such as their hands, their unique identifier and if they "called UNO" or not
 * <p>
 * This class can also draw cards for the player and put it in their hand
 * 
 * @author Daniel Rydén &amp; Fressia Merino
 * @version 2017-03-03
 */

public class Player {

	private ArrayList<Card> cards;
	private String name;
	private Deck deck;
	private GameCore gameCore;
	private boolean uno;
	private UUID uuid;
	
	/**
	 * Takes in all info about the new player to be created, gives the player info about the game currently played and also access to deck and gameCore's methods
	 * @param name The player's name
	 * @param deck The deck used by the game
	 * @param gameCore The current game 
	 * @param uuid The player's unique identifier
	 */
	public Player(String name, Deck deck, GameCore gameCore, UUID uuid) {
		this.name = name;
		cards = new ArrayList<>();
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
	 * When out of options the player can choose to draw a card instead,<br>
	 * If the card is playable it is immediately played. After either case the turn ends.
	 */
	public void endDraw() {
		Card card = deck.draw();
		Card temp = gameCore.deck.getPlayedCards().get(0);
		if((card.color == temp.color) ||  ((card.type == temp.type) && card.number == temp.number) || (card.type == Type.WILD) || (card.type == Type.WILD_DRAW)){
			gameCore.executeCard(card);
		} else {
			cards.add(card);
			gameCore.endTurn();
		}
	}
	
	/**
	 * @return cards Returns the cards that are in the player's hand
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	/**
	 * @return uno A flag that checks if the player called Uno or not
	 */
	public boolean unoStatus() {
		return uno;
	}
	
	/**
	 * Sets the Uno flag to true or false, true meaning that you've called Uno
	 * @param status True or false depending on what you want to set the flag to
	 */
	public void setUno(boolean status) {
		uno = status;
	}
	
	/**
	 * Returns the player's name
	 * @return name The player's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the player's unique identifier
	 * @return uuid The player's unique identifier
	 */
	public UUID getUuid() {
		return uuid;
	}
}
