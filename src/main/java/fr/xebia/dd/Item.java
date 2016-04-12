package fr.xebia.dd;

import java.util.Objects;

class Item {

    private final String name;
    private int damage;

    Item(String name) {
        this.name = name;
        this.damage = 1;
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
}
