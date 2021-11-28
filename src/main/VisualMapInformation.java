package main;

import javax.swing.*;
import java.awt.*;

public class VisualMapInformation extends JPanel implements IInformationObserver, Runnable {
    //NUMBER/COUNT OF:
    private int day;
    private int animals;
    ;
    private int plants;
    private int cells;
    private int birthRate;
    private int deathRate;

    private JLabel JLdayNumber = new JLabel();
    private JLabel JLnumberOfAnimals = new JLabel();
    private JLabel JLnumberOfPlants = new JLabel();
    private JLabel JLnumberOfCells = new JLabel();
    private JLabel JLbirthRate = new JLabel();
    private JLabel JLdeathRate = new JLabel();

    private Field map;

    VisualMapInformation(Field map) {
        Thread thread = new Thread(this);
        this.map = map;
        int maxCells = map.width * map.height;
        this.day = map.getDayCounter();
        this.animals = map.getNumberOfAnimals();
        this.plants = map.getNumberOfPlants();
        this.cells = maxCells;
        this.birthRate = map.getNumberOfBirths();
        this.deathRate = map.getNumberOfDeaths();

        setBorder(BorderFactory.createTitledBorder("Map info"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        this.JLdayNumber.setFont(new Font("Arial", Font.BOLD, 25));

        this.JLdayNumber.setText("Day: " + this.day);
        this.JLnumberOfAnimals.setText("Number of animals: " + this.animals);
        this.JLnumberOfPlants.setText("Number of plants: " + this.plants);
        this.JLnumberOfCells.setText("Number of free cells: " + this.cells);
        this.JLbirthRate.setText("Number of births: " + this.birthRate);
        this.JLdeathRate.setText("Number of deaths: " + this.deathRate);


        add(this.JLdayNumber);
        add(this.JLnumberOfAnimals);
        add(this.JLnumberOfPlants);
        add(this.JLnumberOfCells);
        add(this.JLbirthRate);
        add(this.JLdeathRate);

        thread.start();
    }

    private void alterDataInPanel() {
        this.JLdayNumber.setText("Day: " + this.day);
        this.JLnumberOfAnimals.setText("Number of animals: " + this.animals);
        this.JLnumberOfPlants.setText("Number of plants: " + this.plants);
        this.JLnumberOfCells.setText("Number of free cells: " + this.cells);
        this.JLbirthRate.setText("Number of births: " + this.birthRate);
        this.JLdeathRate.setText("Number of deaths: " + this.deathRate);
    }

    public void informationChanged(int day, int animals, int plants, int cells, int births, int deaths) {
        this.day = day;
        this.animals = animals;
        this.plants = plants;
        this.cells = cells;
        this.birthRate = births;
        this.deathRate = deaths;
    }

    public void run() {
        this.map.setInformationObserver(this);
        try {
            Thread.sleep(500);
            while (!(this.map.getNumberOfAnimals() == 0 && this.map.isInitialized)) {
                this.alterDataInPanel();
                Thread.sleep(this.map.delayInMS);
            }
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        this.alterDataInPanel();
        System.out.println(" exiting..");
    }
}
