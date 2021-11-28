package main;


import java.util.*;

public class Cell {
    // TODO: change structure of the list to CopyOnWriteArrayList
    private SortedSet<AbstractWorldMapElement> objectSet = new TreeSet<>(this::compare);
    private Random generator = new Random();

    Cell() {
    }

    public void add(AbstractWorldMapElement element) {
        this.objectSet.add(element);
    }

    void remove(AbstractWorldMapElement element) {
        Iterator iterator = this.objectSet.iterator();
        while (iterator.hasNext()) {
            AbstractWorldMapElement tmp = (AbstractWorldMapElement) iterator.next();
            if (tmp.equals(element)) {
                iterator.remove();
            }
        }
    }

    void removeLast() {
        this.remove(this.objectSet.last());
    }

    private Animal getStrongestAnimal() {
        if (this.objectSet.first() instanceof Plant) return new Animal(0);
        return (Animal) this.objectSet.first();
    }

    ArrayList<Animal> getTwoStrongestAnimals() {
        ArrayList<Animal> animalList = this.getOnlyAnimals();
        ArrayList<Animal> strongestAnimals = this.getStrongestAnimals(animalList);
        if (strongestAnimals.size() == 2) return strongestAnimals;
        else if (strongestAnimals.size() == 1) {
            strongestAnimals.add(this.getSecondStrongest(animalList));
            return strongestAnimals;
        } else {
            return randomGetFromArrayList(strongestAnimals, 2);
        }
    }

    ArrayList<Animal> getStrongestAnimals() {
        ArrayList<Animal> animalList = this.getOnlyAnimals();
        ArrayList<Animal> resultList = new ArrayList<>();
        Iterator iterator = animalList.iterator();
        Animal animal = getStrongestAnimal();
        int maxEnergy = animal.getEnergy();
        while (iterator.hasNext() && animal.getEnergy() == maxEnergy) {
            AbstractWorldMapElement element = (AbstractWorldMapElement) iterator.next();
            resultList.add(animal);
            if (element instanceof Plant) break;
            animal = (Animal) element;
        }
        return resultList;
    }

    private ArrayList<Animal> getStrongestAnimals(ArrayList<Animal> animalList) {
        ArrayList<Animal> resultList = new ArrayList<>();
        Iterator iterator = animalList.iterator();
        Animal animal = getStrongestAnimal();
        int maxEnergy = animal.getEnergy();
        while (iterator.hasNext() && animal.getEnergy() == maxEnergy) {
            AbstractWorldMapElement element = (AbstractWorldMapElement) iterator.next();
            resultList.add(animal);
            if (element instanceof Plant) break;
            animal = (Animal) element;
        }
        return resultList;
    }

    private Animal getSecondStrongest(ArrayList<Animal> animalList) {
        // random if more than one
        // used just in case if there is one stronger than some other
        ArrayList<Animal> tmpAnimalList = new ArrayList<>(animalList);
        tmpAnimalList.remove(getStrongestAnimal());
        return randomGetFromArrayList(getStrongestAnimals(tmpAnimalList), 1).get(0);
    }

    boolean isWithAnimal() {
        if (this.objectSet.isEmpty()) return false;
        return this.objectSet.first() instanceof Animal;
    }

    boolean isWithPlant() {
        if (this.objectSet.isEmpty()) return false;
        if (this.objectSet.last() instanceof Plant) {
            return true;

        }
        return false;
    }

    SortedSet<AbstractWorldMapElement> getSet() {
        return objectSet;
    }

    int getCountOfAnimals() {
        return this.getOnlyAnimals().size();
    }

    AbstractWorldMapElement getLast() {
        return this.objectSet.last();
    }

    private ArrayList<Animal> getOnlyAnimals() {
        ArrayList<Animal> animalList = new ArrayList<>();
        Iterator iterator = this.objectSet.iterator();
        while (iterator.hasNext()) {
            AbstractWorldMapElement element = (AbstractWorldMapElement) iterator.next();
            if (element instanceof Animal) {
                animalList.add((Animal) element);
            }
        }
        return animalList;
    }

    private int compare(AbstractWorldMapElement v1, AbstractWorldMapElement v2) {
        if (v1 instanceof Plant) return 1;
        if (v2 instanceof Plant) return -1;
        Animal o1 = (Animal) v1;
        Animal o2 = (Animal) v2;
        if (o1.getEnergy() < o2.getEnergy()) return 1;
        else if (o1.getEnergy() > o2.getEnergy()) return -1;
        return ElementID.compare(o1.getID(), o2.getID());
    }

    private ArrayList<Animal> randomGetFromArrayList(ArrayList<Animal> inputArrayList, int numberOfElements) {

        ArrayList<Animal> inputList = new ArrayList<>(inputArrayList);
        ArrayList<Animal> resultList = new ArrayList<>();
        for (int i = 0; i < numberOfElements; i++) {
            Iterator iterator = inputList.iterator();
            int index = this.generator.nextInt(inputList.size());
            for (int j = 0; j < index; j++) iterator.next();
            resultList.add((Animal) iterator.next());
            iterator.remove();
        }
        return resultList;
    }
}
