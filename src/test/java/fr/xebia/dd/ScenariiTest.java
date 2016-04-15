package fr.xebia.dd;

import org.assertj.core.api.AbstractCharSequenceAssert;
import org.junit.Test;

import java.util.Random;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Math.random;
import static org.assertj.core.api.Assertions.assertThat;

public class ScenariiTest {

    @Test
    public void should_play_when_player_kills_monster() {
        assertWithMaybeOutputThat(Dungeon.play("" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########", new Random(89))
        ).isEqualTo("" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n" +
                "Player moved right\nPlayer moved up\nPlayer moved left\nPlayer moved left\nPlayer moved down\n" +
                "Player moved left\nPlayer moved left\nPlayer moved left\nPlayer moved up\nPlayer moved right\n" +
                "Player moved right\nPlayer moved left\nPlayer moved right\nPlayer moved right\nPlayer moved down\n" +
                "Player moved left\nPlayer moved left\nPlayer moved down\nPlayer moved down\nPlayer moved up\n" +
                "Player moved up\nPlayer moved left\nPlayer moved left\nPlayer moved down\n" +
                "Player fight against monster\n" +
                "Player hit monster with 8 damage.\n" +
                "Monster survives and got 7 hp\n" +
                "Monster hits Player with 4 damage.\n" +
                "Player hit monster with 8 damage.\n" +
                "rWrPmq6  killed monster \n" +
                "Player moved left\nPlayer moved right\nPlayer moved down\nPlayer moved left\nPlayer moved down\n" +
                "Player moved up\nPlayer moved left\nPlayer moved right\nPlayer moved left\nPlayer moved left\n" +
                "Game is over\n" +
                "###########\n" +
                "#         #\n" +
                "#         #\n" +
                "#         #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n");
    }

    @Test
    public void should_play_when_monster_kills_player() {
        assertWithMaybeOutputThat(Dungeon.play("" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########", new Random(96))
        ).isEqualTo("" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n" +
                "Player moved down\nPlayer moved down\nPlayer moved down\nPlayer moved down\nPlayer moved right\n" +
                "Player moved up\nPlayer moved down\nPlayer moved left\nPlayer moved up\nPlayer moved left\n" +
                "Player moved right\nPlayer moved down\nPlayer moved left\nPlayer moved left\nPlayer moved left\n" +
                "Player moved right\nPlayer moved up\nPlayer moved up\nPlayer moved left\nPlayer moved left\n" +
                "Player moved left\nPlayer moved up\nPlayer fight against monster\n" +
                "Player hit monster with 13 damage.\n" +
                "Monster survives and got 7 hp\n" +
                "Monster hits Player with 8 damage.\n" +
                "Monster killed player\n" +
                "Game is over\n" +
                "###########\n" +
                "#         #\n" +
                "#         #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n");
    }

    @Test
    public void should_play_when_player_escape_without_fighting_against_monster() {
        assertWithMaybeOutputThat(Dungeon.play("" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########", new Random(56))
        ).isEqualTo("" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n" +
                "Player moved left\nPlayer moved left\nPlayer moved down\nPlayer moved right\nPlayer moved left\n" +
                "Player moved up\nPlayer moved left\nPlayer moved down\nPlayer moved down\nPlayer moved left\n" +
                "Player moved left\nPlayer moved left\nPlayer moved left\nPlayer moved up\nPlayer moved down\n" +
                "Player moved up\nPlayer moved down\nPlayer moved up\nPlayer moved down\nPlayer moved left\n" +
                "Game is over\n" +
                "###########\n" +
                "#         #\n" +
                "#         #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n");
    }

    @Test
    public void should_compare_two_scenarios() {
        int seed = Double.valueOf(random() * 100).intValue();
        assertThat(Dungeon.play("" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n", new Random(seed))
        ).isEqualTo(Dungeon.play("" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n", new Random(seed)));

    }

    private static AbstractCharSequenceAssert<?, String> assertWithMaybeOutputThat(String executionResult) {
        if (parseBoolean(System.getProperty("output", "false"))) {
            System.out.println(executionResult);
        }
        return assertThat(executionResult);
    }

}
