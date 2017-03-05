package uno.logic;

import org.junit.Before;
import org.junit.Test;
import uno.server.core.GameServer;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * 
 * @author nakhle
 */
public class GameCoreTest {
	
	private GameCore gameCore;
	private ArrayList<UUID> uuids;
	private GameServer gameServer;
	
	
	
	@Before
	public void setUp(){
		gameCore = new GameCore(null);
		uuids = new ArrayList<UUID>();
		uuids.add(UUID.randomUUID());
		uuids.add(UUID.randomUUID());
		uuids.add(UUID.randomUUID());
		uuids.add(UUID.randomUUID());
		uuids.add(UUID.randomUUID());
		
	
	}

	@Test
	public void firstDrawTest() {
		gameCore.setupGame(4, uuids);
		assertEquals(gameCore.deck.getPlayedCards().get(0), new Card(Color.RED, Type.NUMBER, 7));
		
	}
	
	@Test
	public void firstDrawEffectsTest(){
		gameCore = new GameCore(null);
		gameCore.setupGame(4, uuids);
		gameCore.deck.getCards().add(0, new Card(Color.RED, Type.DRAW, 1));
		gameCore.firstDraw();
		assertEquals(gameCore.getPlayers().get(gameCore.getNextPlayer()).getCards().size(), 9);
		assertTrue(gameCore.getSkippedPlayers().containsKey(gameCore.getNextPlayer()));
		gameCore.deck.getCards().add(0, new Card(Color.RED, Type.REVERSE, 1));
		gameCore.firstDraw();
		assertTrue(!gameCore.getTurnOrder());
		assertEquals(gameCore.getNextPlayer(), 0);
		
		
		
	}
	
	@Test
	public void drawEffectTest(){
		gameCore = new GameCore(null);
		gameCore.setupGame(4, uuids);
		gameCore.drawEffect(2);
		assertEquals(gameCore.getPlayers().get(gameCore.getNextPlayer()).getCards().size(), 9);
		assertTrue(gameCore.getSkippedPlayers().containsKey(gameCore.getNextPlayer()));
	}
	
	@Test
	public void reverseEffectTest() {
		gameCore = new GameCore(null);
		gameCore.setupGame(4, uuids);
		gameCore.reverseEffect();
		assertTrue(!gameCore.getTurnOrder());
		
	}
	
	@Test
	public void skipEffectTest() {
		gameCore = new GameCore(null);
		gameCore.setupGame(4, uuids);
		gameCore.skipEffect();
	}
	
	@Test
	public void executeCard() {
		gameCore = new GameCore(null);
		gameCore.setupGame(4, uuids);
		gameCore.executeCard(new Card(Color.RED, Type.DRAW, -1));
		assertEquals(gameCore.deck.getPlayedCards().get(0), new Card(Color.RED, Type.DRAW, -1));
		assertEquals(gameCore.getPlayers().get(gameCore.getCurrentPlayerIndex()-1).getCards().size(), 9);
		assertEquals(gameCore.getCurrentPlayerIndex(), 3);
		gameCore.executeCard(new Card(Color.BLUE, Type.REVERSE, -1));
		assertEquals(gameCore.deck.getPlayedCards().get(0), new Card(Color.RED, Type.DRAW, -1));
		gameCore.executeCard(new Card(Color.RED, Type.REVERSE, -1));
		assertEquals(gameCore.deck.getPlayedCards().get(0), new Card(Color.RED, Type.REVERSE, -1));
		assertTrue(!gameCore.getTurnOrder());
		gameCore.executeCard(new Card(Color.BLUE, Type.REVERSE, -1));
		assertEquals(gameCore.deck.getPlayedCards().get(0), new Card(Color.BLUE, Type.REVERSE, -1));
		assertTrue(gameCore.getTurnOrder());
		gameCore.executeCard(new Card(Color.BLUE, Type.NUMBER, 2));
		assertEquals(gameCore.deck.getPlayedCards().get(0), new Card(Color.BLUE, Type.NUMBER, 2));
		gameCore.executeCard(new Card(Color.GREEN, Type.NUMBER, 3));
		assertEquals(gameCore.deck.getPlayedCards().get(0), new Card(Color.BLUE, Type.NUMBER, 2));
		gameCore.executeCard(new Card(Color.GREEN, Type.NUMBER, 2));
		assertEquals(gameCore.deck.getPlayedCards().get(0), new Card(Color.GREEN, Type.NUMBER, 2));
		
	}
	@Test
	public void endTurnTest() {
		gameCore = new GameCore(null);
		gameCore.setupGame(4, uuids);
		gameCore.endTurn();
		assertEquals(gameCore.getCurrentPlayerIndex(), 2);
		gameCore.skipEffect();
		gameCore.endTurn();
		assertTrue(gameCore.getSkippedPlayers().containsKey(3));
		assertEquals(gameCore.getCurrentPlayerIndex(), 0);

	}
	
	

}
