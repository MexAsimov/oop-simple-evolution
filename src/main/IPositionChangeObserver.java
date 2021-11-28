package main;

public interface IPositionChangeObserver {
    void positionChanged(ElementID elementID, Vector2d oldPosition, Vector2d newPosition);
}
