package uno.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import uno.logic.Card;
import uno.logic.Deck;
import uno.logic.Player;
import uno.server.core.GameServer;

import org.junit.Before;
/**
 * 
 * @author nakhle
 * @version
 */
public class PlayerTest {

	private Player player;
	private Deck deck;
	private ArrayList<Card> cards;
	private GameCore gameCore;
	private GameServer gameServer;
	
	@Before
	public void setUp() {
		deck = new Deck();
		gameCore = new GameCore(new GameServer());	
		player = new Player("p1", deck, gameCore, UUID.randomUUID());
		
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
