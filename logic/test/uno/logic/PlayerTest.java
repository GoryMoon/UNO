package uno.logic;

import org.junit.Before;
import org.junit.Test;
import uno.server.core.GameServer;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests to test the player
 * @author Daniel Rydén &amp; Fressia Merino
 * @version 2017-03-03
 */
public class PlayerTest {

	private Player player;
	private Deck deck;
	private ArrayList<Card> cards;
	private GameCore gameCore;

    /**
     * Setups the test
     */
	@Before
	public void setUp() {
		deck = new Deck();
		gameCore = new GameCore(null);
		player = new Player("p1", deck, gameCore, UUID.randomUUID());
	}

    /**
     * Tests if the player got the correct setup
     */
	@Test
	public void setupTest() {
		deck.setupDeck();
		deck.shuffle();
		player.setup();
		cards = player.getCards();
		assertEquals(7, cards.size());
	}

}
