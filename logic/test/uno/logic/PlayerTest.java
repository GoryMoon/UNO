package uno.logic;

import org.junit.Before;
import org.junit.Test;
import uno.server.core.GameServer;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
/**
 * 
 * @author nakhle
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
