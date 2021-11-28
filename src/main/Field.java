package main;


import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Field implements IPositionChangeObserver, Runnable {
    private HashMap<Vector2d, Cell> mapBlocks = new HashMap<>();
    private HashMap<ElementID, Animal> mapAnimals = new HashMap<>(); // for future features
    private ArrayList<Animal> animalList = new ArrayList<>();
    private ArrayList<Plant> plantList = new ArrayList<>(); // for future features
    CopyOnWriteArrayList<Vector2d> changesMade = new CopyOnWriteArrayList<>();
    TreeSet<Vector2d> freeSlotsInGrassland = new TreeSet<>(Vector2d::compare);
    TreeSet<Vector2d> freeSlotsInJungle = new TreeSet<>(Vector2d::compare);

    private IInformationObserver informationObserver;

    private int dayCounter = 1;
    private int countOfPlants = 0;
    private int countOfAnimals = 0;
    private int countOfBirths = 0;
    private int countOfDeaths = 0;
    int incrementalAnimalID = 0;

    int width;
    int height;
    private int startEnergy;
    int moveEnergy;
    private int plantEnergy;
    private double jungleRatio;
    private int initialCountOfAnimals;
    private int jungleWidth;
    private int jungleHeight;
    private Vector2d bottomLeftJungle;
    private Vector2d topRightJungle;

    int delayInMS = 1000;
    boolean isInitialized = false;
    boolean isStarted = false;

    private Thread thread;

    public Field(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio, int initialCountOfAnimals) {
        this.width = width;
        this.height = height;
        this.moveEnergy = moveEnergy;
        this.startEnergy = startEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.initialCountOfAnimals = initialCountOfAnimals;
        this.jungleWidth = (int) Math.floor(width * jungleRatio);
        this.jungleHeight = (int) Math.floor(height * jungleRatio);
    }

    void initializeMap() {
        this.thread = new Thread(this);
        this.thread.start();
        this.defineJungleSpace();
        this.initializeFreeSlots();
        for (int i = 0; i < this.initialCountOfAnimals; i++) {
            if (this.freeSlotsInGrassland.isEmpty() && this.freeSlotsInJungle.isEmpty()) break;
            placeAnimal(new Animal(this, this.startEnergy, ++this.incrementalAnimalID));
        }
        isInitialized = true;
    }

    private void initializeFreeSlots() {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                Vector2d vector = new Vector2d(x, y);
                this.mapBlocks.put(vector, new Cell());
                if (isInJungle(vector))
                    this.freeSlotsInJungle.add(vector);
                else
                    this.freeSlotsInGrassland.add(vector);
            }
    }

    private void nextDay() throws InterruptedException {
        while (!this.isStarted) Thread.sleep(1000);
        this.changesMade.clear();
        this.dayCounter++;
        removeAnimals();
        runAnimalsActions(); // eating and reproducing inside
        if (!this.freeSlotsInJungle.isEmpty())
            placePlant(new Plant(this, MapPosition.JUNGLE));
        if (!this.freeSlotsInGrassland.isEmpty()) {
            placePlant(new Plant(this, MapPosition.GRASSLAND));
        }
        informationChanged();
    }

    private void defineJungleSpace() {
        int boundX1 = (int) Math.floor((this.width - this.jungleWidth) / 2.0);
        int boundX2 = (int) Math.floor((this.width + this.jungleWidth) / 2.0);
        int boundY1 = (int) Math.floor((this.height - this.jungleHeight) / 2.0);
        int boundY2 = (int) Math.floor((this.height + this.jungleHeight) / 2.0);

        this.bottomLeftJungle = new Vector2d(boundX1, boundY1);
        this.topRightJungle = new Vector2d(boundX2, boundY2);
    }

    private void placeAnimal(Animal animal) {
        this.countOfAnimals++;
        this.countOfBirths++;

        Vector2d position = animal.getPosition();

        setTakenDependsOnPlace(position);
        this.cellAt(position).add(animal);

        ElementID animalID = animal.getID();
        this.mapAnimals.put(animalID, animal);
        this.animalList.add(animal);
        animal.addObserver(this);
    }

    private void removeAnimal(Animal animal) {
        this.countOfAnimals--;
        this.countOfDeaths++;
        Vector2d position = animal.getPosition();
        this.cellAt(position).remove(animal);
        this.animalList.remove(animal);
        // Don't remove animal from mapAnimals - useful if we need history of him
        setFreeDependsOnPlace(position);
        animal.removeObserver(this);// TODO: zastanów się
    }

    private void removeAnimals() {
        ArrayList<Animal> animalsToRemove = new ArrayList<>();
        for (Animal animal : this.animalList) {
            if (animal.isDead()) {
                animalsToRemove.add(animal);
            }
        }
        for (Animal animal : animalsToRemove) {
            this.removeAnimal(animal);
        }
    }

    private void placePlant(Plant plant) {
        this.countOfPlants++;
        Vector2d position = plant.getPosition();
        setTakenDependsOnPlace(position);
        this.cellAt(position).add(plant);
        this.plantList.add(plant);
    }

    private void removePlant(Vector2d position) {
        this.countOfPlants--;
        Cell cellWithPlant = this.cellAt(position);
        Plant plant = (Plant) cellWithPlant.getLast();
        this.plantList.remove(plant);
        cellWithPlant.removeLast();
        setFreeDependsOnPlace(position);
    }

    private void runAnimalsActions() {
        ArrayList<Vector2d> positionsWithAction = new ArrayList<>();
        for (Animal animal : animalList) {
            animal.move();
            positionsWithAction.add(animal.getPosition());
        }
        positionsWithAction = duplicatesCleaner(positionsWithAction);
        ArrayList<Animal> newAnimals = new ArrayList<>();
        for (Vector2d vector : positionsWithAction) {
            Cell currentCell = this.cellAt(vector);
            //eating
            if (currentCell.isWithPlant()) {
                ArrayList<Animal> chosenAnimals = currentCell.getStrongestAnimals();
                shareFood(chosenAnimals);
                removePlant(vector);
            }
            //reproducing
            if (currentCell.getCountOfAnimals() > 1) {
                ArrayList<Animal> animalPair = currentCell.getTwoStrongestAnimals();
                Animal animal1 = animalPair.get(0);
                Animal animal2 = animalPair.get(1);
                Animal babyAnimal = animal1.reproduce(animal2);
                if (babyAnimal.getEnergy() != 0)
                    newAnimals.add(babyAnimal);
            }
        }
        for (Animal babyAnimal : newAnimals) {
            this.placeAnimal(babyAnimal);
        }
    }

    private void shareFood(ArrayList<Animal> animalList) {
        int countOfAnimals = animalList.size();
        int foodRation = (int) Math.floor((float) this.plantEnergy / (float) countOfAnimals);
        for (Animal animal : animalList) {
            animal.eat(foodRation);
        }
    }

    private boolean isOccupied(Vector2d position) {
        if (this.cellAt(position) == null) return false;
        return !this.cellAt(position).getSet().isEmpty();
    }

    ArrayList<Vector2d> adjacentPositions(Vector2d position) {
        ArrayList<Vector2d> result = new ArrayList<>();
        int x = position.x;
        int y = position.y;
        result.add(cutPosition(new Vector2d(x + 1, y)));
        result.add(cutPosition(new Vector2d(x + 1, y + 1)));
        result.add(cutPosition(new Vector2d(x, y + 1)));
        result.add(cutPosition(new Vector2d(x - 1, y + 1)));
        result.add(cutPosition(new Vector2d(x - 1, y)));
        result.add(cutPosition(new Vector2d(x - 1, y - 1)));
        result.add(cutPosition(new Vector2d(x, y - 1)));
        result.add(cutPosition(new Vector2d(x + 1, y - 1)));
        return result;
    }

    ArrayList<Vector2d> findFreePlacesAround(ArrayList<Vector2d> adjacentPos) {
        ArrayList<Vector2d> result = new ArrayList<>();
        for (Vector2d position : adjacentPos) {
            position = cutPosition(position);
            if (!this.isOccupied(position)) {
                result.add(position);
            }
        }
        return result;
    }

    Cell cellAt(Vector2d position) {
        return this.mapBlocks.get(position);
    }

    int getNumberOfAnimals() {
        return this.countOfAnimals;
    }

    int getNumberOfPlants() {
        return this.countOfPlants;
    }

    int getStartEnergy() {
        return this.startEnergy;
    }

    int getDayCounter() {
        return this.dayCounter;
    }

    private int getNumberOfFreeCells() {
        return this.freeSlotsInGrassland.size() + this.freeSlotsInJungle.size();
    }

    int getNumberOfBirths() {
        return this.countOfBirths;
    }

    int getNumberOfDeaths() {
        return this.countOfDeaths;
    }

    private boolean isInJungle(Vector2d v) {
        return v.follows(this.bottomLeftJungle) && v.precedes(this.topRightJungle);
    }

    void setInformationObserver(IInformationObserver observer) {
        this.informationObserver = observer;
    }

    private void freeSlotIn(TreeSet<Vector2d> set, Vector2d position) {
        if (!this.isOccupied(position)) {
            set.add(position);
            this.changesMade.add(position);
        }
    }

    private void setFreeDependsOnPlace(Vector2d position) {
        if (this.isInJungle(position))
            this.freeSlotIn(this.freeSlotsInJungle, position);
        else
            this.freeSlotIn(this.freeSlotsInGrassland, position);
    }

    private void takeSlotIn(TreeSet<Vector2d> set, Vector2d position) {
        if (!this.isOccupied(position)) {
            set.remove(position);
        }
        this.changesMade.add(position);
    }

    private void setTakenDependsOnPlace(Vector2d position) {
        if (this.isInJungle(position))
            this.takeSlotIn(this.freeSlotsInJungle, position);
        else
            this.takeSlotIn(this.freeSlotsInGrassland, position);
    }

    Vector2d cutPosition(Vector2d position) {
        int x = position.x;
        int y = position.y;
        if (position.x > this.width - 1) x = position.x % this.width;
        else if (position.x < 0) x = this.width - 1 + position.x;
        if (position.y > this.height - 1) y = position.y % this.height;
        else if (position.y < 0) y = this.height - 1 + position.y;
        return new Vector2d(x, y);
    }

    private void informationChanged() {
        if (this.informationObserver != null)
            this.informationObserver.informationChanged(dayCounter, this.countOfAnimals, this.countOfPlants, this.getNumberOfFreeCells(), this.countOfBirths, this.countOfDeaths);
    }

    public void positionChanged(ElementID elementID, Vector2d oldPosition, Vector2d newPosition) {
        Animal tempAnimal = this.mapAnimals.get(elementID);
        Cell elementSetFrom = cellAt(oldPosition);
        Cell elementSetTo = cellAt(newPosition);

        elementSetFrom.remove(tempAnimal);
        setFreeDependsOnPlace(oldPosition);
        setTakenDependsOnPlace(newPosition);
        elementSetTo.add(tempAnimal);
    }

    public void run() {
        try {
            Thread.sleep(500);
            while (!(this.getNumberOfAnimals() == 0 && this.isInitialized)) {
                this.nextDay();
                Thread.sleep(delayInMS);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println(" exiting..");
    }

    static public <T> ArrayList<T> duplicatesCleaner(ArrayList<T> listWithDuplicates) {
        return (ArrayList<T>) listWithDuplicates.stream().distinct().collect(Collectors.toList());
    }
}
