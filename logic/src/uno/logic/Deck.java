package uno.logic;

import java.util.ArrayList;

/**
 * This class keeps track of the deck, the class can create a deck with all the cards as well as create a "played cards pile" where you put all the played cards<br>
 * If the deck runs out of cards it shuffles all the played cards into a new deck.<br>
 * In this class is also where you draw cards.
 * 
 * @author Daniel Rydén &amp; Fressia Merino
 * @version 2017-03-03
 */
public class Deck {
	
	private ArrayList<Card> cards;
	private ArrayList<Card> playedCards;

	/**
	 * Creates a new deck instance
	 */
	public Deck() {
		cards = new ArrayList<>();
		playedCards = new ArrayList<>();
	}

	/**
     * Gets the cards of the deck
	 * @return cards Returns the cards that are in the decks
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	/**
     * Gets the played card of the deck
	 * @return playedCards Returns the pile of cards that have been played in the duration of the game
	 */
	public ArrayList<Card> getPlayedCards() {
		return playedCards;
	}
	
	/**
	 * Adds all the cards we're using to our deck and shuffles it 
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
		for(int i = 0; i < 8; i++) {
			cards.add(new Card(Color.values()[i % 4], Type.DRAW, -1));
			cards.add(new Card(Color.values()[i % 4], Type.SKIP, -1));
			cards.add(new Card(Color.values()[i % 4], Type.REVERSE, -1));
		}
		for(int i = 0; i < 4; i++) {
			cards.add(new Card(Color.BLACK, Type.WILD, -1));
			cards.add(new Card(Color.BLACK, Type.WILD_DRAW, -1));
		}
	}
	
	/**
	 * Implementation of Fisher-Yates Shuffle algorithm
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
	 * Returns the first card in our deck collection and then removes it
	 * <p>
	 * If the last card is drawn it takes the currently played cards and shuffles them into a new deck
	 * @return card The drawn card
	 */
	public Card draw() {
		Card card = cards.get(0);
		cards.remove(0);
		if(cards.isEmpty()) {
			mergeDecks();
		}
		return card;
	}
	
	
	/**
	 *  Takes the playedCards deck and shuffles it into the cards deck, leaving the last played card for other players to play against 
	 */
	public void mergeDecks() {
		Card temp = playedCards.get(0);
		playedCards.remove(0);
		cards.addAll(playedCards);
		playedCards.clear();
		playedCards.add(0, temp);
		shuffle();
	}
}
