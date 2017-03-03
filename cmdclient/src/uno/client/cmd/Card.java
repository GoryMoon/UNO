package uno.client.cmd;

public class Card {

    public String type;
    public String color;
    public String number;

    public Card(String[] split) {
        type = split[0];
        color = split[1];
        number = split[2];
    }

    @Override
    public String toString() {
        if (type.toLowerCase().equals("number")) {
            return color + " " + number;
        } else {
            return type + " " + color;
        }
    }
}
