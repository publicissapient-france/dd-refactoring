package fr.xebia.dd;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.assertj.core.api.Assertions.assertThat;

public class GameOverTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    private Dungeon dungeon;

    @Test
    public void should_end_game_north() {
        dungeon =  new Dungeon("" +
                "#E#\n" +
                "#P#\n" +
                "###",
                "player", 10, 10);
        systemOutRule.clearLog();

        dungeon.up();

        assertThat(systemOutRule.getLog().split("\n")).contains("Game is over");
    }

    @Test
    public void should_end_game_south() {
        dungeon = new Dungeon("" +
                "###\n" +
                "#P#\n" +
                "#E#",
                "player", 10, 10);
        systemOutRule.clearLog();

        dungeon.down();

        assertThat(systemOutRule.getLog().split("\n")).contains("Game is over");
    }


    @Test
    public void should_end_game_east() {
        dungeon = new Dungeon("" +
                "###\n" +
                "#PE\n" +
                "###",
                "player", 10, 10);
        systemOutRule.clearLog();

        dungeon.right();

        assertThat(systemOutRule.getLog().split("\n")).contains("Game is over");
    }

    @Test
    public void should_end_game_west() {
        dungeon = new Dungeon("" +
                "###\n" +
                "EP#\n" +
                "###",
                "player", 10, 10);
        systemOutRule.clearLog();

        dungeon.left();

        assertThat(systemOutRule.getLog().split("\n")).contains("Game is over");
    }

    @Test
    public void should_not_move_when_player_is_stuck_to_the_wall() {
        dungeon = new Dungeon("" +
                "###\n" +
                "EP#\n" +
                "###",
                "player", 10, 10);
        systemOutRule.clearLog();

        dungeon.up();

        assertThat(systemOutRule.getLog()).isEmpty();
    }

    @Test
    public void should_not_move_when_game_is_over() {
        dungeon = new Dungeon("" +
                "#E#\n" +
                "#P#\n" +
                "###",
                "player", 10, 10);
        dungeon.up();
        systemOutRule.clearLog();

        dungeon.up();

        assertThat(systemOutRule.getLog()).isEmpty();
    }

    @Test
    public void should_works_with_big_dungeon() {
        dungeon = new Dungeon("" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#         #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########",
                "player", 10, 10);
        systemOutRule.clearLog();

        dungeon
                .up()
                .up() // can't goes up
                .right().down().left().down()
                .left().left().left().left().left().left().left()
                .left() // can't goes left
                .down()
                .left(); // exit

        assertThat(systemOutRule.getLog().split("\n")).containsExactly(
                "Player moved up",
                "Player moved right", "Player moved down", "Player moved left", "Player moved down",
                "Player moved left", "Player moved left", "Player moved left", "Player moved left", "Player moved left", "Player moved left", "Player moved left",
                "Player moved down",
                "Player moved left",
                "Game is over");
    }

}
