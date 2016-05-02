package fr.xebia.dd;

import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public class Dungeon {
    int playerX;

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    int playerY;

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    Integer monsterX;

    public Integer getMonsterX() {
        return monsterX;
    }

    public void setMonsterX(Integer monsterX) {
        this.monsterX = monsterX;
    }

    Integer monsterY;

    public Integer getMonsterY() {
        return monsterY;
    }

    public void setMonsterY(Integer monsterY) {
        this.monsterY = monsterY;
    }

    String exitDirection;

    public String getExitDirection() {
        return exitDirection;
    }

    public void setExitDirection(String exitDirection) {
        this.exitDirection = exitDirection;
    }

    int exitPosition;

    public int getExitPosition() {
        return exitPosition;
    }

    public void setExitPosition(int exitPosition) {
        this.exitPosition = exitPosition;
    }

    int width;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    int height;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    int monsterForce;
    int monsterHealth;
    int player_health;

    public int getPlayer_health() {
        return player_health;
    }

    public void setPlayer_health(int player_health) {
        this.player_health = player_health;
    }

    int playerStrength;

    public int getPlayerStrength() {
        return playerStrength;
    }

    public void setPlayerStrength(int playerStrength) {
        this.playerStrength = playerStrength;
    }

    List<Item> playerItems;

    public List<Item> getPlayerItems() {
        return playerItems;
    }

    public void setPlayerItems(List<Item> playerItems) {
        this.playerItems = playerItems;
    }

    String name_secret;

    public String getName_secret() {
        return name_secret;
    }

    public void setName_secret(String name_secret) {
        this.name_secret = name_secret;
    }

    boolean gameOver = false;

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Dungeon() {
    }

    DungeonRunner up() {
        return move("up",
                () -> playerY > 0,
                () -> playerY == 0 && "north".equals(exitDirection) && playerX == exitPosition,
                () -> playerY--);
    }

    DungeonRunner left() {
        return move("left",
                () -> playerX > 0,
                () -> playerX == 0 && "west".equals(exitDirection) && playerY == exitPosition,
                () -> playerX--);
    }

    DungeonRunner right() {
        return move("right",
                () -> playerX < width - 1,
                () -> playerX == width - 1 && "east".equals(exitDirection) && exitPosition == playerY,
                () -> playerX++);
    }

    DungeonRunner down() {
        return move("down",
                () -> playerY < height,
                () -> playerY == height - 1 && "south".equals(exitDirection) && playerX == exitPosition,
                () -> playerY++);
    }

    DungeonRunner move(String direction, BooleanSupplier playerIsNotStuckToWall, BooleanSupplier playerIsInFrontOfExit, Runnable updateCoordinate) {
        if (gameOver) {
            return null;
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
                    int oldHealthPlayer = DungeonRunner.retrieveCurrentHealth(this);
                    player_health = oldHealthPlayer - monsterForce;
                    int newPl_Healty = player_health;
                    System.out.println("Monster hits Player with " + (oldHealthPlayer - newPl_Healty) + " damage.");
                }
                if (player_health <= 0) {
                    System.out.println("Monster killed player");
                    gameOver = true;
                } else if (player_health > 0) {
                    System.out.println(name_secret + "  killed monster ");
                    monsterX = null;
                }
            }
        }
        if (gameOver) {
            System.out.println("Game is over");
        }
        return null;
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
                    String currentDirection = DungeonRunner.computeDirection(x + 1, y + 1, width, height);
                    int currentPosition = DungeonRunner.findPosition(currentDirection, x, y) + 1;
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
}