package fr.xebia.dd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

import static fr.xebia.dd.DungeonFactory.createDungeon;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class FirstItemTest {

    @Parameters(name = "{1} should have {0}")
    public static Iterable<String[]> itemForPlayers() {
        return Arrays.asList(new String[][]{
                {"Medal", "Akuku"},
                {"Medal", "Zapuk"},
                {"Boots of Speed", "Kayin"},
                {"Boots of Speed", "Sofia"},
                {"Headgear Armor Item", "Jacob"},
                {"Headgear Armor Item", "Lynn"},
                {"Ring of Protection", "Thor"},
                {"Ring of Protection", "Jennie"},
                {"Ring of Fire Resistance", "Seth"},
                {"Ring of Spell Turning", "Ralph"},
                {"Ring of Spell Turning", "Jess"},
                {"Ring of Spell Turning", "Emily"},
                {"Gauntlets of Ogre Power", "Dan"},
                {"Gauntlets of Ogre Power", "Ann"},
                {"Anklet", "Duke"},
                {"Anklet", "Wendy"},
                {"Brooch", "Axel"},
                {"Brooch", "Zelda"},
                {"Orb", "Aaron"},
                {"Orb", "Rosa"}
        });
    }

    @Parameter
    public String itemName;

    @Parameter(1)
    public String playerName;

    @Test
    public void should_give_an_item_when_player_is_created() {
        Player player = createDungeon("" +
                "###\n" +
                "EP#\n" +
                "###").createPlayer(playerName).player().orElseThrow(AssertionError::new);

        List<Item> items = player.items();

        assertThat(items).containsExactly(new Item(itemName));
    }

}
