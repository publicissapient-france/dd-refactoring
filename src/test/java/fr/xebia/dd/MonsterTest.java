package fr.xebia.dd;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class MonsterTest {

    @Test
    public void should_fight_with_monster_and_be_killed() {
        List<String> lines = new ArrayList<>();
        Random mock = mock(Random.class);
        given(mock.nextBoolean()).willReturn(true);
        Dungeon dungeon = new Dungeon("" +
                "####\n" +
                "#MP#\n" +
                "#E##"
        ).withRandom(mock).subscribe(lines::add).createPlayer("player");

        dungeon.left();

        assertThat(dungeon.isGameOver()).isTrue();
        assertThat(lines).containsExactly("Player moved left", "Player fought against monster", "Monster killed player", "Game is over");
    }

    @Test
    public void should_fight_with_monster_and_kill_it() {
        List<String> lines = new ArrayList<>();
        Random mock = mock(Random.class);
        given(mock.nextBoolean()).willReturn(false);
        Dungeon dungeon = new Dungeon("" +
                "#E##\n" +
                "#PM#\n" +
                "####"
        ).withRandom(mock).subscribe(lines::add).createPlayer("player");

        dungeon.right();

        assertThat(lines).containsExactly("Player moved right", "Player fought against monster", "Player killed monster");
    }

}
