package uno.ui;

/**
 * Stores information about the cards, used to display and send network message
 * @author Gustaf Järgren
 * @version 2017-03-03
 */
public class Card {

    public String type;
    public String color;
    public String number;

    public Card(String[] split) {
        this.type = split[0];
        this.color = split[1];
        this.number = split[2];
    }

    /**
     * Returns the number of the card with color or if it's a special, the type and color
     */
    @Override
    public String toString() {
        if (type.toLowerCase().equals("number")) {
            return number + color.toLowerCase();
        } else {
            return type + color;
        }
    }
}
