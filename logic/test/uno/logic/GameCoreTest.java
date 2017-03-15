package uno.logic;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests to test methods in the GameCore
 * @author Daniel Rydén &amp; Fressia Merino
 * @version 2017-03-03
 */
public class GameCoreTest {
	
	private GameCore gameCore;
	private ArrayList<UUID> uuids;

	/**
	 * Setups the tests
	 */
	@Before
	public void setUp(){
		gameCore = new GameCore(null);
		uuids = new ArrayList<>();
		uuids.add(UUID.randomUUID());
		uuids.add(UUID.randomUUID());
		uuids.add(UUID.randomUUID());
		uuids.add(UUID.randomUUID());
		uuids.add(UUID.randomUUID());
	}

    /**
     * Tests if the first draw card is the correct one
     */
	@Test
	public void firstDrawTest() {
		gameCore.setupGame(4, uuids, false);
		assertEquals(new Card(Color.RED, Type.NUMBER, 7), gameCore.deck.getPlayedCards().get(0));
		
	}

    /**
     * Tests if the effect of the first draw works
     */
	@Test
	public void firstDrawEffectsTest(){
		gameCore = new GameCore(null);
		gameCore.setupGame(4, uuids, false);
		gameCore.deck.getCards().add(0, new Card(Color.RED, Type.DRAW, 1));
		gameCore.firstDraw();
		assertEquals(9, gameCore.getPlayers().get(gameCore.getNextPlayer()).getCards().size());
		assertTrue(gameCore.getSkippedPlayers().containsKey(gameCore.getNextPlayer()));
		gameCore.deck.getCards().add(0, new Card(Color.RED, Type.REVERSE, 1));
		gameCore.firstDraw();
		assertTrue(!gameCore.getTurnOrder());
		assertEquals(0, gameCore.getNextPlayer());
		
		
		
	}

    /**
     * Test if the effect that draws two cards work
     */
	@Test
	public void drawEffectTest(){
		gameCore = new GameCore(null);
		gameCore.setupGame(4, uuids, false);
		gameCore.drawEffect(2);
		assertEquals(gameCore.getPlayers().get(gameCore.getNextPlayer()).getCards().size(), 9);
		assertTrue(gameCore.getSkippedPlayers().containsKey(gameCore.getNextPlayer()));
	}

    /**
     * Tests if the reverse card effect works
     */
	@Test
	public void reverseEffectTest() {
		gameCore = new GameCore(null);
		gameCore.setupGame(4, uuids, false);
		gameCore.reverseEffect();
		assertTrue(!gameCore.getTurnOrder());
		
	}

    /**
     * Tests if the skip card effect works
     */
	@Test
	public void skipEffectTest() {
		gameCore = new GameCore(null);
		gameCore.setupGame(4, uuids, false);
		gameCore.skipEffect();
		assertEquals(1, gameCore.getSkippedPlayers().size());
	}

    /**
     * Checks if the execute card method works by combing effect tests
     */
	@Test
	public void executeCard() {
        gameCore = new GameCore(null);
        gameCore.setupGame(4, uuids, false);
        gameCore.executeCard(new Card(Color.RED, Type.DRAW, -1));
        assertEquals(new Card(Color.RED, Type.DRAW, -1), gameCore.deck.getPlayedCards().get(0));
        assertEquals(9, gameCore.getPlayers().get(gameCore.getCurrentPlayerIndex()-1).getCards().size());
        assertEquals(3, gameCore.getCurrentPlayerIndex());
        gameCore.executeCard(new Card(Color.BLUE, Type.REVERSE, -1));
        assertEquals(new Card(Color.RED, Type.DRAW, -1), gameCore.deck.getPlayedCards().get(0));
        gameCore.executeCard(new Card(Color.RED, Type.REVERSE, -1));
        assertEquals(new Card(Color.RED, Type.REVERSE, -1), gameCore.deck.getPlayedCards().get(0));
        assertTrue(!gameCore.getTurnOrder());
        gameCore.executeCard(new Card(Color.BLUE, Type.REVERSE, -1));
        assertEquals(new Card(Color.BLUE, Type.REVERSE, -1), gameCore.deck.getPlayedCards().get(0));
        assertTrue(gameCore.getTurnOrder());
        gameCore.executeCard(new Card(Color.BLUE, Type.NUMBER, 2));
        assertEquals(new Card(Color.BLUE, Type.NUMBER, 2), gameCore.deck.getPlayedCards().get(0));
        gameCore.executeCard(new Card(Color.GREEN, Type.NUMBER, 3));
        assertEquals(new Card(Color.BLUE, Type.NUMBER, 2), gameCore.deck.getPlayedCards().get(0));
        gameCore.executeCard(new Card(Color.GREEN, Type.NUMBER, 2));
        assertEquals(new Card(Color.GREEN, Type.NUMBER, 2), gameCore.deck.getPlayedCards().get(0));
		
	}

    /**
     * Checks if the end turn method works
     */
	@Test
	public void endTurnTest() {
        gameCore = new GameCore(null);
        gameCore.setupGame(4, uuids, false);
        gameCore.endTurn();
        assertEquals(2, gameCore.getCurrentPlayerIndex());
        gameCore.skipEffect();
        assertTrue(gameCore.getSkippedPlayers().containsKey(3));
        gameCore.endTurn();
        assertEquals(0, gameCore.getCurrentPlayerIndex());

	}
}
