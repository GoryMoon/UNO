import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;


public class DeckTest {
	private Deck deck;
	private ArrayList<Card> cards;
	
	@Before
	public void setUp(){
		deck = new Deck();
		cards = new ArrayList<Card>();		
	}

	@Test
	public void setupDeck() {
		deck.setupDeck();		
		int index = 0;
		cards = deck.getCards();
			
		for(int i = 0; i <= 9; i++){
			assertEquals(cards.get(index++), new Card(Color.RED, Type.NUMBER, i));		
			assertEquals(cards.get(index++), new Card(Color.YELLOW, Type.NUMBER, i));
			assertEquals(cards.get(index++), new Card(Color.GREEN, Type.NUMBER, i));
			assertEquals(cards.get(index++), new Card(Color.BLUE, Type.NUMBER, i));
		}
		for(int i = 1; i <= 9; i++){
			assertEquals(cards.get(index++), new Card(Color.RED, Type.NUMBER, i));
			assertEquals(cards.get(index++), new Card(Color.YELLOW, Type.NUMBER, i));
			assertEquals(cards.get(index++), new Card(Color.GREEN, Type.NUMBER, i));
			assertEquals(cards.get(index++), new Card(Color.BLUE, Type.NUMBER, i));
		}
		for(int i = 0; i <= 4; i++) {
			assertEquals(cards.get(index++), new Card(Color.values()[i], Type.DRAW, -1));
			assertEquals(cards.get(index++), new Card(Color.values()[i], Type.DRAW, -1));
			assertEquals(cards.get(index++), new Card(Color.values()[i], Type.SKIP, -1));
			assertEquals(cards.get(index++), new Card(Color.values()[i], Type.SKIP, -1));
			assertEquals(cards.get(index++), new Card(Color.values()[i], Type.REVERSE, -1));
			assertEquals(cards.get(index++), new Card(Color.values()[i], Type.REVERSE, -1));
		}
		for(int i = 0; i <= 4; i++) {
			assertEquals(cards.get(index++), new Card(Color.BLACK, Type.WILD, -1));
			assertEquals(cards.get(index++), new Card(Color.BLACK, Type.WILD_DRAW, -1));
		}
			
		
		

	}
	@Test
	public void shuffle() {
		deck = new Deck();
		deck.setupDeck();
		deck.shuffle();
		cards = deck.getCards();
		int index = 0;
		for(int i = 0; i <= 9; i++){
			assertNotEquals(cards.get(index++), new Card(Color.RED, Type.NUMBER, i));		
			assertNotEquals(cards.get(index++), new Card(Color.YELLOW, Type.NUMBER, i));
			assertNotEquals(cards.get(index++), new Card(Color.GREEN, Type.NUMBER, i));
			assertNotEquals(cards.get(index++), new Card(Color.BLUE, Type.NUMBER, i));
		}
		for(int i = 1; i <= 9; i++){
			assertNotEquals(cards.get(index++), new Card(Color.RED, Type.NUMBER, i));
			assertNotEquals(cards.get(index++), new Card(Color.YELLOW, Type.NUMBER, i));
			assertNotEquals(cards.get(index++), new Card(Color.GREEN, Type.NUMBER, i));
			assertNotEquals(cards.get(index++), new Card(Color.BLUE, Type.NUMBER, i));
		}
		for(int i = 0; i <= 4; i++) {
			assertNotEquals(cards.get(index++), new Card(Color.values()[i], Type.DRAW, -1));
			assertNotEquals(cards.get(index++), new Card(Color.values()[i], Type.DRAW, -1));
			assertNotEquals(cards.get(index++), new Card(Color.values()[i], Type.SKIP, -1));
			assertNotEquals(cards.get(index++), new Card(Color.values()[i], Type.SKIP, -1));
			assertNotEquals(cards.get(index++), new Card(Color.values()[i], Type.REVERSE, -1));
			assertNotEquals(cards.get(index++), new Card(Color.values()[i], Type.REVERSE, -1));
		}
		for(int i = 0; i <= 4; i++) {
			assertNotEquals(cards.get(index++), new Card(Color.BLACK, Type.WILD, -1));
			assertNotEquals(cards.get(index++), new Card(Color.BLACK, Type.WILD_DRAW, -1));
		}
		
	}

}
