package fr.xebia.dd;

import java.util.Objects;

import static java.lang.Integer.parseInt;

class Item {

    private final String name;
    private int damage;

    Item(String name) {
        this.name = name;
        name = name.replaceAll(" ", "");
        int sum = name.toUpperCase().chars().map(c -> {
            switch (c) {
                case 'A': case 'F': case 'K': case 'P': case 'U': case 'Z': return 0;
                case 'B': case 'G': case 'L': case 'Q': case 'V':           return 1;
                case 'C': case 'H': case 'M': case 'R': case 'W':           return 2;
                case 'D': case 'I': case 'N': case 'S': case 'X':           return 3;
                case 'E': case 'J': case 'O': case 'T': case 'Y':           return 4;
                case '0': case '5':                                         return 5;
                case '1': case '6':                                         return 6;
                case '2': case '7': case '!': case '?': case '-': case '&': return 7;
                case '3': case '8':                                         return 8;
                case '4': case '9':                                         return 9;
                default: throw new IllegalArgumentException();
            }
        }).sum();

        while (sum > 9) {
            sum = Integer.toString(sum).chars().map(c -> parseInt(Character.toString((char) c))).sum();
        }

        this.damage = sum;
    }

    Item(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    int getDamage() {
        return damage;
    }

    @Override
    public String toString() {
        return name + " +" + damage + "dmg";
    }
}
