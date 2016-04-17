package fr.xebia.dd;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DungeonTest {

    @Test
    public void should_display_south_dungeon() {
        Dungeon dungeon = new Dungeon("" +
                "+-----------+\n" +
                "|           |\n" +
                "    M       #\n" +
                "         P  #\n" +
                "#           #\n" +
                "#           #\n" +
                "#######  E###",
                "name", 0, 1);

        String displayedDungeon = dungeon.toString();

        assertThat(displayedDungeon).isEqualTo("" +
                "#############\n" +
                "#           #\n" +
                "#   M       #\n" +
                "#        P  #\n" +
                "#           #\n" +
                "#           #\n" +
                "#########E###");
    }

    @Test
    public void should_disappear_monster_when_dies() {
        Dungeon dungeon = new Dungeon("" +
                "#####\n" +
                "# MP#\n" +
                "#E###",
                "grooooot", 8, 20).withMonster(20, 10);

        String displayedDungeon = dungeon.left().right().toString();

        assertThat(displayedDungeon).isEqualTo("" +
                "#####\n" +
                "#  P#\n" +
                "#E###");
    }

    @Test
    public void should_disappear_player_when_it_dies() {
        Dungeon dungeon = new Dungeon("" +
                "####\n" +
                "#MP#\n" +
                "#E##",
                "player", 10, 20).withMonster(20, 20);

        String displayedDungeon = dungeon.left().toString();

        assertThat(displayedDungeon).isEqualTo("" +
                "####\n" +
                "#M #\n" +
                "#E##");
    }


    @Test
    public void should_display_north_dungeon() {
        Dungeon dungeon = new Dungeon("" +
                "#E#\n" +
                "#P#\n" +
                "###",
                "name", 0, 1);

        String displayedDungeon = dungeon.toString();

        assertThat(displayedDungeon).isEqualTo("" +
                "#E#\n" +
                "#P#\n" +
                "###");
    }

    @Test
    public void should_display_east_dungeon() {
        Dungeon dungeon = new Dungeon("" +
                "###\n" +
                "#PE\n" +
                "###",
                "name", 0, 1);

        String displayedDungeon = dungeon.toString();

        assertThat(displayedDungeon).isEqualTo("" +
                "###\n" +
                "#PE\n" +
                "###");
    }

    @Test
    public void should_display_west_dungeon() {
        Dungeon dungeon = new Dungeon("" +
                "###\n" +
                "EP#\n" +
                "###", "name", 0, 1);

        String displayedDungeon = dungeon.toString();

        assertThat(displayedDungeon).isEqualTo("" +
                "###\n" +
                "EP#\n" +
                "###");
    }

}
