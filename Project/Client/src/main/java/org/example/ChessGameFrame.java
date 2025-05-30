package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChessGameFrame extends JFrame {
    public ChessGameFrame() {
        setTitle("Chess Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JButton findGameButton = new JButton("Find Game");
        findGameButton.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(this, "Finding a game...");
        });

        add(findGameButton);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}