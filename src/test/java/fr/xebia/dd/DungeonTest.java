package fr.xebia.dd;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DungeonTest {

    @Rule
    public SystemOutRule systemOutRule = new SystemOutRule().enableLog().mute();

    @Test
    public void golden_ticket_generator() throws IOException {
        for (int i = 0; i < 1000; i++) {
            Dungeon.setRandom(new Random(i));
            File testFile = new File("/home/dolounet/dev/test-fixtures/test" + i);
            testFile.delete();
            testFile.createNewFile();
            try (FileWriter testFileWriter = new FileWriter(testFile)){
                systemOutRule.clearLog();
                Dungeon.main(new String[0]);
                testFileWriter.append(systemOutRule.getLog());
            }
        }
    }

}
