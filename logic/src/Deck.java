import java.util.*;


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
	
	/**
	 * Adds all the cards we're using to our deck
	 */
	void setupDeck() { 
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
		
	}
	
	/**
	 * 
	 * Implementations of Fisher-Yates Shuffle algoritm
	 */
	void shuffle() {
		int length = cards.size();
		for (int i = 0; i < length; i++) {
			int r = i + (int)(Math.random() * (length - i));
			Card temp = cards.get(r); 
			cards.set(r, cards.get(i));
			cards.set(i, temp);
		}
	}
	
}
