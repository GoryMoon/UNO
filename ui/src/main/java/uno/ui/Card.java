package uno.ui;

public class Card {

    public String type;
    public String color;
    public String number;

    public Card(String[] split) {
        this.type = split[0];
        this.color = split[1];
        this.number = split[2];
    }

    @Override
    public String toString() {
        if (type.toLowerCase().equals("number")) {
            return number + color.toLowerCase();
        } else {
            return type + color;
        }
    }
}
