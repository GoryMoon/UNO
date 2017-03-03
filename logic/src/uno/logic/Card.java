package uno.logic;

/**
 * This class keeps track of all the information about the cards, such as color, type and number.
 * 
 * @author Daniel Ryd�n & Fressia Moreno
 * @version 2017-03-03
 */

public class Card {

	Color color;
	Type type;
	int number;

	/**
	 * Takes in all the properties a card should have and creates it
	 * @param color The color of the card
	 * @param type The type of the card which determines the special effects it may have
	 * @param number The number of the card (-1 for cards without a number)
	 */
	public Card(Color color, Type type, int number){
		this.color = color;
		this.type = type;
		this.number = number;
	}

	/**
	 * @return color Returns the color of the card
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return type Returns the type of the card
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * @return type Returns the number of the card, if it's a special card that has no number the number is -1
	 */
	public int getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return "Card{" + "color=" + color + ", type=" + type + ", number=" + number + '}';
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Card) {
			Card c = (Card) obj;
			return c.color == color && c.type == type && c.number == number;
		}
		return false;
	}
}
