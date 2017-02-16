package logic.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import uno.logic.Card;
import uno.logic.Deck;
import uno.logic.Player;

import org.junit.Before;

public class PlayerTest {

	private Player player;
	private Deck deck;
	private ArrayList<Card> cards;
	
	@Before
	public void setUp() {
		deck = new Deck();
		player = new Player("p1", deck);
		
	}
	
	@Test
	public void setupTest() {
		deck.setupDeck();
		deck.shuffle();
		player.setup();
		cards = player.getCards();
		assertTrue(cards.size() == 7);
		
	}

}
