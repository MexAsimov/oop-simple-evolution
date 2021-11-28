package main;

import java.util.Random;

public abstract class AbstractWorldMapElement {
    Vector2d position;
    Field map;
    Random generator = new Random();

    public AbstractWorldMapElement() {

    }

    public AbstractWorldMapElement(Field map) {
        this.map = map;
    }

    public AbstractWorldMapElement(Field map, Vector2d initialPosition) {
        this.map = map;
        this.position = initialPosition;
    }

    public Vector2d getPosition() {
        return this.position;
    }
}
