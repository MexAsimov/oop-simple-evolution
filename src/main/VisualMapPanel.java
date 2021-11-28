package main;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class VisualMapPanel extends JPanel implements Runnable {

    private static Color animalColor = new Color(129, 2, 244);
    private static Color plantColor = new Color(30, 184, 0);
    private static Color groundColor = new Color(225, 204, 58);


//    private static ImageIcon BASEAnimalIcon = new ImageIcon("src\\img\\animal.png");
//    private static ImageIcon BASEPlantIcon = new ImageIcon("src\\img\\plant.png");
//    private static ImageIcon BASEFreeSlotIcon = new ImageIcon("src\\img\\freeSlot.png");
//    private ImageIcon animalIcon;
//    private ImageIcon plantIcon;
//    private ImageIcon freeSlotIcon;

    private Field map;
    private JButton[][] cells;

    VisualMapPanel(Field map) {
        Thread thread = new Thread(this);
        this.map = map;
        setBackground(Color.GREEN);

        int cellSizeSingleDimension = (int) Math.floor(Toolkit.getDefaultToolkit().getScreenSize().height * 0.75 / (float) Math.max(map.width, map.height));
        Dimension cellSize = new Dimension(cellSizeSingleDimension, cellSizeSingleDimension);

//        this.animalIcon = new ImageIcon(BASEAnimalIcon.getImage().getScaledInstance(cellSizeSingleDimension, cellSizeSingleDimension, Image.SCALE_SMOOTH));
//        this.plantIcon = new ImageIcon(BASEPlantIcon.getImage().getScaledInstance(cellSizeSingleDimension, cellSizeSingleDimension, Image.SCALE_SMOOTH));
//        this.freeSlotIcon = new ImageIcon(BASEFreeSlotIcon.getImage().getScaledInstance(cellSizeSingleDimension, cellSizeSingleDimension, Image.SCALE_SMOOTH));

        this.cells = new JButton[map.width][map.height];
        Border border = BorderFactory.createEmptyBorder();

        for (int i = 0; i < map.width; i++) {
            for (int j = 0; j < map.height; j++) {
                this.cells[i][j] = new JButton();
                this.cells[i][j].setPreferredSize(cellSize);
                this.cells[i][j].setBorder(border);
                this.cells[i][j].setBackground(groundColor);
                //this.cells[i][j].setIcon(this.freeSlotIcon);
                this.cells[i][j].addActionListener(this::get);
            }
        }

        JPanel scene = new JPanel(new GridBagLayout());
        this.add(scene);


        GridBagConstraints gbc = new GridBagConstraints();
        int width = map.width;
        int height = map.height;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gbc.gridx = i;
                gbc.gridy = j;
                gbc.gridwidth = 1;
                gbc.gridheight = 1;
                scene.add(this.cells[i][j], gbc);
            }
        }


        thread.start();
    }

    private void mapChanged() {
        for (Vector2d vector : this.map.changesMade) {
            if (this.map.cellAt(vector).isWithAnimal())
                animalOnCell(vector.x, vector.y);
            else if (this.map.cellAt(vector).isWithPlant())
                plantOnCell(vector.x, vector.y);
            else
                groundOnCell(vector.x, vector.y);
        }
    }

    public void run() {
        try {
            Thread.sleep(2000);
            while (!(this.map.getNumberOfAnimals() == 0 && this.map.isInitialized)) {
                this.mapChanged();
                Thread.sleep(this.map.delayInMS);
            }
            Thread.sleep(3000);
            this.mapChanged();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println(" exiting..");
    }

    private void get(ActionEvent actionEvent) {

    }

    private void animalOnCell(int x, int y) {
        this.cells[x][y].setBackground(animalColor);
        //this.cells[x][y].setIcon(this.animalIcon);
    }

    private void plantOnCell(int x, int y) {
        this.cells[x][y].setBackground(plantColor);
        //this.cells[x][y].setIcon(this.plantIcon);
    }

    private void groundOnCell(int x, int y) {
        this.cells[x][y].setBackground(groundColor);
        //this.cells[x][y].setIcon(this.freeSlotIcon);
    }
}
