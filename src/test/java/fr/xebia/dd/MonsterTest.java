package fr.xebia.dd;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class MonsterTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void should_fight_with_monster_and_be_killed() {
        Random mock = mock(Random.class);
        given(mock.nextBoolean()).willReturn(true);
        Dungeon dungeon = new Dungeon("" +
                "####\n" +
                "#MP#\n" +
                "#E##"
        ).withRandom(mock).createPlayer("player");

        dungeon.left();

        assertThat(dungeon.isGameOver()).isTrue();
        assertThat(systemOutRule.getLog().split("\n")).containsExactly("Player moved left", "Player fought against monster", "Monster killed player", "Game is over");
    }

    @Test
    public void should_fight_with_monster_and_kill_it() {
        Random mock = mock(Random.class);
        given(mock.nextBoolean()).willReturn(false);
        Dungeon dungeon = new Dungeon("" +
                "#E##\n" +
                "#PM#\n" +
                "####"
        ).withRandom(mock).createPlayer("player");

        dungeon.right();

        assertThat(systemOutRule.getLog().split("\n")).containsExactly("Player moved right", "Player fought against monster", "Player killed monster");
    }

}
