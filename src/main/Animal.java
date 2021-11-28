package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Animal extends AbstractWorldMapElement {
    private ElementID animalID;
    private int energy;
    private MapDirection direction = MapDirection.NORTH;
    private Vector2d previousPosition;
    private Genotype genotype;
    private int moveEnergy;

    private ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    // TODO: DONE Is map really know about our plans?? I am not sure, but Baby has to be placed on map too...

    //additional constructor for some purposes
    Animal(int energy) {
        super();
        this.energy = energy;
    }

    // for a placed Animal
    Animal(Field map, int energy, int animalID) {
        super(map);
        this.animalID = new ElementID(animalID);
        this.direction = direction.rotate(this.generator.nextInt(8));
        this.energy = energy;
        this.genotype = new Genotype();
        this.moveEnergy = map.moveEnergy;
        this.position = randomVector(map);
    }

    // for a baby Animal
    private Animal(Field map, Vector2d initialPosition, int energy, Genotype genotype, int animalID) {
        super(map, initialPosition);
        this.animalID = new ElementID(animalID);
        this.direction = direction.rotate(this.generator.nextInt(8));
        this.energy = energy;
        this.genotype = genotype;
    }

    public String toString() {
        return "Animal: " + this.getEnergy() + " " + this.getID().animalID;
    }

    void move() {
        this.previousPosition = this.position;
        Vector2d tmp = this.previousPosition.add(this.direction.toUnitVector());
        this.position = this.map.cutPosition(tmp);
        this.energy -= this.moveEnergy;
        positionChanged();
        this.direction = this.direction.rotate(this.genotype.randomRotate());
    }

    void eat(int foodToEat) {
        this.energy += foodToEat;
    }

    Animal reproduce(Animal partner) {
        int energyToReproduce = (int) Math.ceil(this.map.getStartEnergy() * 0.5);
        // I don't have to check this.energy, coz animal on which I am executing this function is stronger or as strong as partner
        if (partner.getEnergy() < energyToReproduce) {
            return new Animal(0);
        }// TODO: change it back
        int energy = (int) Math.floor(this.energy * 0.25 + partner.energy * 0.25);
        this.loseEnergyAfterReproducing();
        partner.loseEnergyAfterReproducing();
        Vector2d position;
        ArrayList<Vector2d> adjacentPos = this.map.adjacentPositions(this.position);
        ArrayList<Vector2d> freePositionsAround = this.map.findFreePlacesAround(adjacentPos);
        if (freePositionsAround.isEmpty()) {
            position = adjacentPos.get(this.generator.nextInt(8));
        } else {
            position = freePositionsAround.get(this.generator.nextInt(freePositionsAround.size()));
        }

        return new Animal(this.map, position, energy, new Genotype(this.genotype, partner.getGenotype()), ++this.map.incrementalAnimalID);
    }

    private void loseEnergyAfterReproducing() {
        this.energy = (int) Math.floor(this.energy * 0.75);
    }

    void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    private void positionChanged() {
        for (IPositionChangeObserver observer : this.observers) {
            observer.positionChanged(this.animalID, this.previousPosition, this.position);
        }
    }

    boolean isDead() {
        return this.energy < this.moveEnergy;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    int getEnergy() {
        return this.energy;
    }

    ElementID getID() {
        return this.animalID;
    }

    private Genotype getGenotype() {
        return this.genotype;
    }

    private Vector2d randomVector(Field map) {
        double chooseLocation = this.generator.nextDouble();
        double jungleRate = 0.3;
        TreeSet<Vector2d> set;

        if (chooseLocation < jungleRate) {
            if (map.freeSlotsInJungle.isEmpty())
                set = map.freeSlotsInGrassland;
            else
                set = map.freeSlotsInJungle;
        } else {
            if (map.freeSlotsInGrassland.isEmpty())
                set = map.freeSlotsInJungle;
            else
                set = map.freeSlotsInGrassland;
        }

        int randomIndex = generator.nextInt(set.size());
        Iterator iterator = set.iterator();
        for (int i = 0; i < randomIndex; i++) {
            iterator.next();
        }
        return (Vector2d) iterator.next();
    }
}
