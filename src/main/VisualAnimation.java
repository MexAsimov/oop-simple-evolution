package main;

import javax.swing.*;

public class VisualAnimation {
    public static void runInWindow(Field map, Field map2) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame window = new VisualMainFrame(map, map2);
            }
        });
    }
}
