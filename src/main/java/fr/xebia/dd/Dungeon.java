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

    Dungeon(int width, int height, int playerX, int playerY, String exitDirection, int exitPosition) {
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
