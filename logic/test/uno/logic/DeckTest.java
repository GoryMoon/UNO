package uno.logic;
import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

import uno.logic.Card;
import uno.logic.Color;
import uno.logic.Deck;
import uno.logic.Type;


public class DeckTest {
	private Deck deck;
	private ArrayList<Card> cards;
	private ArrayList<Card> playedCards;
	
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
	
	
	@Test
	public void draw() {
		Card temp;
		deck.setupDeck();
		temp = deck.draw();
		cards = deck.getCards();
		assertEquals(temp, new Card(Color.RED, Type.NUMBER, 0));
		assertEquals(cards.get(0), new Card(Color.YELLOW, Type.NUMBER, 0));
		
	}
	
	@Test
	public void drawEmpty() {
		cards = deck.getCards();
		playedCards = deck.getPlayedCards();
		cards.add(new Card(Color.RED, Type.SKIP, -1));
		playedCards.add(new Card(Color.BLACK, Type.REVERSE, 3));
		deck.draw();
		assertEquals(cards.get(0), new Card(Color.BLACK, Type.REVERSE, 3));
		assertTrue(playedCards.isEmpty()); 
		
	}

}
