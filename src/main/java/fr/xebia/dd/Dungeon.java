package fr.xebia.dd;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.BooleanSupplier;

import static java.util.Optional.ofNullable;

class Dungeon {

    private Player player;
    private int playerX;
    private int playerY;
    private boolean gameOver = false;
    private Integer monsterX;
    private Integer monsterY;
    private Random random;
    private String exitDirection;
    private int exitPosition;

    private final int width;
    private final int height;

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

    Dungeon withRandom(Random random) {
        this.random = random;
        return this;
    }

    Optional<Player> player() {
        return ofNullable(player);
    }

    Dungeon createPlayer(String name) {
        this.player = new Player(name);
        return this;
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
                System.out.println("Player fought against monster");
                if (random.nextBoolean()) {
                    System.out.println("Monster killed player");
                    gameOver = true;
                } else {
                    System.out.println("Player killed monster");
                }
            }
        }
        if (gameOver) {
            System.out.println("Game is over");
        }
        return this;
    }

}
