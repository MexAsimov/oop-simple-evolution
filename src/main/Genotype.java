package main;

import java.util.*;

public class Genotype {
    private Random generator = new Random();
    private ArrayList<Integer> genotype = new ArrayList<>();

    Genotype() {
        for (int i = 0; i < 32; i++) {
            this.genotype.add(randomFromRange(0, 7));
        }
        while (!this.isCongruency())
            this.adaptToMoveAnywhere();
        Collections.sort(this.genotype);
    }

    Genotype(Genotype a, Genotype b) {
        int border1 = randomFromRange(1, 30);
        int border2 = randomFromRange(border1, 30);
        this.genotype.addAll(a.getGenotype().subList(0, border1));
        this.genotype.addAll(b.getGenotype().subList(border1, border2));
        this.genotype.addAll(a.getGenotype().subList(border2, 32));
        while (!this.isCongruency())
            this.adaptToMoveAnywhere();
        Collections.sort(this.genotype);
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Geno: ");
        for (Integer gene : this.genotype) {
            str.append(gene.toString());
        }
        return str.toString();
    }

    private void adaptToMoveAnywhere() {
        for (int i = 0; i <= 7; i++) {
            if (!this.genotype.contains(i)) {
                int index = randomFromRange(0, 31);
                this.genotype.remove(index);
                this.genotype.add(i);
            }
        }
    }

    // Function isCongruency() is necessary cause it's possible to remove another move option from Genotype with single occurrence
    private boolean isCongruency() {
        for (int i = 0; i <= 7; i++) {
            if (!this.genotype.contains(i))
                return false;
        }
        return true;
    }

    private int randomFromRange(int from, int to) {
        return from + generator.nextInt(1 + to - from);
    }

    public int[] getGenes() {
        int[] moveCounter = new int[8];
        for (int i = 0; i < 8; i++) {
            moveCounter[i] = 0;
        }
        for (int i = 0; i < 32; i++) {
            moveCounter[this.genotype.get(i)]++;
        }
        return moveCounter;
    }

    int randomRotate() {
        return this.genotype.get(randomFromRange(0, 31));
    }

    private ArrayList<Integer> getGenotype() {
        return this.genotype;
    }
}
