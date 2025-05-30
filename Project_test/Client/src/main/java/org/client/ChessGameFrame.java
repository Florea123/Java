package org.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ChessGameFrame extends JFrame {
    private final ChessClient client;

    public ChessGameFrame(ChessClient client) {
        this.client = client;

        setTitle("Chess Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JButton findGameButton = new JButton("Find Game");
        findGameButton.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(this, "Finding a game...");
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener((ActionEvent e) -> {
            try {
                client.sendMessage("LOGOUT " + client.getUsername());
                String response = client.receiveMessage();
                if ("LOGOUT_SUCCESS".equals(response)) {
                    client.setUsername(null);
                    dispose();
                    new LoginFrame(client);
                } else {
                    JOptionPane.showMessageDialog(this, "Logout failed: " + response);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener((ActionEvent e) -> {
            try {
                client.sendMessage("EXIT " + client.getUsername());
                String response = client.receiveMessage();
                if ("EXIT_SUCCESS".equals(response)) {
                    client.getSocket().close();
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Exit failed: " + response);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        add(findGameButton);
        add(logoutButton);
        add(exitButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}