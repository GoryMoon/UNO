package uno.logic;
import java.util.*;

/**
 * 
 * @author nakhle
 * @version
 */
public class Deck {
	
	private ArrayList<Card> cards;
	private ArrayList<Card> playedCards;
	
	public Deck() {
		cards = new ArrayList<Card>();
		playedCards = new ArrayList<Card>();
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public ArrayList<Card> getPlayedCards() {
		return playedCards;
	}
	
	/**
	 * Adds all the cards we're using to our deck
	 */
	public void setupDeck() { 
		for(int i = 0; i <= 9; i++){
			cards.add(new Card(Color.RED, Type.NUMBER, i));		
			cards.add(new Card(Color.YELLOW, Type.NUMBER, i));
			cards.add(new Card(Color.GREEN, Type.NUMBER, i));
			cards.add(new Card(Color.BLUE, Type.NUMBER, i));
		}
		for(int i = 1; i <= 9; i++){
			cards.add(new Card(Color.RED, Type.NUMBER, i));
			cards.add(new Card(Color.YELLOW, Type.NUMBER, i));
			cards.add(new Card(Color.GREEN, Type.NUMBER, i));
			cards.add(new Card(Color.BLUE, Type.NUMBER, i));
		}
		for(int i = 0; i <= 4; i++) {
			cards.add(new Card(Color.values()[i], Type.DRAW, -1));
			cards.add(new Card(Color.values()[i], Type.DRAW, -1));
			cards.add(new Card(Color.values()[i], Type.SKIP, -1));
			cards.add(new Card(Color.values()[i], Type.SKIP, -1));
			cards.add(new Card(Color.values()[i], Type.REVERSE, -1));
			cards.add(new Card(Color.values()[i], Type.REVERSE, -1));
		}
		for(int i = 0; i <= 4; i++) {
			cards.add(new Card(Color.BLACK, Type.WILD, -1));
			cards.add(new Card(Color.BLACK, Type.WILD_DRAW, -1));
		}
		//shuffle();
	}
	
	/**
	 * 
	 * Implementations of Fisher-Yates Shuffle algoritm
	 */
	public void shuffle() {
		int length = cards.size();
		for (int i = 0; i < length; i++) {
			int r = i + (int)(Math.random() * (length - i));
			Card temp = cards.get(r); 
			cards.set(r, cards.get(i));
			cards.set(i, temp);
		}
	}
	/**
	 * 
	 * Returns the first card in our deck collection and then removes it
	 * If the last card is drawn it takes the currently played cards and shuffles them into a new deck
	 */
	
	public Card draw() {
		Card temp = cards.get(0);
		cards.remove(0);
		if(cards.isEmpty()) {
			mergeDecks();
		}
		return temp;
	}
	/*
	 *  Takes the playedCards deck and shuffles it into the cards deck 
	 */
	
	public void mergeDecks() {
		cards.addAll(playedCards);
		playedCards.clear();
		shuffle();
	}
	
	
}
