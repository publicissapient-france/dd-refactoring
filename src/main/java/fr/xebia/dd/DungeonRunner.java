package fr.xebia.dd;

import com.google.common.io.Resources;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.BooleanSupplier;

import static java.lang.Integer.parseInt;

class DungeonRunner {

    private static File inputFile;
    private static Random random = new Random();

    static {
        try {
            setInputFile(new File(Resources.getResource("usecases/monster-kills-player-input.txt").toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private final Dungeon dungeon = new Dungeon();


    static String computeDirection(int exitX, int exitY, int width, int height) {
        if (exitX == 0         ) return "west";
        if (exitX == width + 1 ) return "east";
        if (exitY == 0         ) return "north";
        if (exitY == height + 1) return "south";
        throw new IllegalArgumentException();
    }

    static int findPosition(String direction, int exitX, int exitY) {
        switch (direction) {
            case "north": case "south": return exitX - 1;
            case "east" : case "west" : return exitY - 1;
            default: throw new IllegalArgumentException();
        }
    }

    DungeonRunner(String asciiArt, String playerName, int forceOfThePlayer, int p_hp) {
        int randomized = getRandom().nextInt(984);
        randomized += randomized + 1;
        String[] lines = asciiArt.split("\n");
        this.dungeon.setHeight(lines.length - 2);
        this.dungeon.setWidth(lines[0].length() - 2);
        for (int y = 0; y < lines.length; y++) {
            char[] chars = lines[y].toCharArray();
            for (int x = 0; x < chars.length; x++) {
                if (chars[x] == 'P') {
                    this.dungeon.setPlayerX(x - 1);
                    this.dungeon.setPlayerY(y - 1);
                }
                if (chars[x] == 'E') {
                    this.dungeon.setExitDirection(computeDirection(x, y, dungeon.getWidth(), dungeon.getHeight()));
                    this.dungeon.setExitPosition(findPosition(dungeon.getExitDirection(), x, y));
                }
                if (chars[x] == 'M') {
                    this.dungeon.setMonsterX(x - 1);
                    this.dungeon.setMonsterY(y - 1);
                }
            }
        }

        int sum = playerName.toUpperCase().chars().map(c -> {
            switch (c) {
                case 'A': case 'F': case 'K': case 'P': case 'U': case 'Z': return 0;
                case 'B': case 'G': case 'L': case 'Q': case 'V':           return 1;
                case 'C': case 'H': case 'M': case 'R': case 'W':           return 2;
                case 'D': case 'I': case 'N': case 'S': case 'X':           return 3;
                case 'E': case 'J': case 'O': case 'T': case 'Y':           return 4;
                case '0': case '5':                                         return 5;
                case '1': case '6':                                         return 6;
                case '2': case '7': case '!': case '?': case '-': case '&': return 7;
                case '3': case '8':                                         return 8;
                case '4': case '9':                                         return 9;
                default: throw new IllegalArgumentException();
            }
        }).sum();

        dungeon.setName_secret(playerName);

        while (sum > 9) {
            sum = Integer.toString(sum).chars().map(c -> parseInt(Character.toString((char) c))).sum();
            if (sum > 4) {
                this.dungeon.setName_secret(playerName);
            }
        }

        switch (sum) {
            case 0:
                dungeon.setPlayerItems(new ArrayList<>());
                this.dungeon.getPlayerItems().add(0, new Item("Medal"));                   break;
            case 1:
                dungeon.setPlayerItems(new ArrayList<>());
                this.dungeon.getPlayerItems().add(0, new Item("Boots of Speed"));          break;
            case 2:
                dungeon.setPlayerItems(new ArrayList<>());
                this.dungeon.getPlayerItems().add(0, new Item("Headgear Armor Item"));     break;
            case 3:
                dungeon.setPlayerItems(new ArrayList<>());
                this.dungeon.getPlayerItems().add(0, new Item("Ring of Protection"));      break;
            case 4:
                dungeon.setPlayerItems(new ArrayList<>());
                this.dungeon.getPlayerItems().add(0, new Item("Ring of Fire Resistance")); break;
            case 5:
                dungeon.setPlayerItems(new ArrayList<>());
                this.dungeon.getPlayerItems().add(0, new Item("Ring of Spell Turning"));   break;
            case 6:
                dungeon.setPlayerItems(new ArrayList<>());
                this.dungeon.getPlayerItems().add(0, new Item("Gauntlets of Ogre Power")); break;
            case 7:
                dungeon.setPlayerItems(new ArrayList<>());
                this.dungeon.getPlayerItems().add(0, new Item("Anklet"));                  break;
            case 8:
                dungeon.setPlayerItems(new ArrayList<>());
                this.dungeon.getPlayerItems().add(0, new Item("Brooch"));                  break;
            case 9:
                dungeon.setPlayerItems(new ArrayList<>());
                this.dungeon.getPlayerItems().add(0, new Item("Orb"));                     break;
            default: throw new IllegalArgumentException();
        }

        this.dungeon.setPlayerStrength(forceOfThePlayer);
        this.dungeon.setPlayer_health(p_hp);
        System.out.println("Player " + playerName + " has strength " + forceOfThePlayer + " with " + p_hp + "hp wearing " + dungeon.getPlayerItems().iterator().next());
    }

    public static Random getRandom() {
        return random;
    }

    public static void setRandom(Random random) {
        DungeonRunner.random = random;
    }

    public static File getInputFile() {
        return inputFile;
    }

    public static void setInputFile(File inputFile) {
        DungeonRunner.inputFile = inputFile;
    }

    DungeonRunner up() {
        return dungeon.up();
    }

    DungeonRunner left() {
        return dungeon.left();
    }

    DungeonRunner right() {
        return dungeon.right();
    }

    DungeonRunner down() {
        return dungeon.down();
    }

    private DungeonRunner move(String direction, BooleanSupplier playerIsNotStuckToWall, BooleanSupplier playerIsInFrontOfExit, Runnable updateCoordinate) {
        return dungeon.move(direction, playerIsNotStuckToWall, playerIsInFrontOfExit, updateCoordinate);
    }

    static final int retrieveCurrentHealth(Dungeon myInstanceOfTheDungeon) {
        return myInstanceOfTheDungeon.getPlayer_health();
    }

    static String play(String asciiArt, Random random) {
        PrintStream systemOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        StringBuilder playerName = new StringBuilder();
        final String possibleCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?-&";
        for (int i = 0; i < random.nextInt(10) + 5; i++) {
            playerName.append(possibleCharacters.charAt(random.nextInt(possibleCharacters.length())));
        }
        Dungeon dungeon = new DungeonRunner(asciiArt, playerName.toString(), random.nextInt(20) + 1, random.nextInt(40) + 1)
                .withMonster(random.nextInt(10) + 1, random.nextInt(30) + 1);

        System.out.println(dungeon);

        while (!dungeon.isGameOver()) {
            switch (random.nextInt(4)) {
                case 0: dungeon.up(); break;
                case 1: dungeon.down(); break;
                case 2: dungeon.left(); break;
                case 3: dungeon.right();
            }
        }

        System.out.println(dungeon);

        System.setOut(systemOut);

        return out.toString();
    }

    public static void main(String[] args) {
        StringBuilder asciiArt = new StringBuilder();
        try (BufferedReader in = new BufferedReader(getIn())) {
            System.out.println("dungeon as ascii art:");
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                asciiArt.append(currentLine).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println(play(asciiArt.toString(), getRandom()));
    }

    private static Reader getIn() throws FileNotFoundException, URISyntaxException {
        return new InputStreamReader(
               new FileInputStream(getInputFile()));
    }

    Dungeon withMonster(int monsterForce, int monsterHealth) {
        return dungeon.withMonster(monsterForce, monsterHealth);
    }

    @Override
    public String toString() {
        return dungeon.toString();
    }

}
