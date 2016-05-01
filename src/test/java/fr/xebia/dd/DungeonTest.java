package fr.xebia.dd;

import com.google.common.io.Resources;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class DungeonTest {

    @Rule
    public SystemOutRule systemOutRule = new SystemOutRule().enableLog().mute();

    @Test
    @Ignore
    public void golden_ticket_generator() throws IOException {
        for (int i = 0; i < 1000; i++) {
            Dungeon.setRandom(new Random(i));
            File testFile = new File("/home/dolounet/dev/test-fixtures/test" + i);
            testFile.delete();
            testFile.createNewFile();
            try (FileWriter testFileWriter = new FileWriter(testFile)) {
                systemOutRule.clearLog();
                Dungeon.main(new String[0]);
                testFileWriter.append(systemOutRule.getLog());
            }
        }
    }

    @Test
    public void should_be_consistent_with_golden_ticket() throws Exception {
        for (int i = 0; i < 1000; i++) {
            systemOutRule.clearLog();
            Dungeon.setRandom(new Random(i));
            File file = new File(Resources.getResource("usecases/monster-kills-player-input.txt").toURI());
            Dungeon.setInputFile(file);
            String testFileContent = new String(Files.readAllBytes(Paths.get("/home/dolounet/dev/test-fixtures/test" + i)));

            Dungeon.main(new String[0]);

            assertThat(systemOutRule.getLog()).isEqualTo(testFileContent);
        }
    }
}
