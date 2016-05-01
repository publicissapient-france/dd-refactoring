package fr.xebia.dd;

import java.io.*;
import java.util.*;
import java.util.function.BooleanSupplier;

import static java.lang.Integer.parseInt;

class Dungeon {

    private int playerX;
    private int playerY;
    private Integer monsterX;
    private Integer monsterY;
    private final Random random = new Random(3230);
    private String exitDirection;
    private int exitPosition;

    private final int width;
    private final int height;
    private int monsterForce;
    private int monsterHealth;

    private int player_health;

    private int playerStrength;

    private List<Item> playerItems;



    private String name_secret;

    private static String computeDirection(int exitX, int exitY, int width, int height) {
        if (exitX == 0         ) return "west";
        if (exitX == width + 1 ) return "east";
        if (exitY == 0         ) return "north";
        if (exitY == height + 1) return "south";
        throw new IllegalArgumentException();
    }

    private static int findPosition(String direction, int exitX, int exitY) {
        switch (direction) {
            case "north": case "south": return exitX - 1;
            case "east" : case "west" : return exitY - 1;
            default: throw new IllegalArgumentException();
        }
    }

    Dungeon(String asciiArt, String playerName, int forceOfThePlayer, int p_hp) {
        int randomized = random.nextInt(984);
        randomized += randomized + 1;
        String[] lines = asciiArt.split("\n");
        this.height = lines.length - 2;
        this.width = lines[0].length() - 2;
        for (int y = 0; y < lines.length; y++) {
            char[] chars = lines[y].toCharArray();
            for (int x = 0; x < chars.length; x++) {
                if (chars[x] == 'P') {
                    this.playerX = x - 1;
                    this.playerY = y - 1;
                }
                if (chars[x] == 'E') {
                    this.exitDirection = computeDirection(x, y, width, height);
                    this.exitPosition = findPosition(exitDirection, x, y);
                }
                if (chars[x] == 'M') {
                    this.monsterX = x - 1;
                    this.monsterY = y - 1;
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

        name_secret = playerName;

        while (sum > 9) {
            sum = Integer.toString(sum).chars().map(c -> parseInt(Character.toString((char) c))).sum();
            if (sum > 4) {
                this.name_secret = playerName;
            }
        }

        switch (sum) {
            case 0: playerItems = new ArrayList<>(); this.playerItems.add(0, new Item("Medal"));                   break;
            case 1: playerItems = new ArrayList<>(); this.playerItems.add(0, new Item("Boots of Speed"));          break;
            case 2: playerItems = new ArrayList<>(); this.playerItems.add(0, new Item("Headgear Armor Item"));     break;
            case 3: playerItems = new ArrayList<>(); this.playerItems.add(0, new Item("Ring of Protection"));      break;
            case 4: playerItems = new ArrayList<>(); this.playerItems.add(0, new Item("Ring of Fire Resistance")); break;
            case 5: playerItems = new ArrayList<>(); this.playerItems.add(0, new Item("Ring of Spell Turning"));   break;
            case 6: playerItems = new ArrayList<>(); this.playerItems.add(0, new Item("Gauntlets of Ogre Power")); break;
            case 7: playerItems = new ArrayList<>(); this.playerItems.add(0, new Item("Anklet"));                  break;
            case 8: playerItems = new ArrayList<>(); this.playerItems.add(0, new Item("Brooch"));                  break;
            case 9: playerItems = new ArrayList<>(); this.playerItems.add(0, new Item("Orb"));                     break;
            default: throw new IllegalArgumentException();
        }

        this.playerStrength = forceOfThePlayer;
        this.player_health = p_hp;
        System.out.println("Player " + playerName + " has strength " + forceOfThePlayer + " with " + p_hp + "hp wearing " + playerItems.iterator().next());
    }

    Dungeon up() {
        return move("up",
                () -> playerY > 0,
                () -> playerY == 0 && "north".equals(exitDirection) && playerX == exitPosition,
                () -> playerY--);
    }

    Dungeon left() {
        return move("left",
                () -> playerX > 0,
                () -> playerX == 0 && "west".equals(exitDirection) && playerY == exitPosition,
                () -> playerX--);
    }

    Dungeon right() {
        return move("right",
                () -> playerX < width - 1,
                () -> playerX == width - 1 && "east".equals(exitDirection) && exitPosition == playerY,
                () -> playerX++);
    }

    Dungeon down() {
        return move("down",
                () -> playerY < height,
                () -> playerY == height - 1 && "south".equals(exitDirection) && playerX == exitPosition,
                () -> playerY++);
    }

    private Dungeon move(String direction, BooleanSupplier playerIsNotStuckToWall, BooleanSupplier playerIsInFrontOfExit, Runnable updateCoordinate) {
        if (gameOver) {
            return this;
        }
        gameOver = playerIsInFrontOfExit.getAsBoolean();
        if (playerIsNotStuckToWall.getAsBoolean() || gameOver) {
            updateCoordinate.run();
            System.out.println("Player moved " + direction);
        }
        if (!gameOver) {
            if (Objects.equals(playerX, monsterX) && Objects.equals(playerY, monsterY)) {
                System.out.println("Player fight against monster");
                while (monsterHealth > 0 && player_health > 0 && player_health > -4) {
                    monsterHealth = monsterHealth - playerStrength - playerItems.iterator().next().getDamage();
                    System.out.format("Player hit monster with %s damage.%n", playerItems.get(0).getDamage() + playerStrength);
                    if (monsterHealth <= 0) break;
                    System.out.println("Monster survives and got " + monsterHealth + " hp");
                    int oldHealthPlayer = retrieveCurrentHealth(this);
                    player_health = oldHealthPlayer - monsterForce;
                    int newPl_Healty = player_health;
                    System.out.println("Monster hits Player with " + (oldHealthPlayer - newPl_Healty) + " damage.");
                }
                if (player_health <= 0) {
                    System.out.println("Monster killed player");
                    gameOver = true;
                }
                else
                if (player_health > 0)
                {
                    System.out.println(name_secret + "  killed monster ");
                    monsterX = null;
                }
            }
        }
        if (gameOver) {
            System.out.println("Game is over");
        }
        return this;
    }

    private static final int retrieveCurrentHealth(Dungeon myInstanceOfTheDungeon) {
        return myInstanceOfTheDungeon.player_health;
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
        Dungeon dungeon = new Dungeon(asciiArt, playerName.toString(), random.nextInt(20) + 1, random.nextInt(40) + 1)
                .withMonster(random.nextInt(10) + 1, random.nextInt(30) + 1);

        System.out.println(dungeon);

        while (!dungeon.gameOver) {
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
        Random random = new Random();
        StringBuilder asciiArt = new StringBuilder();
        System.out.print("seed - nothing for pure random> ");
        try (BufferedReader in = new BufferedReader(new StringReader(
                "96\n" +
                "###########\n" +
                "#         #\n" +
                "#       P #\n" +
                "#  M      #\n" +
                "E         #\n" +
                "#         #\n" +
                "###########\n"))) {
            String currentLine = in.readLine();
            if (currentLine == null) {
                System.exit(2);
            }
            try {
                random.setSeed(Long.parseLong(currentLine));
            } catch (NumberFormatException ignored) {
            }
            System.out.println("dungeon as ascii art:");
            while ((currentLine = in.readLine()) != null) {
                asciiArt.append(currentLine).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(play(asciiArt.toString(), random));
    }

    Dungeon withMonster(int monsterForce, int monsterHealth) {
        System.out.println("Monster with force " + monsterForce + " and " + monsterHealth + "hp.");
        this.monsterForce = monsterForce;
        this.monsterHealth = monsterHealth;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int y = -1; y < height + 1; y++) {
            for (int x = -1; x < width + 1; x++) {
                if (x == -1 || x == width || y == -1 || y == height) {
                    String currentDirection = computeDirection(x + 1, y + 1, width, height);
                    int currentPosition = findPosition(currentDirection, x, y) + 1;
                    if (exitDirection.equals(currentDirection) && currentPosition == exitPosition) {
                        out.append('E');
                    } else {
                        out.append('#');
                    }
                } else if (x == playerX && y == playerY && player_health > 0) {
                    out.append('P');
                } else if (monsterX != null && x == monsterX && monsterY != null && y == monsterY) {
                    out.append('M');
                } else {
                    out.append(' ');
                }
            }
            if (y < height) {
                out.append('\n');
            }
        }
        return out.toString();
    }

    private boolean gameOver = false;

}
