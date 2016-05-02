package fr.xebia.dd;

import com.google.common.io.Resources;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class DungeonTest {

    public static final String MONSTER_KILLS_PLAYER_INPUT = "monster-kills-player-input";
    public static final String PLAYER_ESCAPES_WITHOUT_FIGHTING_INPUT = "player-escapes-without-fighting-input";
    public static final String PLAYER_KILLS_MONSTER_INPUT = "player-kills-monster-input";

    // TODO Change this directory into the one you want to store golden tickets scenarios
    public static final String GOLDEN_TICKET_STORAGE_DIRECTORY = "/home/dolounet/dev/test-fixtures/test";

    @Rule
    public SystemOutRule systemOutRule = new SystemOutRule().enableLog().mute();

    @Test
    @Ignore
    public void golden_ticket_generator() throws Exception {
        for (int i = 0; i < 1000; i++) {
            writeScenarioIteration(i, MONSTER_KILLS_PLAYER_INPUT);
            writeScenarioIteration(i, PLAYER_ESCAPES_WITHOUT_FIGHTING_INPUT);
            writeScenarioIteration(i, PLAYER_KILLS_MONSTER_INPUT);
        }
    }

    @Test
    public void should_be_consistent_with_golden_ticket() throws Exception {
        for (int i = 0; i < 1000; i++) {
            assertScenarioIteration(i, MONSTER_KILLS_PLAYER_INPUT);
            assertScenarioIteration(i, PLAYER_ESCAPES_WITHOUT_FIGHTING_INPUT);
            assertScenarioIteration(i, PLAYER_KILLS_MONSTER_INPUT);
        }
    }

    private void writeScenarioIteration(int i, String scenarioName) throws URISyntaxException, IOException {
        systemOutRule.clearLog();
        Dungeon.setRandom(new Random(i));
        Dungeon.setInputFile(inputFileOf(scenarioName));
        File goldenTicketFile = new File(GOLDEN_TICKET_STORAGE_DIRECTORY + scenarioName + i);
        reinitGoldenTicketFile(goldenTicketFile);
        try (FileWriter testFileWriter = new FileWriter(goldenTicketFile)) {
            Dungeon.main(new String[0]);
            testFileWriter.append(systemOutRule.getLog());
        }
    }

    private void reinitGoldenTicketFile(File outputFile) throws IOException {
        outputFile.delete();
        outputFile.createNewFile();
    }

    private void assertScenarioIteration(int i, String scenarioName) throws URISyntaxException, IOException {
        systemOutRule.clearLog();
        Dungeon.setRandom(new Random(i));
        Dungeon.setInputFile(inputFileOf(scenarioName));
        String testFileContent = new String(Files.readAllBytes(Paths.get(GOLDEN_TICKET_STORAGE_DIRECTORY + scenarioName + i)));

        Dungeon.main(new String[0]);

        assertThat(systemOutRule.getLog()).isEqualTo(testFileContent);
    }

    private File inputFileOf(String scenarioName) throws URISyntaxException {
        return new File(Resources.getResource("usecases/" + scenarioName + ".txt").toURI());
    }
}
