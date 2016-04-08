package fr.xebia.dd;

import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.Collections.singletonList;

class Player {

    private final String name;

    Player(String name) {
        this.name = name;
    }

    List<Item> items() {
        return singletonList(nameToItem());
    }

    private Item nameToItem() {
        int sum = name.toUpperCase().chars().map(c -> {
            switch (c) {
                case 'A':
                case 'F':
                case 'K':
                case 'P':
                case 'U':
                case 'Z':
                    return 0;
                case 'B':
                case 'G':
                case 'L':
                case 'Q':
                case 'V':
                    return 1;
                case 'C':
                case 'H':
                case 'M':
                case 'R':
                case 'W':
                    return 2;
                case 'D':
                case 'I':
                case 'N':
                case 'S':
                case 'X':
                    return 3;
                case 'E':
                case 'J':
                case 'O':
                case 'T':
                case 'Y':
                    return 4;
                case '0':
                case '5':
                    return 5;
                case '1':
                case '6':
                    return 6;
                case '2':
                case '7':
                case '!':
                case '?':
                case '-':
                case '&':
                    return 7;
                case '3':
                case '8':
                    return 8;
                case '4':
                case '9':
                    return 9;
                default:
                    throw new IllegalArgumentException();
            }
        }).sum();

        while (sum > 9) {
            sum = Integer.toString(sum).chars().map(c -> parseInt(Character.toString((char) c))).sum();
        }

        switch (sum) {
            case 0:
                return new Item("Medal");
            case 1:
                return new Item("Boots of Speed");
            case 2:
                return new Item("Headgear Armor Item");
            case 3:
                return new Item("Ring of Protection");
            case 4:
                return new Item("Ring of Fire Resistance");
            case 5:
                return new Item("Ring of Spell Turning");
            case 6:
                return new Item("Gauntlets of Ogre Power");
            case 7:
                return new Item("Anklet");
            case 8:
                return new Item("Brooch");
            case 9:
                return new Item("Orb");
            default:
                throw new IllegalArgumentException();
        }
    }

}
