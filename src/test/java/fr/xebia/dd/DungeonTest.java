package fr.xebia.dd;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class DungeonTest {

    @Rule
    public SystemOutRule systemOut = new SystemOutRule().enableLog().mute();

    @Rule
    public TextFromStandardInputStream systemIn = emptyStandardInputStream();

    @Test
    public void should_execute_one_scenario() {
        systemIn.provideLines("" +
                "96\n" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n");

        Dungeon.main(new String[]{});

        assertThat(systemOut.getLog()).isEqualTo("seed - nothing for pure random> dungeon as ascii art:\n" +
                "Player ixl9mWQFn has strength 12 with 3hp wearing Gauntlets of Ogre Power +2dmg\n" +
                "Monster with force 8 and 20hp.\n" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n" +
                "Player moved down\n" +
                "Player moved down\n" +
                "Player moved down\n" +
                "Player moved down\n" +
                "Player moved right\n" +
                "Player moved up\n" +
                "Player moved down\n" +
                "Player moved left\n" +
                "Player moved up\n" +
                "Player moved left\n" +
                "Player moved right\n" +
                "Player moved down\n" +
                "Player moved left\n" +
                "Player moved left\n" +
                "Player moved left\n" +
                "Player moved right\n" +
                "Player moved up\n" +
                "Player moved up\n" +
                "Player moved left\n" +
                "Player moved left\n" +
                "Player moved left\n" +
                "Player moved up\n" +
                "Player fight against monster\n" +
                "Player hit monster with 14 damage.\n" +
                "Monster survives and got 6 hp\n" +
                "Monster hits Player with 8 damage.\n" +
                "Monster killed player\n" +
                "Game is over\n" +
                "###########\n" +
                "#         #\n" +
                "#         #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n" +
                "\n");
    }

}
