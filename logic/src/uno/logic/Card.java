package uno.logic;

/**
 * 
 * @author nakhle
 * @version
 */

public class Card {
	Color color;
	Type type;
	int number;
	
	public Card(Color color, Type type, int number){
		this.color = color;
		this.type = type;
		this.number = number;

	}
	
	public Color getColor() {
		return color;
	}
	
	public Type getType() {
		return type;
	}
	
	public int getNumber() {
		return number;
	}
	
	public String toString() {
		return 
				"Color: " + color + 
	            " Type: " + type +
	            " Number: " + number;
	}
	public boolean equals(Object obj) {
		if(obj instanceof Card) {
			Card c = (Card) obj;
			return c.color == color && c.type == type && c.number == number;
		}
		return false;
	}
}
