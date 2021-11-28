package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;


public class Plant extends AbstractWorldMapElement {
    private MapPosition mapPosition;

    public Plant() {
        super();
    }

    Plant(Field map, MapPosition mapPosition) {
        super(map);
        this.mapPosition = mapPosition;
        this.position = randomVector(map);
    }

    public String toString() {
        return "Plant: ";
    }

    private Vector2d randomVector(Field map) {
        TreeSet<Vector2d> set;
        if (this.mapPosition == MapPosition.JUNGLE)
            set = map.freeSlotsInJungle;
        else
            set = map.freeSlotsInGrassland;

        int randomIndex = generator.nextInt(set.size());

        Iterator iterator = set.iterator();
        for (int i = 0; i < randomIndex; i++) {
            iterator.next();
        }
        return (Vector2d) iterator.next();
    }
}
