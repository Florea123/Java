package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

public class ConfigPanel extends JPanel {
    final MainFrame frame;
    JLabel label;
    JSpinner spinner;
    JLabel player1Score;
    JLabel player2Score;
    private final DecimalFormat scoreFormat = new DecimalFormat("0.00");

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init()
    {
        label=new JLabel("Number of dots:");
        spinner=new JSpinner(new SpinnerNumberModel(10,2,100,1));
        player1Score=new JLabel("Player 1 Score:");
        player2Score=new JLabel("Player 2 Score:");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 5, 20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(player1Score, gbc);

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 1;
        add(label, gbc);

        gbc.gridx = 2;
        add(spinner, gbc);

        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.gridx = 3;
        add(player2Score, gbc);
    }
    public void updateScore(double player1Score, double player2Score)
    {
        this.player1Score.setText("Player 1: " + scoreFormat.format(player1Score));
        this.player2Score.setText("Player 2: " + scoreFormat.format(player2Score));
    }
}
