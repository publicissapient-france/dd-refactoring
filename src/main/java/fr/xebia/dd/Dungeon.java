package fr.xebia.dd;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.BooleanSupplier;

import static java.util.Optional.ofNullable;

class Dungeon {

    private Player player;
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

    Dungeon(String asciiArt) {
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
    }

    Optional<Player> player() {
        return ofNullable(player);
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

    boolean isGameOver() {
        return gameOver;
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
                while (monsterHealth > 0 && player.getHealth() > 0 && player.getHealth() > -4) {
                    monsterHealth = monsterHealth - player.getForce() - player.items().iterator().next().getDamage();
                    System.out.format("Player hit monster with %s damage.%n", player.items().get(0).getDamage() + player.getForce());
                    if (monsterHealth <= 0) break;
                    System.out.println("Monster survives and got " + monsterHealth + " hp");
                    int oldHealthPlayer = retrieveCurrentHealth(player);
                    player.setHealth(oldHealthPlayer - monsterForce);
                    int newPl_Healty = player.getHealth();
                    System.out.println("Monster hits Player with " + (oldHealthPlayer - newPl_Healty) + " damage.");
                }
                if (player.getHealth() <= 0) {
                    System.out.println("Monster killed player");
                    gameOver = true;
                }
                else
                if (player.getHealth() > 0)
                {
                    System.out.println(player + "  killed monster ");
                    monsterX = null;
                }
            }
        }
        if (gameOver) {
            System.out.println("Game is over");
        }
        return this;
    }

    private static final int retrieveCurrentHealth(Player player) {
        return player.getHealth();
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
        Dungeon dungeon = new Dungeon(asciiArt)
                .createPlayer(playerName.toString(), random.nextInt(20) + 1, random.nextInt(40) + 1)
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
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
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

    Dungeon createPlayer(String name, int force, int health) {
        this.player = new Player(name, force, health);
        return this;
    }

    Dungeon createPlayer(String playerName, int force, int health, Item item) {
        this.player = new Player(playerName, force, health, item);
        return this;
    }

    Dungeon withMonster(int monsterForce, int monsterHealth) {
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
                } else if (x == playerX && y == playerY && player != null && player.getHealth() > 0) {
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
