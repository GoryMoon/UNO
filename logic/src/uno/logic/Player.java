package uno.logic;
import	java.util.*;


public class Player {

	private ArrayList<Card> cards;
	private String name;
	private Deck deck;
	
	public Player(String name, Deck deck) {
		this.name = name;
		cards = new ArrayList<Card>();
		this.deck = deck;
	}
	
	public void setup() {
		for(int i = 0; i < 7; i++) {
			drawCard();
		}
	}
	
	public void drawCard()  {
		cards.add(deck.draw());
	}
	
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public String getName() {
		return name;
	}
}
