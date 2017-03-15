package uno.logic;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests to test methods in the Deck
 * @author Daniel Rydén &amp; Fressia Merino
 * @version 2017-03-03
 */
public class DeckTest {

	private Deck deck;
	private ArrayList<Card> cards;

    /**
     * Setups the tests
     */
	@Before
	public void setUp(){
		deck = new Deck();
		deck.setupDeck();
	}

    /**
     * Tests if the setup of the deck works
     */
	@Test
	public void setupDeck() {
        setUp();
        int index = 0;
        cards = deck.getCards();

        for(int i = 0; i <= 9; i++){
            assertEquals(new Card(Color.RED, Type.NUMBER, i), cards.get(index++));
            assertEquals(new Card(Color.YELLOW, Type.NUMBER, i), cards.get(index++));
            assertEquals(new Card(Color.GREEN, Type.NUMBER, i), cards.get(index++));
            assertEquals(new Card(Color.BLUE, Type.NUMBER, i), cards.get(index++));
        }
        for(int i = 1; i <= 9; i++){
            assertEquals(new Card(Color.RED, Type.NUMBER, i), cards.get(index++));
            assertEquals(new Card(Color.YELLOW, Type.NUMBER, i), cards.get(index++));
            assertEquals(new Card(Color.GREEN, Type.NUMBER, i), cards.get(index++));
            assertEquals(new Card(Color.BLUE, Type.NUMBER, i), cards.get(index++));
        }
        for(int i = 0; i < 8; i++) {
            assertEquals(new Card(Color.values()[i % 4], Type.DRAW, -1), cards.get(index++));
            assertEquals(new Card(Color.values()[i % 4], Type.SKIP, -1), cards.get(index++));
            assertEquals(new Card(Color.values()[i % 4], Type.REVERSE, -1), cards.get(index++));
        }
        for(int i = 0; i < 4; i++) {
            assertEquals(new Card(Color.BLACK, Type.WILD, -1), cards.get(index++));
            assertEquals(new Card(Color.BLACK, Type.WILD_DRAW, -1), cards.get(index++));
        }
	}

    /**
     * Tests if the draw method works in the deck
     */
	@Test
	public void draw() {
        setUp();
        Card temp;
        temp = deck.draw();
        cards = deck.getCards();
        assertEquals(temp, new Card(Color.RED, Type.NUMBER, 0));
        assertEquals(cards.get(0), new Card(Color.YELLOW, Type.NUMBER, 0));
	}

    /**
     * Tests if the draw works if the deck gets empty
     */
	@Test
	public void drawEmpty() {
        setUp();
        Card tmp = deck.draw();
        deck.getPlayedCards().addAll(deck.getCards());
        deck.getCards().clear();
        deck.getCards().add(tmp);

        assertEquals(1, deck.getCards().size());
        assertEquals(107, deck.getPlayedCards().size());
        deck.draw();
        //One card is on the table and one in the hand,  total 108
        assertEquals(106, deck.getCards().size());
        assertEquals(1, deck.getPlayedCards().size());
	}

}
