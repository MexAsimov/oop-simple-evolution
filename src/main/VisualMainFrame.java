package main;

import javax.swing.*;
import java.awt.*;

public class VisualMainFrame extends JFrame {
    public JPanel firstMapOptions;
    public JPanel secondMapOptions;

    public VisualMainFrame(Field map1, Field map2) {
        super("Animal Evolution 2.1.37");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(500, 500));
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Container panel = this.getContentPane();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        this.firstMapOptions = new VisualControlPanel(map1);
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(firstMapOptions, gridBagConstraints);

        JPanel firstMapInfo = new VisualMapInformation(map1);
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panel.add(firstMapInfo, gridBagConstraints);

        JPanel firstMapAnimalSelect = new VisualSelectedInfo(map1);
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panel.add(firstMapAnimalSelect, gridBagConstraints);

        JPanel firstMap = new VisualMapPanel(map1);
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new Insets(0, 0, 0, 2);
        panel.add(firstMap, gridBagConstraints);


        this.secondMapOptions = new VisualControlPanel(map2);
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        panel.add(secondMapOptions, gridBagConstraints);

        JPanel secondMapInfo = new VisualMapInformation(map2);
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        panel.add(secondMapInfo, gridBagConstraints);

        JPanel secondMapAnimalSelect = new VisualSelectedInfo(map2);
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        panel.add(secondMapAnimalSelect, gridBagConstraints);

        JPanel secondMap = new VisualMapPanel(map2);
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new Insets(0, 2, 0, 0);
        panel.add(secondMap, gridBagConstraints);
    }
}
