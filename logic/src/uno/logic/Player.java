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
	
	public void setup() {
		for(int i = 0; i < 7; i++) {
			drawCard();
		}
	}
	
	public void drawCard()  {
		cards.add(deck.draw());
		uno = false;
	}
	
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
		uno = false;
		
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public boolean unoStatus() {
		return uno;
	}
	
	public void setUno(boolean status) {
		uno = status;
	}
	
	public String getName() {
		return name;
	}
	public UUID getUuid() {
		return uuid;
	}
}
