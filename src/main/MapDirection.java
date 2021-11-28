package main;

import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public String toString() {
        switch (this) {
            case NORTH:
                return "polnoc";
            case NORTHEAST:
                return "polnocny wschod";
            case NORTHWEST:
                return "polnocny zachod";

            case SOUTH:
                return "poludnie";
            case SOUTHEAST:
                return "poludniowy wschod";
            case SOUTHWEST:
                return "poludniowy zachod";

            case WEST:
                return "zachod";

            case EAST:
                return "wschod";

            default:
                return "";
        }
    }

    public MapDirection rotate(int rotateNumber) {
        MapDirection[] tab = MapDirection.values();
        int index = 0;
        for (; tab[index] != this; index++) ;
        index += rotateNumber;
        index %= tab.length;
        return tab[index];
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH:
                return new Vector2d(0, 1);
            case NORTHEAST:
                return new Vector2d(1, 1);
            case NORTHWEST:
                return new Vector2d(-1, 1);
            case SOUTH:
                return new Vector2d(0, -1);
            case SOUTHEAST:
                return new Vector2d(1, -1);
            case SOUTHWEST:
                return new Vector2d(-1, -1);
            case EAST:
                return new Vector2d(1, 0);
            case WEST:
            default:
                return new Vector2d(-1, 0);
        }
    }
}
