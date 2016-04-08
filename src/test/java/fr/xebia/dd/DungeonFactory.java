package fr.xebia.dd;

class DungeonFactory {

    static Dungeon createDungeon(String dungeon) {
        String[] lines = dungeon.split("\n");
        int width = lines[0].length() - 2;
        int height = lines.length - 2;
        int playerX = findLine(lines, 'P').indexOf('P') - 1;
        int playerY = findY(lines, 'P') - 1;
        int exitX = findLine(lines, 'E').indexOf('E');
        int exitY = findY(lines, 'E');
        String direction = computeDirection(exitX, exitY, width, height);
        int position = findPosition(direction, exitX, exitY);
        return new Dungeon(width, height, playerX, playerY, direction, position);
    }

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
        if (exitX == 0) return "east";
        if (exitX == width + 1) return "west";
        if (exitY == 0) return "north";
        if (exitY == height + 1) return "south";
        throw new IllegalArgumentException();
    }

    private static int findPosition(String direction, int exitX, int exitY) {
        switch (direction) {
            case "north":
            case "south":
                return exitX - 1;
            case "east":
            case "west":
                return exitY - 1;
            default:
                throw new IllegalArgumentException();
        }
    }

}
