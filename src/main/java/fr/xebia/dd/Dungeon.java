package fr.xebia.dd;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

class Dungeon {

    private Player player;
    private int playerX;
    private int playerY;
    private boolean gameOver = false;

    private final int width;
    private final int height;
    private final String exitDirection;
    private final int exitPosition;
    private final List<Consumer<String>> eventConsumers;

    private static String findLine(String[] lines, char c) {
        for (String line : lines) {
            if (line.indexOf(c) != -1) {
                return line;
            }
        }
        throw new IllegalArgumentException();
    }

    private static int findY(String[] lines, char c) {
        for (int y = 0; y < lines.length; y++) {
            if (lines[y].indexOf(c) != -1) {
                return y;
            }
        }
        throw new IllegalArgumentException();
    }

    private static String computeDirection(int exitX, int exitY, int width, int height) {
        if (exitX == 0         ) return "east";
        if (exitX == width + 1 ) return "west";
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
        this(asciiArt.split("\n"));
    }

    private Dungeon(String[] lines) {
        this(lines, computeDirection(findLine(lines, 'E').indexOf('E'), findY(lines, 'E'), lines[0].length() - 2, lines.length - 2));
    }

    private Dungeon(String[] lines, String exitDirection) {
        this(
                lines[0].length() - 2,
                lines.length - 2,
                findLine(lines, 'P').indexOf('P') - 1,
                findY(lines, 'P') - 1,
                exitDirection,
                findPosition(exitDirection, findLine(lines, 'E').indexOf('E'), findY(lines, 'E'))
        );
    }

    private Dungeon(int width, int height, int playerX, int playerY, String exitDirection, int exitPosition) {
        this.width = width;
        this.height = height;
        this.playerX = playerX;
        this.playerY = playerY;
        this.exitDirection = exitDirection;
        this.exitPosition = exitPosition;
        this.eventConsumers = new ArrayList<>();
    }

    Optional<Player> player() {
        return ofNullable(player);
    }

    Dungeon subscribe(Consumer<String> eventConsumer) {
        eventConsumers.add(eventConsumer);
        return this;
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
                () -> playerX == 0 && "east".equals(exitDirection) && playerY == exitPosition,
                () -> playerX--);
    }

    Dungeon right() {
        return move("right",
                () -> playerX < width - 1,
                () -> playerX == width - 1 && "west".equals(exitDirection) && exitPosition == playerY,
                () -> playerX++);
    }

    Dungeon down() {
        return move("down",
                () -> playerY < height - 1,
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
            eventConsumers.forEach(consumer -> consumer.accept("Player moved " + direction));
        }
        if (gameOver) {
            eventConsumers.forEach(consumer -> consumer.accept("Game is over"));
        }
        return this;
    }

}
