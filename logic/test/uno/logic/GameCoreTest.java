package uno.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;

public class GameCoreTest {
	
	private GameCore gameCore;
	private ArrayList<Player> players;
	private int currentPlayerIndex;
	private ArrayList<Integer> skippedPlayers;
	private boolean clockwise;
	
	
	
	@Before
	public void setUp(){
		gameCore = new GameCore();
		players = new ArrayList<Player>();
		skippedPlayers = new ArrayList<Integer>();
		clockwise = true;
		
	}

	@Test
	public void firstDrawTest() {
		gameCore.setupGame(4);
		assertEquals(gameCore.deck.getPlayedCards().get(0), new Card(Color.RED, Type.NUMBER, 7));
		
	}
	
	@Test
	public void firstDrawEffectsTest(){
		gameCore = new GameCore();
		gameCore.setupGame(4);
		gameCore.deck.getCards().add(0, new Card(Color.RED, Type.DRAW, 1));
		gameCore.firstDraw();
		assertEquals(gameCore.getPlayers().get(1).getCards().size(), 9);
		assertEquals(gameCore.getSkippedPlayers().get(0).intValue(), 1);
		gameCore.deck.getCards().add(0, new Card(Color.RED, Type.REVERSE, 1));
		gameCore.firstDraw();
		assertTrue(!gameCore.getTurnOrder());
		assertEquals(gameCore.getNextPlayer(), 0);
		
		
		
	}
	
	@Test
	public void drawEffectTest(){
		gameCore = new GameCore();
		gameCore.setupGame(4);
		gameCore.drawEffect(2);
		assertEquals(gameCore.getPlayers().get(1).getCards().size(), 9);
		assertEquals(gameCore.getSkippedPlayers().get(0).intValue(), 1);
	}
	
	@Test
	public void reverseEffectTest() {
		gameCore = new GameCore();
		gameCore.setupGame(4);
		gameCore.reverseEffect();
		assertTrue(!gameCore.getTurnOrder());
		
	}
	
	@Test
	public void skipEffectTest() {
		gameCore = new GameCore();
		gameCore.setupGame(4);
		gameCore.skipEffect();
	}
	
	@Test
	public void executeCard() {
		gameCore = new GameCore();
		gameCore.setupGame(4);
		gameCore.executeCard(new Card(Color.RED, Type.DRAW, -1));
		assertEquals(gameCore.deck.getPlayedCards().get(0), new Card(Color.RED, Type.DRAW, -1));
		assertEquals(gameCore.getPlayers().get(1).getCards().size(), 9);
		assertEquals(gameCore.getSkippedPlayers().get(0).intValue(), 1);
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

}
