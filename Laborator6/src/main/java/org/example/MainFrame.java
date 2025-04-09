package org.example;

import javax.swing.*;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    ConfigPanel configPanel;
    ControlPanel controlPanel;
    DrawingPanel canvas;

    public MainFrame() {
        super("My Drawing Application");
        init();
    }
    private void init()
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        configPanel=new ConfigPanel(this);
        controlPanel=new ControlPanel(this);
        canvas=new DrawingPanel(this);
        add(canvas, BorderLayout.CENTER);
        add(configPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);
        pack();
    }
}
