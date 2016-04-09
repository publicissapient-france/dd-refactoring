package fr.xebia.dd;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GameOverTest {

    @Test
    public void should_end_game_north() {
        List<String> events = new ArrayList<>();
        Dungeon dungeon = new Dungeon("" +
                "#E#\n" +
                "#P#\n" +
                "###"
        ).subscribe(events::add).createPlayer("player");

        dungeon.up();

        assertThat(dungeon.isGameOver()).isTrue();
        assertThat(events).containsExactly("Player moved up", "Game is over");
    }

    @Test
    public void should_end_game_south() {
        List<String> events = new ArrayList<>();
        Dungeon dungeon = new Dungeon("" +
                "###\n" +
                "#P#\n" +
                "#E#"
        ).subscribe(events::add).createPlayer("player");

        dungeon.down();

        assertThat(dungeon.isGameOver()).isTrue();
        assertThat(events).containsExactly("Player moved down", "Game is over");
    }

    @Test
    public void should_end_game_east() {
        List<String> events = new ArrayList<>();
        Dungeon dungeon = new Dungeon("" +
                "###\n" +
                "#PE\n" +
                "###"
        ).subscribe(events::add).createPlayer("player");

        dungeon.right();

        assertThat(dungeon.isGameOver()).isTrue();
        assertThat(events).containsExactly("Player moved right", "Game is over");
    }

    @Test
    public void should_end_game_west() {
        List<String> events = new ArrayList<>();
        Dungeon dungeon = new Dungeon("" +
                "###\n" +
                "EP#\n" +
                "###"
        ).subscribe(events::add).createPlayer("player");

        dungeon.left();

        assertThat(dungeon.isGameOver()).isTrue();
        assertThat(events).containsExactly("Player moved left", "Game is over");
    }

    @Test
    public void should_not_move_when_player_is_stuck_to_the_wall() {
        List<String> events = new ArrayList<>();
        Dungeon dungeon = new Dungeon("" +
                "###\n" +
                "EP#\n" +
                "###"
        ).subscribe(events::add).createPlayer("player");

        dungeon.up();

        assertThat(events).isEmpty();
    }

    @Test
    public void should_not_move_when_game_is_over() {
        List<String> events = new ArrayList<>();
        Dungeon dungeon = new Dungeon("" +
                "#E#\n" +
                "#P#\n" +
                "###"
        ).subscribe(events::add).createPlayer("player").up();

        dungeon.up();

        assertThat(dungeon.isGameOver()).isTrue();
        assertThat(events).containsExactly("Player moved up", "Game is over");
    }

    @Test
    public void should_works_with_big_dungeon() {
        List<String> events = new ArrayList<>();
        Dungeon dungeon = new Dungeon("" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#         #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########"
        ).subscribe(events::add).createPlayer("player");

        dungeon
                .up()
                .up() // can't goes up
                .right().down().left().down()
                .left().left().left().left().left().left().left()
                .left() // can't goes left
                .down()
                .left(); // exit

        assertThat(dungeon.isGameOver()).isTrue();
        assertThat(events).containsExactly(
                "Player moved up",
                "Player moved right", "Player moved down", "Player moved left", "Player moved down",
                "Player moved left", "Player moved left", "Player moved left", "Player moved left", "Player moved left", "Player moved left", "Player moved left",
                "Player moved down",
                "Player moved left",
                "Game is over");
    }

}
