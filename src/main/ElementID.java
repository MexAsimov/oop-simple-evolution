package main;

public class ElementID {
    int animalID;

    ElementID(int animalId) {
        this.animalID = animalId;
    }

    public long getID() {
        return this.animalID;
    }

    static int compare(ElementID id1, ElementID id2) {
        return Integer.compare(id1.animalID, id2.animalID);
    }
}
