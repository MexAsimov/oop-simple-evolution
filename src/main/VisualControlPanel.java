package main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VisualControlPanel extends JPanel implements Runnable {
    public int mainDelay = 1000;
    Field map;

    public VisualControlPanel(Field map) {
        setBorder(BorderFactory.createTitledBorder("Configuration Panel"));

        this.map = map;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton start = new JButton("Play");
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        start.addActionListener(this::start);
        add(start, gbc);

        JButton stop = new JButton("Stop");
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        stop.addActionListener(this::stop);
        add(stop, gbc);

        final int FPS_MIN = 1;
        final int FPS_MAX = 10;
        final int FPS_INIT = 1;

        JLabel speedLabel = new JLabel("Frames Per Second", JLabel.CENTER);
        speedLabel.setVerticalAlignment(JLabel.BOTTOM);
        gbc.weightx = 0.5;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(speedLabel, gbc);

        JSlider speed = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);
        speed.setMajorTickSpacing(3);
        speed.setMinorTickSpacing(1);
        speed.setPaintTicks(true);
        speed.setPaintLabels(true);
        speed.addChangeListener(this::stateChanged);
        gbc.weightx = 0.5;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(speed, gbc);
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            int fps = (int) source.getValue();
            this.map.delayInMS = this.mainDelay / fps;
        }
    }

    public void start(ActionEvent e) {
        if (!this.map.isStarted)
            this.map.isStarted = true;
    }

    public void stop(ActionEvent e) {
        if (this.map.isStarted)
            this.map.isStarted = false;
    }

    public void run() {

    }
}
