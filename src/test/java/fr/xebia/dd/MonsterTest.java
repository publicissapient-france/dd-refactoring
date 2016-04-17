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
        Dungeon dungeon = new Dungeon("" +
                "####\n" +
                "#MP#\n" +
                "#E##",
                "player", 10, 20).withMonster(20, 20);
        systemOutRule.clearLog();

        dungeon.left();

        assertThat(dungeon.isGameOver()).isTrue();
        assertThat(systemOutRule.getLog().split("\n")).containsExactly(
                "Player moved left",
                "Player fight against monster",
                "Player hit monster with 13 damage.",
                "Monster survives and got 7 hp",
                "Monster hits Player with 20 damage.",
                "Monster killed player",
                "Game is over");
    }

    @Test
    public void should_fight_with_monster_and_kill_it() {
        Random mock = mock(Random.class);
        given(mock.nextBoolean()).willReturn(false);
        Dungeon dungeon = new Dungeon("" +
                "#E##\n" +
                "#PM#\n" +
                "####",
                "grooooot", 8, 20).withMonster(20, 10);
        systemOutRule.clearLog();

        dungeon.right();

        assertThat(dungeon.isGameOver()).isFalse();
        assertThat(systemOutRule.getLog().split("\n")).containsExactly(
                "Player moved right",
                "Player fight against monster",
                "Player hit monster with 15 damage.",
                "grooooot  killed monster "
        );
    }

}
